#!/usr/bin/env python3
"""
Oracle SQL Script Converter for Oracle 23ai Free
Converts legacy DDL scripts to Oracle 23ai Free compatible format
"""

import os
import re
import sys
from pathlib import Path

def convert_sql_content(content, schema_name):
    """
    Convert SQL content to Oracle 23ai Free compatible format

    Args:
        content: Original SQL content
        schema_name: Schema name (DB_JT or DB_DSA)

    Returns:
        Converted SQL content
    """
    # Replace TABLESPACE names
    if schema_name == "DB_JT":
        content = re.sub(r'TABLESPACE\s+"DBAJT"', 'TABLESPACE "USERS"', content, flags=re.IGNORECASE)
    elif schema_name == "DB_DSA":
        content = re.sub(r'TABLESPACE\s+"DBADSA"', 'TABLESPACE "USERS"', content, flags=re.IGNORECASE)

    # Remove STORAGE clauses that can cause issues in Oracle 23ai Free
    content = re.sub(
        r'STORAGE\s*\([^)]*\)',
        '',
        content,
        flags=re.IGNORECASE | re.DOTALL
    )

    # Remove PCTFREE, PCTUSED, INITRANS, MAXTRANS from table definitions
    content = re.sub(r'\s+PCTFREE\s+\d+', '', content, flags=re.IGNORECASE)
    content = re.sub(r'\s+PCTUSED\s+\d+', '', content, flags=re.IGNORECASE)
    content = re.sub(r'\s+INITRANS\s+\d+', '', content, flags=re.IGNORECASE)
    content = re.sub(r'\s+MAXTRANS\s+\d+', '', content, flags=re.IGNORECASE)

    # Clean up multiple spaces
    content = re.sub(r'\s+', ' ', content)
    content = re.sub(r'\s+;', ';', content)

    # Restore newlines after semicolons
    content = re.sub(r';', ';\n', content)

    return content

