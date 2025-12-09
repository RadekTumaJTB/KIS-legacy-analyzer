#!/usr/bin/env python3
"""
Script to create DB_DSA schema from PL/SQL source files
"""
import os
import re
import glob

SOURCE_DIR = "/Users/radektuma/DEV/KIS/sources/DB/DB_DSA"
OUTPUT_FILE = "/Users/radektuma/DEV/KIS/db_schema_dsa.sql"

def clean_sql(content):
    """Remove schema prefix and clean SQL content"""
    # Remove "DB_DSA". prefix
    content = re.sub(r'"DB_DSA"\.', '', content)
    # Remove comments and dashes
    content = re.sub(r'--.*\n', '\n', content)
    # Remove storage and physical clauses
    content = re.sub(r'PCTFREE \d+', '', content)
    content = re.sub(r'PCTUSED \d+', '', content)
    content = re.sub(r'INITRANS \d+', '', content)
    content = re.sub(r'MAXTRANS \d+', '', content)
    content = re.sub(r'STORAGE\s*\([^)]+\)', '', content, flags=re.DOTALL)
    content = re.sub(r'TABLESPACE\s+"[^"]+"', '', content)
    content = re.sub(r'NOCOMPRESS\s+LOGGING', '', content)
    content = re.sub(r'SEGMENT CREATION IMMEDIATE', ';', content)
    content = re.sub(r'SEGMENT CREATION DEFERRED', ';', content)
    # Remove COMMENT ON statements
    content = re.sub(r'COMMENT ON .*?;', '', content, flags=re.DOTALL)
    # Clean up multiple newlines
    content = re.sub(r'\n\s*\n\s*\n+', '\n\n', content)
    return content

def main():
    # Get all table files
    table_files = sorted(glob.glob(os.path.join(SOURCE_DIR, "TABLES", "*.sql")))

    with open(OUTPUT_FILE, 'w', encoding='utf-8') as outfile:
        outfile.write("-- DB_DSA Schema - Data Staging Area\n")
        outfile.write("-- Generated from legacy PL/SQL sources\n")
        outfile.write("-- Target: Oracle 23c Free\n\n")
        outfile.write("SET ECHO ON\n")
        outfile.write("SET FEEDBACK ON\n")
        outfile.write("SET DEFINE OFF\n\n")

        # Process all tables
        outfile.write("-- ==========================================\n")
        outfile.write("-- TABLES\n")
        outfile.write("-- ==========================================\n\n")

        for table_file in table_files:
            table_name = os.path.basename(table_file).replace('.sql', '')
            print(f"Processing table: {table_name}")

            try:
                with open(table_file, 'r', encoding='utf-8', errors='ignore') as infile:
                    content = infile.read()
                    cleaned = clean_sql(content)
                    outfile.write(f"-- Table: {table_name}\n")
                    outfile.write(cleaned)
                    outfile.write("\n\n")
            except Exception as e:
                print(f"Error processing {table_name}: {e}")

        # Process constraints
        outfile.write("-- ==========================================\n")
        outfile.write("-- CONSTRAINTS\n")
        outfile.write("-- ==========================================\n\n")

        constraint_files = sorted(glob.glob(os.path.join(SOURCE_DIR, "CONSTRAINTS", "*.sql")))
        for constraint_file in constraint_files:
            table_name = os.path.basename(constraint_file).replace('.sql', '')
            print(f"Processing constraints: {table_name}")

            try:
                with open(constraint_file, 'r', encoding='utf-8', errors='ignore') as infile:
                    content = infile.read()
                    cleaned = clean_sql(content)
                    outfile.write(f"-- Constraints for: {table_name}\n")
                    outfile.write(cleaned)
                    outfile.write("\n\n")
            except Exception as e:
                print(f"Error processing constraints for {table_name}: {e}")

        # Process ref constraints
        outfile.write("-- ==========================================\n")
        outfile.write("-- REFERENTIAL CONSTRAINTS\n")
        outfile.write("-- ==========================================\n\n")

        ref_files = sorted(glob.glob(os.path.join(SOURCE_DIR, "REF_CONSTRAINTS", "*.sql")))
        for ref_file in ref_files:
            table_name = os.path.basename(ref_file).replace('.sql', '')
            print(f"Processing ref constraints: {table_name}")

            try:
                with open(ref_file, 'r', encoding='utf-8', errors='ignore') as infile:
                    content = infile.read()
                    cleaned = clean_sql(content)
                    outfile.write(f"-- Ref Constraints for: {table_name}\n")
                    outfile.write(cleaned)
                    outfile.write("\n\n")
            except Exception as e:
                print(f"Error processing ref constraints for {table_name}: {e}")

        outfile.write("-- Schema creation completed\n")
        outfile.write("COMMIT;\n")

    print(f"\n✓ DB_DSA Schema SQL created: {OUTPUT_FILE}")
    print(f"  Total tables: {len(table_files)}")
    print(f"\nTo import into Oracle:")
    print(f"docker exec -i kis-oracle sqlplus DB_DSA/kis_db_dsa_2024@FREEPDB1 < {OUTPUT_FILE}")

if __name__ == "__main__":
    main()
