#!/usr/bin/env python3
"""
Fix PARTITION BY REFERENCE tables for Oracle Free Edition:
1. Remove PARTITION BY REFERENCE and all partition definitions
2. Fix double closing parentheses
3. Replace INDEXY tablespace with USERS
"""
import re
from pathlib import Path

def fix_partition_by_reference_v2(sql_file_path):
    """Properly fix PARTITION BY REFERENCE tables."""
    try:
        with open(sql_file_path, 'r', encoding='utf-8') as f:
            content = f.read()
    except UnicodeDecodeError:
        with open(sql_file_path, 'r', encoding='latin-1') as f:
            content = f.read()

    original = content

    # Step 1: Remove PARTITION BY REFERENCE line and all partition definitions
    # Find the line with PARTITION BY REFERENCE
    content = re.sub(
        r'\s+PARTITION BY REFERENCE\s+\([^)]+\)[^;]*?ENABLE ROW MOVEMENT\s*;',
        '',
        content,
        flags=re.DOTALL
    )

    # Step 2: Fix double closing parentheses ););
    content = re.sub(r'\)\s*;\s*\);', ');', content)

    # Step 3: Add TABLESPACE USERS to CREATE TABLE if missing
    # Look for closing parenthesis followed by ALTER TABLE without TABLESPACE between them
    if 'TABLESPACE "USERS"' not in content and 'CREATE TABLE' in content:
        # Add TABLESPACE before the semicolon after the closing parenthesis
        content = re.sub(
            r'(\s+BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT\))\s*;',
            r'\1\n TABLESPACE "USERS";',
            content
        )

    # Step 4: Replace INDEXY tablespace with USERS
    content = content.replace('TABLESPACE "INDEXY"', 'TABLESPACE "USERS"')
    content = content.replace("TABLESPACE 'INDEXY'", "TABLESPACE 'USERS'")
    content = content.replace("INDEXY", "USERS")

    if content != original:
        return content
    return None

def main():
    tables = [
        'KP_DAT_DOKLADZDROJDAT',
        'KP_DAT_DOKLADZDROJDATAGREGACE',
        'KP_REL_DOKLAD_ZDROJDAT'
    ]

    tables_dir = Path('sources/DB/DB_JT/TABLES')

    print("=" * 80)
    print("FIXING PARTITION BY REFERENCE TABLES (v2)")
    print("=" * 80)
    print()

    for table_name in tables:
        # Use the .bak file (which has inline FK constraints)
        sql_file = tables_dir / f"{table_name}.sql.bak"

        if not sql_file.exists():
            print(f"✗ File not found: {sql_file}")
            continue

        print(f"Processing {table_name}...")
        fixed_content = fix_partition_by_reference_v2(sql_file)

        if fixed_content:
            output_file = tables_dir / f"{table_name}.sql"
            with open(output_file, 'w', encoding='utf-8') as f:
                f.write(fixed_content)
            print(f"  ✓ Fixed and saved to: {output_file.name}")
        else:
            print(f"  ⊘ No changes needed")

        print()

    print("=" * 80)
    print("DONE")
    print("=" * 80)

if __name__ == '__main__':
    main()
