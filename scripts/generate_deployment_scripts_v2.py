#!/usr/bin/env python3
"""
Oracle Database Deployment Script Generator V2
Generates complete deployment SQL scripts with direct execution (no EXECUTE IMMEDIATE)
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
    """Generate SQL block with error handling using anonymous PL/SQL block"""

    # Read SQL content
    try:
        with open(sql_file, 'r', encoding='utf-8') as f:
            sql_content = f.read()
    except Exception as e:
        return f"-- ERROR: Cannot read {sql_file}: {e}\n\n"

    # Replace tablespace
    sql_content = replace_tablespace(sql_content)

    file_name = sql_file.name

    # Generate deployment block - direct execution with error handling
    block = f"""
-- ============================================================================
-- Deploy: {file_name}
-- Category: {category_name}
-- ============================================================================

PROMPT Deploying {file_name}...

WHENEVER SQLERROR CONTINUE

{sql_content}

PROMPT Completed {file_name}
PROMPT

"""

    return block


def generate_deployment_script(schema_name, source_path, deployment_order, output_file):
    """Generate complete deployment script"""

    print(f"Generating deployment script for {schema_name}...")

    script_header = f"""-- ============================================================================
-- Oracle Database Deployment Script V2
-- Schema: {schema_name}
-- Generated: 2025-12-10
-- Target: Oracle 23ai Free (Docker)
-- Connection: {schema_name}/kis_{schema_name.lower()}_2024@//localhost:1521/FREEPDB1
-- ============================================================================

SET SERVEROUTPUT ON SIZE UNLIMITED
SET VERIFY OFF
SET FEEDBACK ON
SET ECHO ON
SET DEFINE OFF

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
-- Post-Deployment: Summary of Objects
-- ============================================================================

PROMPT ============================================================================
PROMPT Object Summary
PROMPT ============================================================================

SELECT object_type, status, COUNT(*) as count
FROM user_objects
GROUP BY object_type, status
ORDER BY object_type, status;

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
-- Post-Deployment: Recompile Invalid Objects (3 passes)
-- ============================================================================

PROMPT ============================================================================
PROMPT Recompiling INVALID objects - Pass 1
PROMPT ============================================================================

BEGIN
    FOR obj IN (SELECT object_name, object_type
                FROM user_objects
                WHERE status = 'INVALID'
                ORDER BY DECODE(object_type,
                    'TYPE', 1,
                    'PACKAGE', 2,
                    'PACKAGE BODY', 3,
                    'FUNCTION', 4,
                    'PROCEDURE', 5,
                    'TRIGGER', 6,
                    'VIEW', 7,
                    99))
    LOOP
        BEGIN
            IF obj.object_type = 'PACKAGE BODY' THEN
                EXECUTE IMMEDIATE 'ALTER PACKAGE ' || obj.object_name || ' COMPILE BODY';
            ELSIF obj.object_type = 'PACKAGE' THEN
                EXECUTE IMMEDIATE 'ALTER PACKAGE ' || obj.object_name || ' COMPILE';
            ELSIF obj.object_type = 'TYPE BODY' THEN
                EXECUTE IMMEDIATE 'ALTER TYPE ' || obj.object_name || ' COMPILE BODY';
            ELSIF obj.object_type = 'TYPE' THEN
                EXECUTE IMMEDIATE 'ALTER TYPE ' || obj.object_name || ' COMPILE';
            ELSIF obj.object_type = 'VIEW' THEN
                EXECUTE IMMEDIATE 'ALTER VIEW ' || obj.object_name || ' COMPILE';
            ELSIF obj.object_type = 'TRIGGER' THEN
                EXECUTE IMMEDIATE 'ALTER TRIGGER ' || obj.object_name || ' COMPILE';
            ELSIF obj.object_type = 'FUNCTION' THEN
                EXECUTE IMMEDIATE 'ALTER FUNCTION ' || obj.object_name || ' COMPILE';
            ELSIF obj.object_type = 'PROCEDURE' THEN
                EXECUTE IMMEDIATE 'ALTER PROCEDURE ' || obj.object_name || ' COMPILE';
            END IF;
            DBMS_OUTPUT.PUT_LINE('PASS1 OK: ' || obj.object_type || ' ' || obj.object_name);
        EXCEPTION
            WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE('PASS1 FAIL: ' || obj.object_type || ' ' || obj.object_name);
        END;
    END LOOP;
END;
/

PROMPT ============================================================================
PROMPT Recompiling INVALID objects - Pass 2
PROMPT ============================================================================

