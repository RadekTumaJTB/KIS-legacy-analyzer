#!/usr/bin/env python3
"""
Deploy all DB_DSA tables directly using sqlplus.
"""
import subprocess
from pathlib import Path

def deploy_table(table_file, connection_string):
    """Deploy a single table."""
    table_name = table_file.stem

    # Copy to container
    cp_result = subprocess.run(
        ["docker", "cp", str(table_file), "kis-oracle:/tmp/deploy_table.sql"],
        capture_output=True,
        text=True
    )

    if cp_result.returncode != 0:
        return False, f"Failed to copy: {cp_result.stderr}"

    # Execute in Oracle
    exec_result = subprocess.run(
        ["docker", "exec", "kis-oracle", "bash", "-c",
         f"sqlplus -s {connection_string} @/tmp/deploy_table.sql"],
        capture_output=True,
        text=True
    )

    output = exec_result.stdout + exec_result.stderr

    # Check for success
    if "created" in output.lower() or "Table created" in output:
        return True, "Success"
    elif "ORA-" in output:
        # Extract error
        error_lines = [line for line in output.split('\n') if 'ORA-' in line]
        return False, ' '.join(error_lines[:1])
    else:
        return False, "Unknown status"

def main():
    tables_dir = Path("sources/DB/DB_DSA/TABLES")
    connection_string = "DB_DSA/kis_db_dsa_2024@FREEPDB1"

    table_files = sorted(tables_dir.glob("*.sql"))

    print("=" * 80)
    print(f"DEPLOYING {len(table_files)} DB_DSA TABLES")
    print("=" * 80)
    print()

    deployed = 0
    failed = {}

    for table_file in table_files:
        table_name = table_file.stem
        success, message = deploy_table(table_file, connection_string)

        if success:
            deployed += 1
            print(f"  ✓ {table_name}")
        else:
            failed[table_name] = message
            print(f"  ✗ {table_name}: {message}")

    print()
    print("=" * 80)
    print("SUMMARY")
    print("=" * 80)
    print(f"✓ Deployed: {deployed}/{len(table_files)}")
    print(f"✗ Failed: {len(failed)}/{len(table_files)}")

    if failed:
        print("\nFailed tables:")
        for name, error in sorted(failed.items())[:10]:
            print(f"  - {name}: {error}")
        if len(failed) > 10:
            print(f"  ... and {len(failed) - 10} more")

    # Verify
    verify_result = subprocess.run(
        ["docker", "exec", "kis-oracle", "bash", "-c",
         f"sqlplus -s {connection_string} <<'EOF'\nSET PAGESIZE 0\nSET FEEDBACK OFF\nSELECT COUNT(*) FROM user_tables;\nEXIT;\nEOF"],
        capture_output=True,
        text=True
    )

    print(f"\nTables in database: {verify_result.stdout.strip()}")
    print("=" * 80)

if __name__ == "__main__":
    main()
