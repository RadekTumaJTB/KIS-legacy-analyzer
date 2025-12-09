#!/usr/bin/env python3
"""
Script to create Oracle schema from PL/SQL source files
Combines all necessary table definitions into a single SQL script
"""
import os
import re

SOURCE_DIR = "/Users/radektuma/DEV/KIS/sources/DB/DB_JT"
OUTPUT_FILE = "/Users/radektuma/DEV/KIS/db_schema_combined.sql"

# Main tables to include
MAIN_TABLES = [
    "KP_DAT_DOKUMENT",
    "KP_DAT_DOKUMENTRADEK",
    "KP_DAT_BUDGET",
    "KP_DAT_BUDGETPOLOZKA",
    "KP_CIS_DOKUMENT",
    "KP_CIS_DOKUMENTSTATUS",
    "KP_KTG_ODBOR",
    "KP_KTG_SPOLECNOST",
    "KP_KTG_UCETNISPOLECNOST",
    "KP_KTG_PROJEKT",
    "KP_CIS_TYPTRANSAKCE",
    "KP_CIS_MENA",
    "KP_KTG_APPUSER",  # Users table
]

def clean_sql(content):
    """Remove schema prefix and clean SQL content"""
    # Remove "DB_JT". prefix
    content = re.sub(r'"DB_JT"\.', '', content)
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
    # Remove COMMENT ON statements (will add them separately if needed)
    content = re.sub(r'COMMENT ON .*?;', '', content, flags=re.DOTALL)
    # Clean up multiple newlines
    content = re.sub(r'\n\s*\n\s*\n+', '\n\n', content)
    return content

def main():
    with open(OUTPUT_FILE, 'w', encoding='utf-8') as outfile:
        outfile.write("-- KIS Banking Application - Schema Creation\n")
        outfile.write("-- Generated from legacy PL/SQL sources\n")
        outfile.write("-- Target: Oracle 23c Free\n\n")
        outfile.write("SET ECHO ON\n")
        outfile.write("SET FEEDBACK ON\n")
        outfile.write("SET DEFINE OFF\n\n")

        # Process tables
        outfile.write("-- ==========================================\n")
        outfile.write("-- TABLES\n")
        outfile.write("-- ==========================================\n\n")

        for table in MAIN_TABLES:
            table_file = os.path.join(SOURCE_DIR, "TABLES", f"{table}.sql")

            if os.path.exists(table_file):
                print(f"Processing table: {table}")
                try:
                    with open(table_file, 'r', encoding='utf-8', errors='ignore') as infile:
                        content = infile.read()
                        cleaned = clean_sql(content)
                        outfile.write(f"-- Table: {table}\n")
                        outfile.write(cleaned)
                        outfile.write("\n\n")
                except Exception as e:
                    print(f"Error processing {table}: {e}")
            else:
                print(f"Warning: Table file not found: {table_file}")

        # Process constraints if they exist
        outfile.write("-- ==========================================\n")
        outfile.write("-- CONSTRAINTS\n")
        outfile.write("-- ==========================================\n\n")

        for table in MAIN_TABLES:
            constraint_file = os.path.join(SOURCE_DIR, "CONSTRAINTS", f"{table}.sql")

            if os.path.exists(constraint_file):
                print(f"Processing constraints: {table}")
                try:
                    with open(constraint_file, 'r', encoding='utf-8', errors='ignore') as infile:
                        content = infile.read()
                        cleaned = clean_sql(content)
                        outfile.write(f"-- Constraints for: {table}\n")
                        outfile.write(cleaned)
                        outfile.write("\n\n")
                except Exception as e:
                    print(f"Error processing constraints for {table}: {e}")

        # Process ref constraints (foreign keys)
        outfile.write("-- ==========================================\n")
        outfile.write("-- REFERENTIAL CONSTRAINTS (Foreign Keys)\n")
        outfile.write("-- ==========================================\n\n")

        for table in MAIN_TABLES:
            ref_file = os.path.join(SOURCE_DIR, "REF_CONSTRAINTS", f"{table}.sql")

            if os.path.exists(ref_file):
                print(f"Processing ref constraints: {table}")
                try:
                    with open(ref_file, 'r', encoding='utf-8', errors='ignore') as infile:
                        content = infile.read()
                        cleaned = clean_sql(content)
                        outfile.write(f"-- Ref Constraints for: {table}\n")
                        outfile.write(cleaned)
                        outfile.write("\n\n")
                except Exception as e:
                    print(f"Error processing ref constraints for {table}: {e}")

        outfile.write("-- Schema creation completed\n")
        outfile.write("COMMIT;\n")

    print(f"\n✓ Schema SQL created: {OUTPUT_FILE}")
    print(f"\nTo import into Oracle:")
    print(f"docker exec -i kis-oracle sqlplus DB_JT/kis_db_jt_2024@FREEPDB1 < {OUTPUT_FILE}")

if __name__ == "__main__":
    main()