def create_deployment_script(source_dir, schema_name, output_file):
    """
    Create master deployment script with proper ordering

    Args:
        source_dir: Source directory containing SQL files
        schema_name: Schema name (DB_JT or DB_DSA)
        output_file: Output deployment script file
    """

    # Deployment order - critical for dependencies
    deployment_order = [
        'TYPES',
        'SEQUENCES',
        'TABLES',
        'MATERIALIZED_VIEWS',
        'VIEWS',
        'INDEXES',
        'CONSTRAINTS',
        'REF_CONSTRAINTS',
        'FUNCTIONS',
        'PROCEDURES',
        'PACKAGES',
        'PACKAGE_BODIES',
        'TRIGGERS',
        'SYNONYMS',
        'DATABASE_LINKS'
    ]

    with open(output_file, 'w', encoding='utf-8') as outf:
        # Write header
        outf.write(f"""--==============================================================================
-- COMPLETE DEPLOYMENT SCRIPT FOR {schema_name}
-- Generated for Oracle 23ai Free
-- Target: kis-oracle Docker container
-- Connection: {schema_name}/kis_{schema_name.lower()}_2024@//localhost:1521/FREEPDB1
--==============================================================================

SET DEFINE OFF;
SET SERVEROUTPUT ON;
WHENEVER SQLERROR CONTINUE;

-- Deployment log table
BEGIN
    EXECUTE IMMEDIATE 'CREATE TABLE deployment_log (
        id NUMBER GENERATED ALWAYS AS IDENTITY,
        object_type VARCHAR2(50),
        object_name VARCHAR2(200),
        status VARCHAR2(20),
        error_message VARCHAR2(4000),
        deployment_date TIMESTAMP DEFAULT SYSTIMESTAMP
    )';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -955 THEN -- Table already exists
            RAISE;
        END IF;
END;
/

PROMPT ================================================================================
PROMPT Starting deployment for {schema_name}
PROMPT ================================================================================

""")

        total_files = 0

        # Process each object type in order
        for obj_type in deployment_order:
            obj_dir = Path(source_dir) / obj_type

            if not obj_dir.exists():
                print(f"Directory not found: {obj_dir}")
                continue

            sql_files = sorted(obj_dir.glob('*.sql'))

            if not sql_files:
                continue

            outf.write(f"\n{'='*80}\n")
            outf.write(f"-- DEPLOYING {obj_type} ({len(sql_files)} files)\n")
            outf.write(f"{'='*80}\n\n")

            for sql_file in sql_files:
                total_files += 1

                try:
                    with open(sql_file, 'r', encoding='utf-8', errors='ignore') as inf:
                        content = inf.read()

                    # Convert content
                    converted = convert_sql_content(content, schema_name)

                    outf.write(f"\nPROMPT Processing {obj_type}: {sql_file.name}\n")
                    outf.write(f"-- Source: {sql_file.relative_to(source_dir)}\n")
                    outf.write("BEGIN\n")
                    outf.write("    EXECUTE IMMEDIATE q'[\n")
                    outf.write(converted)
                    outf.write("\n]';\n")
                    outf.write(f"    INSERT INTO deployment_log (object_type, object_name, status) ")
                    outf.write(f"VALUES ('{obj_type}', '{sql_file.stem}', 'SUCCESS');\n")
                    outf.write("    COMMIT;\n")
                    outf.write("EXCEPTION\n")
                    outf.write("    WHEN OTHERS THEN\n")
                    outf.write(f"        INSERT INTO deployment_log (object_type, object_name, status, error_message) ")
                    outf.write(f"VALUES ('{obj_type}', '{sql_file.stem}', 'ERROR', SQLERRM);\n")
                    outf.write("        COMMIT;\n")
                    outf.write(f"        DBMS_OUTPUT.PUT_LINE('ERROR in {sql_file.name}: ' || SQLERRM);\n")
                    outf.write("END;\n")
                    outf.write("/\n\n")

                except Exception as e:
                    print(f"Error processing {sql_file}: {e}")
                    outf.write(f"-- ERROR processing {sql_file.name}: {e}\n\n")

        # Write summary and validation section
        outf.write(f"""

{'='*80}
-- DEPLOYMENT SUMMARY
{'='*80}

PROMPT
PROMPT ================================================================================
PROMPT DEPLOYMENT COMPLETED FOR {schema_name}
PROMPT ================================================================================
PROMPT

-- Display deployment statistics
SELECT
    object_type,
    status,
    COUNT(*) as count
FROM deployment_log
GROUP BY object_type, status
ORDER BY object_type, status;

PROMPT
PROMPT ================================================================================
PROMPT INVALID OBJECTS REPORT
PROMPT ================================================================================
PROMPT

-- Show invalid objects
SELECT
    object_type,
    object_name,
    status
FROM user_objects
WHERE status = 'INVALID'
ORDER BY object_type, object_name;

PROMPT
PROMPT ================================================================================
PROMPT ERROR LOG
PROMPT ================================================================================
PROMPT

-- Show errors from deployment
SELECT
    object_type,
    object_name,
    error_message,
    deployment_date
FROM deployment_log
WHERE status = 'ERROR'
ORDER BY deployment_date;

PROMPT
PROMPT ================================================================================
PROMPT OBJECT COUNT BY TYPE
PROMPT ================================================================================
PROMPT

-- Count all objects
SELECT
    object_type,
    COUNT(*) as count,
    SUM(CASE WHEN status = 'VALID' THEN 1 ELSE 0 END) as valid_count,
    SUM(CASE WHEN status = 'INVALID' THEN 1 ELSE 0 END) as invalid_count
FROM user_objects
GROUP BY object_type
ORDER BY object_type;

PROMPT
PROMPT Total files processed: {total_files}
PROMPT Deployment script completed.
PROMPT
""")

    print(f"Created deployment script: {output_file}")
    print(f"Total files processed: {total_files}")

def main():
    """Main execution"""

    # Configuration
    db_jt_source = "/Users/radektuma/DEV/KIS/sources/DB/DB_JT"
    db_dsa_source = "/Users/radektuma/DEV/KIS/sources/DB/DB_DSA"
    output_dir = "/Users/radektuma/DEV/KIS/kis-bff-simple/src/main/resources/db/migration/complete"

    print("="*80)
    print("Oracle 23ai Free SQL Deployment Script Generator")
    print("="*80)
    print()

    # Create DB_JT deployment script
    print("Processing DB_JT schema...")
    db_jt_output = os.path.join(output_dir, "deploy_db_jt_complete.sql")
    create_deployment_script(db_jt_source, "DB_JT", db_jt_output)
    print()

    # Create DB_DSA deployment script
    print("Processing DB_DSA schema...")
    db_dsa_output = os.path.join(output_dir, "deploy_db_dsa_complete.sql")
    create_deployment_script(db_dsa_source, "DB_DSA", db_dsa_output)
    print()

    print("="*80)
    print("Deployment scripts generated successfully!")
    print("="*80)
    print()
    print("Next steps:")
    print(f"1. Deploy DB_JT:  docker exec -i kis-oracle sqlplus DB_JT/kis_db_jt_2024@//localhost:1521/FREEPDB1 < {db_jt_output}")
    print(f"2. Deploy DB_DSA: docker exec -i kis-oracle sqlplus DB_DSA/kis_db_dsa_2024@//localhost:1521/FREEPDB1 < {db_dsa_output}")
    print()

if __name__ == "__main__":
    main()
