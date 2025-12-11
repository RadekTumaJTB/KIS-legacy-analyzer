#!/usr/bin/env python3
"""
Deploy all DB_DSA sequences.
"""
import subprocess
from pathlib import Path

def deploy_sequence(seq_file, connection_string):
    """Deploy a single sequence."""
    seq_name = seq_file.stem

    # Copy to container
    cp_result = subprocess.run(
        ["docker", "cp", str(seq_file), "kis-oracle:/tmp/deploy_seq.sql"],
        capture_output=True,
        text=True
    )

    if cp_result.returncode != 0:
        return False, f"Failed to copy: {cp_result.stderr}"

    # Execute in Oracle
    exec_result = subprocess.run(
        ["docker", "exec", "kis-oracle", "bash", "-c",
         f"sqlplus -s {connection_string} @/tmp/deploy_seq.sql"],
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
    seqs_dir = Path("sources/DB/DB_DSA/SEQUENCES")
    connection_string = "DB_DSA/kis_db_dsa_2024@FREEPDB1"

    seq_files = sorted(seqs_dir.glob("*.sql"))

    print("=" * 80)
    print(f"DEPLOYING {len(seq_files)} DB_DSA SEQUENCES")
    print("=" * 80)
    print()

    deployed = 0
    failed = {}

    for seq_file in seq_files:
        seq_name = seq_file.stem
        success, message = deploy_sequence(seq_file, connection_string)

        if success:
            deployed += 1
            print(f"  ✓ {seq_name}")
        else:
            failed[seq_name] = message
            print(f"  ✗ {seq_name}: {message}")

    print()
    print("=" * 80)
    print("SUMMARY")
    print("=" * 80)
    print(f"✓ Deployed: {deployed}/{len(seq_files)}")
    print(f"✗ Failed: {len(failed)}/{len(seq_files)}")

    if failed:
        print("\nFailed sequences:")
        for name, error in sorted(failed.items()):
            print(f"  - {name}: {error}")

    # Verify
    verify_result = subprocess.run(
        ["docker", "exec", "kis-oracle", "bash", "-c",
         f"sqlplus -s {connection_string} <<'EOF'\nSET PAGESIZE 0\nSET FEEDBACK OFF\nSELECT COUNT(*) FROM user_sequences;\nEXIT;\nEOF"],
        capture_output=True,
        text=True
    )

    print(f"\nSequences in database: {verify_result.stdout.strip()}")
    print("=" * 80)

if __name__ == "__main__":
    main()
