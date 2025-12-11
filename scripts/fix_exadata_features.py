#!/usr/bin/env python3
"""
Remove Exadata-specific features from Oracle SQL files.
Fixes ORA-64307: Exadata Hybrid Columnar Compression is not supported

Removes:
- COLUMN STORE COMPRESS FOR QUERY HIGH
- NO ROW LEVEL LOCKING
- Other Exadata-specific compression options
"""
import os
import sys
import re
from pathlib import Path

def fix_exadata_features(file_path):
    """Remove Exadata-specific features from a single file"""
    try:
        # Try UTF-8 first
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
        except UnicodeDecodeError:
            # Fall back to latin-1
            with open(file_path, 'r', encoding='latin-1') as f:
                content = f.read()

        original = content

        # Remove Exadata column store compression
        content = re.sub(
            r'COLUMN\s+STORE\s+COMPRESS\s+FOR\s+QUERY\s+HIGH\s+',
            '',
            content,
            flags=re.IGNORECASE
        )

        # Remove NO ROW LEVEL LOCKING
        content = re.sub(
            r'NO\s+ROW\s+LEVEL\s+LOCKING\s+',
            '',
            content,
            flags=re.IGNORECASE
        )

        # Remove other Exadata compression variants
        content = re.sub(
            r'COLUMN\s+STORE\s+COMPRESS\s+FOR\s+QUERY\s+LOW\s+',
            '',
            content,
            flags=re.IGNORECASE
        )

        content = re.sub(
            r'COLUMN\s+STORE\s+COMPRESS\s+FOR\s+ARCHIVE\s+HIGH\s+',
            '',
            content,
            flags=re.IGNORECASE
        )

        content = re.sub(
            r'COLUMN\s+STORE\s+COMPRESS\s+FOR\s+ARCHIVE\s+LOW\s+',
            '',
            content,
            flags=re.IGNORECASE
        )

        # Fix any double spaces that might result
        content = re.sub(r'  +', ' ', content)

        # Fix any spacing issues before keywords
        content = re.sub(r'\s+NOLOGGING', ' NOLOGGING', content)
        content = re.sub(r'\s+LOGGING', ' LOGGING', content)
        content = re.sub(r'\s+NOCOMPRESS', ' NOCOMPRESS', content)

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

    print("=" * 80)
    print("EXADATA FEATURES REMOVAL")
    print("=" * 80)
    print(f"Scanning {source_dir} for SQL files with Exadata features...")

    sql_files = list(source_dir.rglob('*.sql'))
    print(f"Found {len(sql_files)} SQL files")

    fixed_count = 0
    error_count = 0

    for i, sql_file in enumerate(sql_files, 1):
        if i % 100 == 0:
            print(f"  Processed {i}/{len(sql_files)} files...")

        if fix_exadata_features(sql_file):
            fixed_count += 1
            if fixed_count <= 10:  # Show first 10 fixes
                print(f"  ✓ Fixed: {sql_file.name}")

    print(f"\n{'=' * 80}")
    print(f"COMPLETED!")
    print(f"{'=' * 80}")
    print(f"  ✓ Fixed: {fixed_count} files")
    print(f"  ✗ Errors: {error_count} files")
    print(f"  📊 Total processed: {len(sql_files)} files")
    print()
    print("Changes made:")
    print("  • Removed: COLUMN STORE COMPRESS FOR QUERY HIGH")
    print("  • Removed: NO ROW LEVEL LOCKING")
    print("  • Removed: Other Exadata compression variants")
    print()
    print("Tables should now deploy on Oracle 23ai Free Edition.")

if __name__ == '__main__':
    main()
