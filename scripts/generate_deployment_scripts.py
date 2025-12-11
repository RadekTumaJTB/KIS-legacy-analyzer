#!/usr/bin/env python3
"""
Oracle Database Deployment Script Generator
Generates complete deployment SQL scripts with dependency resolution
"""

import os
import sys
from pathlib import Path
import re

# Base paths
DB_JT_SOURCE = "/Users/radektuma/DEV/KIS/sources/DB/DB_JT"
DB_DSA_SOURCE = "/Users/radektuma/DEV/KIS/sources/DB/DB_DSA"
OUTPUT_DIR = "/Users/radektuma/DEV/KIS/kis-bff-simple/src/main/resources/db/migration/complete"

# Deployment order for DB_JT
DB_JT_DEPLOYMENT_ORDER = [
    ("TYPES", "types"),
    ("SEQUENCES", "sequences"),
    ("TABLES", "tables"),
    ("CONSTRAINTS", "constraints"),
    ("REF_CONSTRAINTS", "foreign keys"),
    ("MATERIALIZED_VIEWS", "materialized views"),
    ("VIEWS", "views"),
    ("FUNCTIONS", "functions"),
    ("PROCEDURES", "procedures"),
    ("PACKAGES", "package specifications"),
    ("PACKAGE_BODIES", "package bodies"),
    ("TRIGGERS", "triggers"),
    ("SYNONYMS", "synonyms"),
]

# Deployment order for DB_DSA
DB_DSA_DEPLOYMENT_ORDER = [
    ("SEQUENCES", "sequences"),
    ("TABLES", "tables"),
    ("CONSTRAINTS", "constraints"),
    ("REF_CONSTRAINTS", "foreign keys"),
    ("INDEXES", "indexes"),
    ("VIEWS", "views"),
    ("FUNCTIONS", "functions"),
    ("PROCEDURES", "procedures"),
    ("PACKAGES", "package specifications"),
    ("PACKAGE_BODIES", "package bodies"),
    ("TRIGGERS", "triggers"),
]


def replace_tablespace(sql_content):
    """Replace DBAJT and DBADSA tablespace with USERS"""
    replacements = [
        (r'TABLESPACE\s+"DBAJT"', 'TABLESPACE USERS'),
        (r'TABLESPACE\s+"DBADSA"', 'TABLESPACE USERS'),
        (r'TABLESPACE\s+DBAJT', 'TABLESPACE USERS'),
        (r'TABLESPACE\s+DBADSA', 'TABLESPACE USERS'),
    ]

    for pattern, replacement in replacements:
        sql_content = re.sub(pattern, replacement, sql_content, flags=re.IGNORECASE)

    return sql_content


def get_sql_files(base_path, category):
    """Get all SQL files from a category directory"""
    category_path = Path(base_path) / category

    if not category_path.exists():
        return []

    sql_files = sorted(category_path.glob("*.sql"))
    return sql_files


def generate_sql_block(sql_file, schema_name, category_name):
    """Generate SQL block with error handling"""

    # Read SQL content
    try:
        with open(sql_file, 'r', encoding='utf-8') as f:
            sql_content = f.read()
    except Exception as e:
        return f"-- ERROR: Cannot read {sql_file}: {e}\n\n"

    # Replace tablespace
    sql_content = replace_tablespace(sql_content)

    # Remove any existing trailing slashes or semicolons
    sql_content = sql_content.rstrip()
    if sql_content.endswith('/'):
        sql_content = sql_content[:-1].rstrip()
    if sql_content.endswith(';'):
        sql_content = sql_content[:-1].rstrip()

    file_name = sql_file.name

    # Generate deployment block
    block = f"""
-- ============================================================================
-- Deploy: {file_name}
-- Category: {category_name}
-- ============================================================================

PROMPT Deploying {file_name}...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
{sql_content}
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: {file_name}');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: {file_name} - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: {file_name} - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/

"""

    return block


