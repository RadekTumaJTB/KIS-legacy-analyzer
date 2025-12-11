#!/usr/bin/env python3
"""
Remove PARTITION BY REFERENCE from tables for Oracle Free Edition compatibility.
These 3 tables cannot use reference partitioning in Oracle 23ai Free Edition.
"""
import re
from pathlib import Path

def fix_partition_by_reference(sql_file_path):
    """Remove PARTITION BY REFERENCE and all partition definitions."""
    try:
        with open(sql_file_path, 'r', encoding='utf-8') as f:
            content = f.read()
    except UnicodeDecodeError:
        with open(sql_file_path, 'r', encoding='latin-1') as f:
            content = f.read()

    original = content

    # Remove the PARTITION BY REFERENCE line and all partition definitions
    # Pattern: PARTITION BY REFERENCE ... followed by partition definitions up to ENABLE ROW MOVEMENT
    pattern = r'PARTITION BY REFERENCE[^)]*\)[^;]*ENABLE ROW MOVEMENT\s*;'
    content = re.sub(pattern, ');', content, flags=re.DOTALL)

    # Also remove any remaining partition-related clauses before the closing parenthesis
    content = re.sub(r'\s+PARTITION BY[^)]*\).*?ENABLE ROW MOVEMENT\s*;', ');', content, flags=re.DOTALL)

    if content != original:
        print(f"  ✓ Fixed: {sql_file_path.name}")
        return content
    else:
        print(f"  ⊘ No changes needed: {sql_file_path.name}")
        return None

def main():
    # The 3 tables that use PARTITION BY REFERENCE
    tables = [
        'KP_DAT_DOKLADZDROJDAT',
        'KP_DAT_DOKLADZDROJDATAGREGACE',
        'KP_REL_DOKLAD_ZDROJDAT'
    ]

    tables_dir = Path('sources/DB/DB_JT/TABLES')

    print("=" * 80)
    print("FIXING PARTITION BY REFERENCE TABLES")
    print("=" * 80)
    print(f"Removing PARTITION BY REFERENCE from {len(tables)} tables...")
    print()

    fixed_count = 0

    for table_name in tables:
        # Use the .bak file (which has inline FK constraints)
        sql_file = tables_dir / f"{table_name}.sql.bak"

        if not sql_file.exists():
            print(f"  ✗ File not found: {sql_file}")
            continue

        print(f"Processing {table_name}...")
        fixed_content = fix_partition_by_reference(sql_file)

        if fixed_content:
            # Write the fixed version to the regular .sql file
            output_file = tables_dir / f"{table_name}.sql"
            with open(output_file, 'w', encoding='utf-8') as f:
                f.write(fixed_content)
            print(f"  → Saved to: {output_file.name}")
            fixed_count += 1

        print()

    print("=" * 80)
    print("SUMMARY")
    print("=" * 80)
    print(f"✓ Fixed {fixed_count} tables")
    print(f"✓ Tables are now ready for deployment without partitioning")
    print()

if __name__ == '__main__':
    main()
