#!/usr/bin/env python3
"""
Strip foreign key constraints from CREATE TABLE statements.
Save constraints as separate ALTER TABLE statements.
This allows two-phase deployment to resolve circular dependencies.
"""
import os
import sys
import re
from pathlib import Path
from typing import Tuple, List

def extract_foreign_keys(sql_content: str, table_name: str, schema_name: str) -> Tuple[str, List[str]]:
    """
    Extract foreign key constraints from CREATE TABLE statement.

    Returns:
        tuple: (cleaned_sql, list_of_alter_statements)
    """
    alter_statements = []

    # Pattern to match CONSTRAINT ... FOREIGN KEY within CREATE TABLE
    # Handles multi-line constraints
    fk_pattern = r',?\s*CONSTRAINT\s+"([^"]+)"\s+FOREIGN KEY\s+\([^)]+\)\s+REFERENCES\s+[^)]+\)\s+(?:ENABLE|DISABLE)?'

    def fk_replacement(match):
        """Extract FK constraint and create ALTER TABLE statement"""
        constraint_text = match.group(0).strip()

        # Remove leading comma if present
        if constraint_text.startswith(','):
            constraint_text = constraint_text[1:].strip()

        # Extract constraint name
        constraint_name_match = re.search(r'CONSTRAINT\s+"([^"]+)"', constraint_text)
        if constraint_name_match:
            constraint_name = constraint_name_match.group(1)

            # Create ALTER TABLE statement
            alter_sql = f'ALTER TABLE "{schema_name}"."{table_name}" ADD {constraint_text};'
            alter_statements.append((constraint_name, alter_sql))

        # Return empty string to remove from CREATE TABLE
        return ''

    # Remove foreign key constraints
    cleaned_sql = re.sub(fk_pattern, fk_replacement, sql_content, flags=re.IGNORECASE | re.DOTALL)

    # Clean up any resulting double commas or trailing commas before closing parenthesis
    cleaned_sql = re.sub(r',\s*,', ',', cleaned_sql)
    cleaned_sql = re.sub(r',\s*\)', ')', cleaned_sql)

    return cleaned_sql, alter_statements

def extract_table_info(sql_content: str) -> Tuple[str, str]:
    """Extract table name and schema from CREATE TABLE statement"""
    match = re.search(r'CREATE\s+TABLE\s+"([^"]+)"."([^"]+)"', sql_content, re.IGNORECASE)
    if match:
        return match.group(1), match.group(2)  # schema, table
    return None, None

def process_table_file(file_path: Path, constraints_dir: Path) -> dict:
    """
    Process a single table SQL file.

    Returns:
        dict: Statistics about processing
    """
    try:
        # Read file
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
        except UnicodeDecodeError:
            with open(file_path, 'r', encoding='latin-1') as f:
                content = f.read()

        # Check if this is a CREATE TABLE statement
        if 'CREATE TABLE' not in content.upper():
            return {'skipped': True, 'reason': 'not_create_table'}

        # Extract table info
        schema_name, table_name = extract_table_info(content)
        if not schema_name or not table_name:
            return {'skipped': True, 'reason': 'no_table_info'}

        # Extract foreign keys
        cleaned_sql, fk_constraints = extract_foreign_keys(content, table_name, schema_name)

        if not fk_constraints:
            return {'skipped': True, 'reason': 'no_foreign_keys'}

        # Write cleaned SQL back to original file
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(cleaned_sql)

        # Write ALTER TABLE statements to constraints directory
        constraint_file = constraints_dir / file_path.name
        with open(constraint_file, 'w', encoding='utf-8') as f:
            f.write(f"-- Foreign key constraints for {schema_name}.{table_name}\n")
            f.write(f"-- Extracted from {file_path.name}\n\n")
            for constraint_name, alter_sql in fk_constraints:
                f.write(f"-- Constraint: {constraint_name}\n")
                f.write(alter_sql)
                f.write('\n\n')

        return {
            'processed': True,
            'table': f'{schema_name}.{table_name}',
            'fk_count': len(fk_constraints),
            'constraint_file': str(constraint_file)
        }

    except Exception as e:
        return {'error': True, 'message': str(e), 'file': str(file_path)}

def main():
    source_dir = Path('sources/DB')
    if not source_dir.exists():
        print(f"Error: Directory {source_dir} not found")
        sys.exit(1)

    print("=" * 80)
    print("FOREIGN KEY CONSTRAINT EXTRACTION")
    print("=" * 80)
    print(f"Source directory: {source_dir}")

    # Process each schema
    for schema_dir in ['DB_JT', 'DB_DSA']:
        schema_path = source_dir / schema_dir
        if not schema_path.exists():
            print(f"Warning: Schema directory {schema_path} not found, skipping")
            continue

        tables_dir = schema_path / 'TABLES'
        if not tables_dir.exists():
            print(f"Warning: Tables directory {tables_dir} not found, skipping")
            continue

        # Create constraints directory
        constraints_dir = schema_path / 'CONSTRAINTS_FK'
        constraints_dir.mkdir(exist_ok=True)

        print(f"\nProcessing schema: {schema_dir}")
        print(f"  Tables directory: {tables_dir}")
        print(f"  Constraints directory: {constraints_dir}")

        # Find all SQL files
        sql_files = list(tables_dir.glob('*.sql'))
        print(f"  Found {len(sql_files)} SQL files")

        # Process files
        stats = {
            'processed': 0,
            'skipped': 0,
            'errors': 0,
            'total_fks': 0
        }

        for i, sql_file in enumerate(sql_files, 1):
            if i % 50 == 0:
                print(f"    Progress: {i}/{len(sql_files)} files...")

            result = process_table_file(sql_file, constraints_dir)

            if result.get('processed'):
                stats['processed'] += 1
                stats['total_fks'] += result['fk_count']
                if i % 10 == 0 or result['fk_count'] > 5:
                    print(f"    ✓ {sql_file.name}: {result['fk_count']} FKs extracted → {Path(result['constraint_file']).name}")
            elif result.get('skipped'):
                stats['skipped'] += 1
            elif result.get('error'):
                stats['errors'] += 1
                print(f"    ✗ Error processing {sql_file.name}: {result['message']}")

        print(f"\n  Summary for {schema_dir}:")
        print(f"    ✓ Processed: {stats['processed']} tables")
        print(f"    ✓ Foreign keys extracted: {stats['total_fks']}")
        print(f"    ⊘ Skipped: {stats['skipped']} files")
        print(f"    ✗ Errors: {stats['errors']} files")

    print("\n" + "=" * 80)
    print("EXTRACTION COMPLETE")
    print("=" * 80)
    print("\nNext steps:")
    print("  1. Deploy tables (without FK constraints)")
    print("  2. Deploy foreign key constraints from CONSTRAINTS_FK directories")

if __name__ == '__main__':
    main()
