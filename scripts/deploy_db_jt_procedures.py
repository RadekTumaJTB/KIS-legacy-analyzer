#!/usr/bin/env python3
"""
Deploy all DB_JT procedures.
"""
import subprocess
from pathlib import Path

def deploy_procedure(proc_file, connection_string):
    """Deploy a single procedure."""
    proc_name = proc_file.stem

    # Copy to container
    cp_result = subprocess.run(
        ["docker", "cp", str(proc_file), "kis-oracle:/tmp/deploy_proc.sql"],
        capture_output=True,
        text=True
    )

    if cp_result.returncode != 0:
        return False, f"Failed to copy: {cp_result.stderr}"

    # Execute in Oracle
    exec_result = subprocess.run(
        ["docker", "exec", "kis-oracle", "bash", "-c",
         f"sqlplus -s {connection_string} @/tmp/deploy_proc.sql"],
        capture_output=True,
        text=True
    )

    output = exec_result.stdout + exec_result.stderr

    # Check for errors
    if "ORA-" in output and "created" not in output.lower():
        error_lines = [line for line in output.split('\n') if 'ORA-' in line]
        return False, ' '.join(error_lines[:1])

    return True, "Success"

def main():
    procs_dir = Path("sources/DB/DB_JT/PROCEDURES")
    connection_string = "DB_JT/kis_db_jt_2024@FREEPDB1"

    proc_files = sorted(procs_dir.glob("*.sql"))

    print("=" * 80)
    print(f"DEPLOYING {len(proc_files)} DB_JT PROCEDURES")
    print("=" * 80)
    print()

    deployed = 0
    failed = {}

    for proc_file in proc_files:
        proc_name = proc_file.stem
        success, message = deploy_procedure(proc_file, connection_string)

        if success:
            deployed += 1
            print(f"  ✓ {proc_name}")
        else:
            failed[proc_name] = message
            print(f"  ✗ {proc_name}: {message}")

    print()
    print("=" * 80)
    print("SUMMARY")
    print("=" * 80)
    print(f"✓ Deployed: {deployed}/{len(proc_files)}")
    print(f"✗ Failed: {len(failed)}/{len(proc_files)}")

    if failed:
        print("\nFailed procedures:")
        for name, error in sorted(failed.items())[:10]:
            print(f"  - {name}: {error}")
        if len(failed) > 10:
            print(f"  ... and {len(failed) - 10} more")

    # Verify
    verify_result = subprocess.run(
        ["docker", "exec", "kis-oracle", "bash", "-c",
         f"sqlplus -s {connection_string} <<'EOF'\nSET PAGESIZE 0\nSET FEEDBACK OFF\nSELECT COUNT(*) FROM user_procedures WHERE object_type = 'PROCEDURE';\nEXIT;\nEOF"],
        capture_output=True,
        text=True
    )

    print(f"\nProcedures in database: {verify_result.stdout.strip()}")
    print("=" * 80)

if __name__ == "__main__":
    main()