BEGIN
    FOR obj IN (SELECT object_name, object_type
                FROM user_objects
                WHERE status = 'INVALID'
                ORDER BY DECODE(object_type,
                    'TYPE', 1,
                    'PACKAGE', 2,
                    'PACKAGE BODY', 3,
                    'FUNCTION', 4,
                    'PROCEDURE', 5,
                    'TRIGGER', 6,
                    'VIEW', 7,
                    99))
    LOOP
        BEGIN
            IF obj.object_type = 'PACKAGE BODY' THEN
                EXECUTE IMMEDIATE 'ALTER PACKAGE ' || obj.object_name || ' COMPILE BODY';
            ELSIF obj.object_type = 'PACKAGE' THEN
                EXECUTE IMMEDIATE 'ALTER PACKAGE ' || obj.object_name || ' COMPILE';
            ELSIF obj.object_type = 'TYPE BODY' THEN
                EXECUTE IMMEDIATE 'ALTER TYPE ' || obj.object_name || ' COMPILE BODY';
            ELSIF obj.object_type = 'TYPE' THEN
                EXECUTE IMMEDIATE 'ALTER TYPE ' || obj.object_name || ' COMPILE';
            ELSIF obj.object_type = 'VIEW' THEN
                EXECUTE IMMEDIATE 'ALTER VIEW ' || obj.object_name || ' COMPILE';
            ELSIF obj.object_type = 'TRIGGER' THEN
                EXECUTE IMMEDIATE 'ALTER TRIGGER ' || obj.object_name || ' COMPILE';
            ELSIF obj.object_type = 'FUNCTION' THEN
                EXECUTE IMMEDIATE 'ALTER FUNCTION ' || obj.object_name || ' COMPILE';
            ELSIF obj.object_type = 'PROCEDURE' THEN
                EXECUTE IMMEDIATE 'ALTER PROCEDURE ' || obj.object_name || ' COMPILE';
            END IF;
            DBMS_OUTPUT.PUT_LINE('PASS2 OK: ' || obj.object_type || ' ' || obj.object_name);
        EXCEPTION
            WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE('PASS2 FAIL: ' || obj.object_type || ' ' || obj.object_name);
        END;
    END LOOP;
END;
/

PROMPT ============================================================================
PROMPT Recompiling INVALID objects - Pass 3
PROMPT ============================================================================

BEGIN
    FOR obj IN (SELECT object_name, object_type
                FROM user_objects
                WHERE status = 'INVALID'
                ORDER BY DECODE(object_type,
                    'TYPE', 1,
                    'PACKAGE', 2,
                    'PACKAGE BODY', 3,
                    'FUNCTION', 4,
                    'PROCEDURE', 5,
                    'TRIGGER', 6,
                    'VIEW', 7,
                    99))
    LOOP
        BEGIN
            IF obj.object_type = 'PACKAGE BODY' THEN
                EXECUTE IMMEDIATE 'ALTER PACKAGE ' || obj.object_name || ' COMPILE BODY';
            ELSIF obj.object_type = 'PACKAGE' THEN
                EXECUTE IMMEDIATE 'ALTER PACKAGE ' || obj.object_name || ' COMPILE';
            ELSIF obj.object_type = 'TYPE BODY' THEN
                EXECUTE IMMEDIATE 'ALTER TYPE ' || obj.object_name || ' COMPILE BODY';
            ELSIF obj.object_type = 'TYPE' THEN
                EXECUTE IMMEDIATE 'ALTER TYPE ' || obj.object_name || ' COMPILE';
            ELSIF obj.object_type = 'VIEW' THEN
                EXECUTE IMMEDIATE 'ALTER VIEW ' || obj.object_name || ' COMPILE';
            ELSIF obj.object_type = 'TRIGGER' THEN
                EXECUTE IMMEDIATE 'ALTER TRIGGER ' || obj.object_name || ' COMPILE';
            ELSIF obj.object_type = 'FUNCTION' THEN
                EXECUTE IMMEDIATE 'ALTER FUNCTION ' || obj.object_name || ' COMPILE';
            ELSIF obj.object_type = 'PROCEDURE' THEN
                EXECUTE IMMEDIATE 'ALTER PROCEDURE ' || obj.object_name || ' COMPILE';
            END IF;
            DBMS_OUTPUT.PUT_LINE('PASS3 OK: ' || obj.object_type || ' ' || obj.object_name);
        EXCEPTION
            WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE('PASS3 FAIL: ' || obj.object_type || ' ' || obj.object_name);
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

PROMPT
PROMPT ============================================================================
PROMPT Remaining INVALID Objects
PROMPT ============================================================================

SELECT object_type, object_name
FROM user_objects
WHERE status = 'INVALID'
ORDER BY object_type, object_name;

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
    print("Oracle Database Deployment Script Generator V2")
    print("=" * 80)
    print()

    # Generate DB_JT deployment script
    db_jt_output = f"{OUTPUT_DIR}/deploy_db_jt_complete_v2.sql"
    db_jt_count = generate_deployment_script(
        "DB_JT",
        DB_JT_SOURCE,
        DB_JT_DEPLOYMENT_ORDER,
        db_jt_output
    )

    # Generate DB_DSA deployment script
    db_dsa_output = f"{OUTPUT_DIR}/deploy_db_dsa_complete_v2.sql"
    db_dsa_count = generate_deployment_script(
        "DB_DSA",
        DB_DSA_SOURCE,
        DB_DSA_DEPLOYMENT_ORDER,
        db_dsa_output
    )

    print("=" * 80)
    print("Deployment Scripts Generated Successfully (V2)")
    print("=" * 80)
    print(f"DB_JT:  {db_jt_count} SQL files -> {db_jt_output}")
    print(f"DB_DSA: {db_dsa_count} SQL files -> {db_dsa_output}")
    print()
    print("To deploy, run:")
    print(f"  docker exec -i kis-oracle sqlplus DB_JT/kis_db_jt_2024@//localhost:1521/FREEPDB1 < {db_jt_output}")
    print(f"  docker exec -i kis-oracle sqlplus DB_DSA/kis_db_dsa_2024@//localhost:1521/FREEPDB1 < {db_dsa_output}")
    print()


if __name__ == "__main__":
    main()
