#!/usr/bin/env python3
"""
Fix tablespace references in SQL files
Replace DBAJT, DBADSA, INDEXY, and deleted tablespaces with USERS
"""
import os
import sys
import re
from pathlib import Path

def fix_tablespace(file_path):
    """Replace tablespace references in a single file"""
    try:
        # Try UTF-8 first
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
        except UnicodeDecodeError:
            # Fall back to latin-1
            with open(file_path, 'r', encoding='latin-1') as f:
                content = f.read()

        # Replace tablespaces (with quotes)
        original = content
        content = content.replace('TABLESPACE "DBAJT"', 'TABLESPACE "USERS"')
        content = content.replace('TABLESPACE "DBADSA"', 'TABLESPACE "USERS"')
        content = content.replace('TABLESPACE "INDEXY"', 'TABLESPACE "USERS"')
        # Also without quotes (just in case)
        content = content.replace('TABLESPACE DBAJT', 'TABLESPACE USERS')
        content = content.replace('TABLESPACE DBADSA', 'TABLESPACE USERS')
        content = content.replace('TABLESPACE INDEXY', 'TABLESPACE USERS')
        # Fix deleted tablespaces (pattern: "_$deleted$X$Y")
        content = re.sub(r'TABLESPACE\s+"_\$deleted\$\d+\$\d+"', 'TABLESPACE "USERS"', content)

        # Write back if changed
        if content != original:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            return True
        return False
    except Exception as e:
        print(f"Error processing {file_path}: {e}", file=sys.stderr)
        return False

def main():
    source_dir = Path('sources/DB')
    if not source_dir.exists():
        print(f"Error: Directory {source_dir} not found")
        sys.exit(1)

    print(f"Scanning {source_dir} for SQL files...")
    sql_files = list(source_dir.rglob('*.sql'))
    print(f"Found {len(sql_files)} SQL files")

    fixed_count = 0
    error_count = 0

    for i, sql_file in enumerate(sql_files, 1):
        if i % 100 == 0:
            print(f"  Processed {i}/{len(sql_files)} files...")

        if fix_tablespace(sql_file):
            fixed_count += 1

    print(f"\nCompleted!")
    print(f"  Fixed: {fixed_count} files")
    print(f"  Errors: {error_count} files")
    print(f"  Total processed: {len(sql_files)} files")

if __name__ == '__main__':
    main()
