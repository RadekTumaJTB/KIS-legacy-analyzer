#!/usr/bin/env python3
"""
Deploy remaining tables using sqlplus directly.
This bypasses Python oracledb parsing issues.
"""
import subprocess
import sys
from pathlib import Path

def get_deployed_tables():
    """Get list of tables already in database."""
    cmd = [
        'docker', 'exec', 'kis-oracle', 'bash', '-c',
        "echo 'SET PAGESIZE 0; SET FEEDBACK OFF; SET HEADING OFF; SELECT table_name FROM user_tables ORDER BY table_name;' | sqlplus -s DB_JT/kis_db_jt_2024@FREEPDB1"
    ]

    result = subprocess.run(cmd, capture_output=True, text=True)
    if result.returncode != 0:
        print(f"Error getting deployed tables: {result.stderr}")
        return set()

    tables = set()
    for line in result.stdout.strip().split('\n'):
        line = line.strip()
        if line and not line.startswith('SQL>'):
            tables.add(line.upper())

    return tables

def get_all_table_files():
    """Get all table SQL files."""
    tables_dir = Path('sources/DB/DB_JT/TABLES')
    if not tables_dir.exists():
        print(f"Error: {tables_dir} not found")
        return {}

    table_files = {}
    for sql_file in tables_dir.glob('*.sql'):
        # Extract table name from filename
        table_name = sql_file.stem.upper()
        # Remove special characters from MacOS
        table_name = table_name.replace('.!', '').replace('!', '')
        # Extract number patterns like "30548" from filenames
        import re
        table_name = re.sub(r'\d+', '', table_name)
        table_files[table_name] = sql_file

    return table_files

def deploy_table_with_sqlplus(sql_file):
    """Deploy a single table using sqlplus."""
    print(f"  Deploying: {sql_file.name}")

    # Copy file to Docker container
    docker_path = '/tmp/table_deploy.sql'
    copy_cmd = ['docker', 'cp', str(sql_file), f'kis-oracle:{docker_path}']
    result = subprocess.run(copy_cmd, capture_output=True, text=True)

    if result.returncode != 0:
        print(f"    ✗ Failed to copy file: {result.stderr}")
        return False

    # Execute with sqlplus
    sql_cmd = [
        'docker', 'exec', 'kis-oracle', 'bash', '-c',
        f'sqlplus -s DB_JT/kis_db_jt_2024@FREEPDB1 @{docker_path}'
    ]

    result = subprocess.run(sql_cmd, capture_output=True, text=True)

    # Check for errors
    if result.returncode != 0 or 'ORA-' in result.stdout or 'ERROR' in result.stdout.upper():
        print(f"    ✗ Failed: {sql_file.name}")
        if result.stdout:
            # Print first error line
            for line in result.stdout.split('\n'):
                if 'ORA-' in line or 'ERROR' in line.upper():
                    print(f"      Error: {line.strip()}")
                    break
        return False
    else:
        print(f"    ✓ Success: {sql_file.name}")
        return True

def main():
    print("=" * 80)
    print("DEPLOYING REMAINING TABLES USING SQLPLUS")
    print("=" * 80)

    # Get deployed tables
    print("\nFetching list of already deployed tables...")
    deployed_tables = get_deployed_tables()
    print(f"Found {len(deployed_tables)} tables already in database")

    # Get all table files
    print("\nScanning for table SQL files...")
    all_table_files = get_all_table_files()
    print(f"Found {len(all_table_files)} table SQL files")

    # Find missing tables
    print("\nIdentifying missing tables...")
    missing_tables = []

    for table_name, sql_file in all_table_files.items():
        # Try to find this table in deployed tables
        # Handle various naming variations
        found = False
        for deployed in deployed_tables:
            if table_name in deployed or deployed in table_name:
                found = True
                break

        if not found:
            missing_tables.append(sql_file)

    print(f"\nFound {len(missing_tables)} tables to deploy")

    if not missing_tables:
        print("\n✅ All tables are already deployed!")
        return

    # Deploy missing tables
    print(f"\nDeploying {len(missing_tables)} missing tables using sqlplus...")
    print("-" * 80)

    success_count = 0
    failed_count = 0

    for sql_file in sorted(missing_tables):
        if deploy_table_with_sqlplus(sql_file):
            success_count += 1
        else:
            failed_count += 1

    # Final summary
    print("\n" + "=" * 80)
    print("DEPLOYMENT COMPLETE")
    print("=" * 80)
    print(f"✓ Successfully deployed: {success_count} tables")
    print(f"✗ Failed: {failed_count} tables")

    # Check final count
    final_deployed = get_deployed_tables()
    print(f"\n📊 Total tables in database: {len(final_deployed)}/752")

    if len(final_deployed) >= 752:
        print("\n🎉 SUCCESS! All 752 tables are now deployed!")
    else:
        print(f"\n⚠️  Still missing {752 - len(final_deployed)} tables")

if __name__ == '__main__':
    main()
