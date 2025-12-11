#!/usr/bin/env python3
"""
Deploy all unique views (excluding .! duplicate files).
"""
import subprocess
from pathlib import Path
import re

def get_view_name_from_file(view_file):
    """Extract view name from SQL file."""
    try:
        with open(view_file, 'r', encoding='utf-8', errors='ignore') as f:
            content = f.read()
            # Look for CREATE OR REPLACE ... VIEW "schema"."viewname"
            match = re.search(r'VIEW\s+"[^"]+"\."([^"]+)"', content, re.IGNORECASE)
            if match:
                return match.group(1)
    except:
        pass
    return None

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
    if "ORA-" in output:
        error_lines = [line for line in output.split('\n') if 'ORA-' in line]
        return False, ' '.join(error_lines[:1])

    # Success if no errors (views with FORCE don't always output "created")
    return True, "Success"

def get_existing_views(connection_string):
    """Get list of views in database."""
    result = subprocess.run(
        ["docker", "exec", "kis-oracle", "bash", "-c",
         f"sqlplus -s {connection_string} <<'EOF'\nSET PAGESIZE 0\nSET FEEDBACK OFF\nSELECT view_name FROM user_views;\nEXIT;\nEOF"],
        capture_output=True,
        text=True
    )

    views = set()
    for line in result.stdout.split('\n'):
        line = line.strip()
        if line and not line.startswith('---'):
            views.add(line)
    return views

def main():
    views_dir = Path("sources/DB/DB_JT/VIEWS")
    connection_string = "DB_JT/kis_db_jt_2024@FREEPDB1"

    # Get all unique view files (exclude .! duplicates)
    all_files = list(views_dir.glob("*.sql"))
    view_files = [f for f in all_files if '.!' not in f.name]

    print("=" * 80)
    print(f"DEPLOYING UNIQUE VIEWS")
    print("=" * 80)
    print(f"Total SQL files: {len(all_files)}")
    print(f"Unique views (excluding .! duplicates): {len(view_files)}")
    print()

    # Get existing views from database
    print("Checking existing views in database...")
    existing_views = get_existing_views(connection_string)
    print(f"Existing views in database: {len(existing_views)}")
    print()

    # Build mapping of file -> expected view name
    file_to_view = {}
    for vf in view_files:
        view_name = get_view_name_from_file(vf)
        if view_name:
            file_to_view[vf] = view_name

    # Find views that don't exist
    missing = []
    for vf, vname in file_to_view.items():
        if vname not in existing_views:
            missing.append((vf, vname))

    if not missing:
        print("✓ All views already deployed!")
        print(f"✓ Total views in database: {len(existing_views)}")
        return

    print(f"Found {len(missing)} missing views:")
    for vf, vname in missing[:10]:
        print(f"  - {vname}")
    if len(missing) > 10:
        print(f"  ... and {len(missing) - 10} more")
    print()

    # Deploy missing views
    print("Deploying missing views...")
    deployed = 0
    failed = {}

    for vf, vname in missing:
        success, message = deploy_view(vf, connection_string)
        if success:
            deployed += 1
            print(f"  ✓ {vname}")
        else:
            failed[vname] = message
            print(f"  ✗ {vname}: {message}")

    print()
    print("=" * 80)
    print("FINAL SUMMARY")
    print("=" * 80)
    print(f"✓ Deployed: {deployed}/{len(missing)}")
    print(f"✗ Failed: {len(failed)}/{len(missing)}")

    # Verify final count
    final_views = get_existing_views(connection_string)
    print(f"\nViews in database: {len(final_views)}")
    print("=" * 80)

if __name__ == "__main__":
    main()
