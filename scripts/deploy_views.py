#!/usr/bin/env python3
"""
Deploy all views from sources/DB/DB_JT/VIEWS to Oracle database.
Views may have dependencies, so we'll retry failed views until all succeed.
"""
import os
import subprocess
from pathlib import Path
import time

def deploy_view(view_file, connection_string):
    """Deploy a single view file."""
    view_name = view_file.stem

    # Copy to container
    container_path = f"/tmp/{view_name}.sql"
    cp_result = subprocess.run(
        ["docker", "cp", str(view_file), f"kis-oracle:{container_path}"],
        capture_output=True,
        text=True
    )

    if cp_result.returncode != 0:
        return False, f"Failed to copy: {cp_result.stderr}"

    # Execute in Oracle
    exec_result = subprocess.run(
        ["docker", "exec", "kis-oracle", "bash", "-c",
         f"sqlplus -s {connection_string} @{container_path}"],
        capture_output=True,
        text=True
    )

    output = exec_result.stdout + exec_result.stderr

    # Check for errors
    if "ORA-" in output and "created" not in output.lower():
        # Extract just the error code and message
        error_lines = [line for line in output.split('\n') if 'ORA-' in line]
        return False, ' '.join(error_lines[:2])  # First 2 error lines

    if "created" in output.lower() or "view created" in output.lower():
        return True, "Success"

    # No clear error but also no success message
    return False, "Unknown status"

def main():
    views_dir = Path("sources/DB/DB_JT/VIEWS")
    connection_string = "DB_JT/kis_db_jt_2024@FREEPDB1"

    # Get all view files
    view_files = sorted(views_dir.glob("*.sql"))
    total_views = len(view_files)

    print("=" * 80)
    print(f"DEPLOYING {total_views} VIEWS")
    print("=" * 80)
    print()

    max_rounds = 10
    deployed = set()
    failed = {}

    for round_num in range(1, max_rounds + 1):
        print(f"\n{'='*80}")
        print(f"ROUND {round_num}/{max_rounds}")
        print(f"{'='*80}")

        # Try to deploy views that haven't been deployed yet
        to_deploy = [vf for vf in view_files if vf.stem not in deployed]

        if not to_deploy:
            print("\n✓ All views deployed successfully!")
            break

        print(f"Attempting to deploy {len(to_deploy)} views...")

        round_deployed = 0
        round_failed = 0

        for i, view_file in enumerate(to_deploy, 1):
            view_name = view_file.stem

            # Progress indicator
            if i % 50 == 0 or i == len(to_deploy):
                print(f"  Progress: {i}/{len(to_deploy)} ({round_deployed} ✓, {round_failed} ✗)")

            success, message = deploy_view(view_file, connection_string)

            if success:
                deployed.add(view_name)
                round_deployed += 1
                if view_name in failed:
                    del failed[view_name]
            else:
                failed[view_name] = message
                round_failed += 1

        print(f"\nRound {round_num} Summary:")
        print(f"  ✓ Deployed: {round_deployed}")
        print(f"  ✗ Failed: {round_failed}")
        print(f"  Total deployed: {len(deployed)}/{total_views}")

        # If we didn't deploy any new views this round, we're stuck
        if round_deployed == 0 and round_failed > 0:
            print("\n⚠ No progress made in this round. Remaining failures:")
            for view_name, error in list(failed.items())[:10]:  # Show first 10
                print(f"  - {view_name}: {error}")
            if len(failed) > 10:
                print(f"  ... and {len(failed) - 10} more")
            break

    print("\n" + "=" * 80)
    print("FINAL SUMMARY")
    print("=" * 80)
    print(f"✓ Successfully deployed: {len(deployed)}/{total_views}")
    print(f"✗ Failed: {len(failed)}/{total_views}")

    if failed:
        print("\nFailed views:")
        for view_name, error in sorted(failed.items())[:20]:  # Show first 20
            print(f"  - {view_name}: {error}")
        if len(failed) > 20:
            print(f"  ... and {len(failed) - 20} more")

    # Verify in database
    print("\nVerifying in database...")
    verify_result = subprocess.run(
        ["docker", "exec", "kis-oracle", "bash", "-c",
         f"sqlplus -s {connection_string} <<'EOF'\nSET PAGESIZE 0\nSET FEEDBACK OFF\nSELECT COUNT(*) FROM user_views;\nEXIT;\nEOF"],
        capture_output=True,
        text=True
    )

    db_count = verify_result.stdout.strip()
    print(f"Views in database: {db_count}")
    print("=" * 80)

if __name__ == "__main__":
    main()