def generate_deployment_script(schema_name, source_path, deployment_order, output_file):
    """Generate complete deployment script"""

    print(f"Generating deployment script for {schema_name}...")

    script_header = f"""-- ============================================================================
-- Oracle Database Deployment Script
-- Schema: {schema_name}
-- Generated: 2025-12-10
-- Target: Oracle 23ai Free (Docker)
-- Connection: {schema_name}/kis_{schema_name.lower()}_2024@//localhost:1521/FREEPDB1
-- ============================================================================

SET SERVEROUTPUT ON SIZE UNLIMITED
SET VERIFY OFF
SET FEEDBACK ON
SET ECHO ON

WHENEVER SQLERROR CONTINUE

-- Enable output buffering
ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD HH24:MI:SS';

PROMPT ============================================================================
PROMPT Starting {schema_name} Schema Deployment
PROMPT ============================================================================
PROMPT

"""

    script_footer = f"""
-- ============================================================================
-- Post-Deployment: Check Invalid Objects
-- ============================================================================

PROMPT ============================================================================
PROMPT Checking for INVALID objects...
PROMPT ============================================================================

SELECT object_type, object_name, status
FROM user_objects
WHERE status = 'INVALID'
ORDER BY object_type, object_name;

-- ============================================================================
-- Post-Deployment: Recompile Invalid Objects
-- ============================================================================

PROMPT ============================================================================
PROMPT Recompiling INVALID objects...
PROMPT ============================================================================

BEGIN
    FOR obj IN (SELECT object_name, object_type
                FROM user_objects
                WHERE status = 'INVALID'
                ORDER BY DECODE(object_type,
                    'PACKAGE', 1,
                    'PACKAGE BODY', 2,
                    'FUNCTION', 3,
                    'PROCEDURE', 4,
                    'TRIGGER', 5,
                    'VIEW', 6,
                    99))
    LOOP
        BEGIN
            IF obj.object_type = 'PACKAGE BODY' THEN
                EXECUTE IMMEDIATE 'ALTER PACKAGE ' || obj.object_name || ' COMPILE BODY';
            ELSIF obj.object_type = 'PACKAGE' THEN
                EXECUTE IMMEDIATE 'ALTER PACKAGE ' || obj.object_name || ' COMPILE';
            ELSIF obj.object_type = 'VIEW' THEN
                EXECUTE IMMEDIATE 'ALTER VIEW ' || obj.object_name || ' COMPILE';
            ELSIF obj.object_type = 'TRIGGER' THEN
                EXECUTE IMMEDIATE 'ALTER TRIGGER ' || obj.object_name || ' COMPILE';
            ELSIF obj.object_type = 'FUNCTION' THEN
                EXECUTE IMMEDIATE 'ALTER FUNCTION ' || obj.object_name || ' COMPILE';
            ELSIF obj.object_type = 'PROCEDURE' THEN
                EXECUTE IMMEDIATE 'ALTER PROCEDURE ' || obj.object_name || ' COMPILE';
            END IF;

            DBMS_OUTPUT.PUT_LINE('RECOMPILED: ' || obj.object_type || ' ' || obj.object_name);
        EXCEPTION
            WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE('FAILED: ' || obj.object_type || ' ' || obj.object_name || ': ' || SQLERRM);
        END;
    END LOOP;
END;
/

-- ============================================================================
-- Final Status Report
-- ============================================================================

PROMPT ============================================================================
PROMPT Final Object Status
PROMPT ============================================================================

SELECT object_type, status, COUNT(*) as count
FROM user_objects
GROUP BY object_type, status
ORDER BY object_type, status;

PROMPT ============================================================================
PROMPT {schema_name} Schema Deployment Complete
PROMPT ============================================================================

EXIT;
"""

    # Start building the script
    full_script = script_header

    total_files = 0

    # Process each category in order
    for category, category_desc in deployment_order:
        sql_files = get_sql_files(source_path, category)

        if not sql_files:
            continue

        total_files += len(sql_files)

        # Category header
        full_script += f"""
-- ============================================================================
-- PHASE: Deploy {category_desc.upper()} ({len(sql_files)} files)
-- ============================================================================

PROMPT
PROMPT ============================================================================
PROMPT Deploying {category_desc.upper()} ({len(sql_files)} files)
PROMPT ============================================================================
PROMPT

"""

        # Process each SQL file
        for sql_file in sql_files:
            full_script += generate_sql_block(sql_file, schema_name, category_desc)

        # Category footer
        full_script += f"""
PROMPT Completed {category_desc} deployment
PROMPT

"""

    # Add footer
    full_script += script_footer

    # Write output file
    output_path = Path(output_file)
    output_path.parent.mkdir(parents=True, exist_ok=True)

    with open(output_path, 'w', encoding='utf-8') as f:
        f.write(full_script)

    print(f"Generated: {output_file}")
    print(f"Total SQL files included: {total_files}")
    print()

    return total_files


def main():
    """Main execution"""
    print("=" * 80)
    print("Oracle Database Deployment Script Generator")
    print("=" * 80)
    print()

    # Generate DB_JT deployment script
    db_jt_output = f"{OUTPUT_DIR}/deploy_db_jt_complete.sql"
    db_jt_count = generate_deployment_script(
        "DB_JT",
        DB_JT_SOURCE,
        DB_JT_DEPLOYMENT_ORDER,
        db_jt_output
    )

    # Generate DB_DSA deployment script
    db_dsa_output = f"{OUTPUT_DIR}/deploy_db_dsa_complete.sql"
    db_dsa_count = generate_deployment_script(
        "DB_DSA",
        DB_DSA_SOURCE,
        DB_DSA_DEPLOYMENT_ORDER,
        db_dsa_output
    )

    print("=" * 80)
    print("Deployment Scripts Generated Successfully")
    print("=" * 80)
    print(f"DB_JT:  {db_jt_count} SQL files -> {db_jt_output}")
    print(f"DB_DSA: {db_dsa_count} SQL files -> {db_dsa_output}")
    print()
    print("To deploy, run:")
    print(f"  sqlplus DB_JT/kis_db_jt_2024@//localhost:1521/FREEPDB1 @{db_jt_output}")
    print(f"  sqlplus DB_DSA/kis_db_dsa_2024@//localhost:1521/FREEPDB1 @{db_dsa_output}")
    print()


if __name__ == "__main__":
    main()
