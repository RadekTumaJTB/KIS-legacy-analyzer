#!/bin/bash

# ==============================================================================
# Oracle Database Individual File Deployment Script
# ==============================================================================
# This script deploys SQL files individually to avoid parsing issues with
# SQLPlus stdin redirection. Each file is executed separately with its own
# SQLPlus session, providing better error isolation and tracking.
# ==============================================================================

set -e

# Colors for output
RED='\033[0:31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Arguments
SCHEMA=$1
SOURCE_DIR=$2

if [ -z "$SCHEMA" ] || [ -z "$SOURCE_DIR" ]; then
    echo -e "${RED}Usage: $0 <SCHEMA> <SOURCE_DIR>${NC}"
    echo "Example: $0 DB_JT /Users/radektuma/DEV/KIS/sources/DB/DB_JT"
    exit 1
fi

# Determine password
if [ "$SCHEMA" == "DB_JT" ]; then
    PASSWORD="kis_db_jt_2024"
elif [ "$SCHEMA" == "DB_DSA" ]; then
    PASSWORD="kis_db_dsa_2024"
else
    echo -e "${RED}Invalid schema: $SCHEMA${NC}"
    exit 1
fi

CONNECTION_STRING="${SCHEMA}/${PASSWORD}@//localhost:1521/FREEPDB1"

# Deployment order
CATEGORIES=(
    "TYPES"
    "SEQUENCES"
    "TABLES"
    "CONSTRAINTS"
    "REF_CONSTRAINTS"
    "MATERIALIZED_VIEWS"
    "INDEXES"
    "VIEWS"
    "FUNCTIONS"
    "PROCEDURES"
    "PACKAGES"
    "PACKAGE_BODIES"
    "TRIGGERS"
    "SYNONYMS"
)

# Statistics
TOTAL_FILES=0
SUCCESS_COUNT=0
FAILED_COUNT=0
SKIPPED_COUNT=0

# Log file
LOG_DIR="/Users/radektuma/DEV/KIS/logs"
mkdir -p "$LOG_DIR"
LOG_FILE="$LOG_DIR/deploy_${SCHEMA}_individual_$(date +%Y%m%d_%H%M%S).log"
ERROR_LOG="$LOG_DIR/deploy_${SCHEMA}_errors_$(date +%Y%m%d_%H%M%S).log"

echo -e "${BLUE}===============================================================================${NC}"
echo -e "${BLUE}Oracle Database Individual File Deployment${NC}"
echo -e "${BLUE}===============================================================================${NC}"
echo -e "Schema:      ${GREEN}$SCHEMA${NC}"
echo -e "Source Dir:  ${GREEN}$SOURCE_DIR${NC}"
echo -e "Log File:    ${GREEN}$LOG_FILE${NC}"
echo -e "Error Log:   ${GREEN}$ERROR_LOG${NC}"
echo -e "${BLUE}===============================================================================${NC}"
echo ""

# Create temporary SQL wrapper with tablespace replacement
create_temp_sql() {
    local INPUT_FILE=$1
    local TEMP_FILE=$(mktemp)

    # Replace tablespaces and write to temp file
    sed -e 's/TABLESPACE[[:space:]]*"DBAJT"/TABLESPACE USERS/gi' \
        -e 's/TABLESPACE[[:space:]]*"DBADSA"/TABLESPACE USERS/gi' \
        -e 's/TABLESPACE[[:space:]]*DBAJT/TABLESPACE USERS/gi' \
        -e 's/TABLESPACE[[:space:]]*DBADSA/TABLESPACE USERS/gi' \
        "$INPUT_FILE" > "$TEMP_FILE"

    echo "$TEMP_FILE"
}

# Deploy single SQL file
deploy_file() {
    local SQL_FILE=$1
    local FILE_NAME=$(basename "$SQL_FILE")
    local CATEGORY=$2

    ((TOTAL_FILES++))

    # Create temp SQL with tablespace replacements
    TEMP_SQL=$(create_temp_sql "$SQL_FILE")

    # Execute SQL file by piping content
    RESULT=$(docker exec -i kis-oracle sqlplus -S "$CONNECTION_STRING" < "$TEMP_SQL" 2>&1)

    # Clean up temp file
    rm -f "$TEMP_SQL"

    # Check result
    if echo "$RESULT" | grep -qE "(ORA-[0-9]{5}|SP2-[0-9]{4}|PLS-[0-9]{5})"; then
        # Check if it's just "already exists" error
        if echo "$RESULT" | grep -q "ORA-00955"; then
            echo -e "  ${YELLOW}SKIP${NC}: $FILE_NAME (already exists)"
            ((SKIPPED_COUNT++))
        else
            echo -e "  ${RED}FAIL${NC}: $FILE_NAME"
            ((FAILED_COUNT++))
            echo "==== $CATEGORY / $FILE_NAME ====" >> "$ERROR_LOG"
            echo "$RESULT" >> "$ERROR_LOG"
            echo "" >> "$ERROR_LOG"
        fi
    else
        echo -e "  ${GREEN}OK${NC}:   $FILE_NAME"
        ((SUCCESS_COUNT++))
    fi

    # Log everything
    echo "[$CATEGORY] $FILE_NAME" >> "$LOG_FILE"
    echo "$RESULT" >> "$LOG_FILE"
    echo "---" >> "$LOG_FILE"
}

# Main deployment loop
for CATEGORY in "${CATEGORIES[@]}"; do
    CATEGORY_DIR="$SOURCE_DIR/$CATEGORY"

    if [ ! -d "$CATEGORY_DIR" ]; then
        echo -e "${YELLOW}Skipping $CATEGORY (directory not found)${NC}"
        continue
    fi

    FILE_COUNT=$(ls -1 "$CATEGORY_DIR"/*.sql 2>/dev/null | wc -l)

    if [ "$FILE_COUNT" -eq 0 ]; then
        echo -e "${YELLOW}Skipping $CATEGORY (no SQL files)${NC}"
        continue
    fi

    echo -e "${BLUE}===============================================================================${NC}"
    echo -e "${BLUE}Deploying $CATEGORY ($FILE_COUNT files)${NC}"
    echo -e "${BLUE}===============================================================================${NC}"

    for SQL_FILE in "$CATEGORY_DIR"/*.sql; do
        deploy_file "$SQL_FILE" "$CATEGORY"
    done

    echo ""
done

# Final statistics
echo -e "${BLUE}===============================================================================${NC}"
echo -e "${BLUE}Deployment Summary${NC}"
echo -e "${BLUE}===============================================================================${NC}"
echo -e "Total Files:   ${BLUE}$TOTAL_FILES${NC}"
echo -e "Success:       ${GREEN}$SUCCESS_COUNT${NC}"
echo -e "Failed:        ${RED}$FAILED_COUNT${NC}"
echo -e "Skipped:       ${YELLOW}$SKIPPED_COUNT${NC}"
echo -e "${BLUE}===============================================================================${NC}"

if [ $FAILED_COUNT -gt 0 ]; then
    echo -e "${YELLOW}Warning: $FAILED_COUNT files failed to deploy. Check error log:${NC}"
    echo -e "${YELLOW}$ERROR_LOG${NC}"
fi

# Recompile invalid objects
echo ""
echo -e "${BLUE}===============================================================================${NC}"
echo -e "${BLUE}Recompiling INVALID Objects (3 passes)${NC}"
echo -e "${BLUE}===============================================================================${NC}"

for PASS in 1 2 3; do
    echo -e "${YELLOW}Pass $PASS...${NC}"
    docker exec -i kis-oracle sqlplus -S "$CONNECTION_STRING" <<'EOSQL' 2>&1 | grep -E "(PASS|rows selected)"
SET SERVEROUTPUT ON SIZE UNLIMITED
BEGIN
    FOR obj IN (SELECT object_name, object_type
                FROM user_objects
                WHERE status = 'INVALID'
                ORDER BY DECODE(object_type, 'TYPE', 1, 'PACKAGE', 2, 'PACKAGE BODY', 3, 'FUNCTION', 4, 'PROCEDURE', 5, 'TRIGGER', 6, 'VIEW', 7, 99))
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
        EXCEPTION
            WHEN OTHERS THEN
                NULL;
        END;
    END LOOP;
END;
/
EXIT;
EOSQL
done

# Final object count
echo ""
echo -e "${BLUE}===============================================================================${NC}"
echo -e "${BLUE}Final Object Status${NC}"
echo -e "${BLUE}===============================================================================${NC}"

docker exec -i kis-oracle sqlplus -S "$CONNECTION_STRING" <<'EOSQL'
SET PAGESIZE 100
SET LINESIZE 200
COLUMN object_type FORMAT A25
COLUMN status FORMAT A10
COLUMN count FORMAT 999,999

SELECT object_type, status, COUNT(*) as count
FROM user_objects
GROUP BY object_type, status
ORDER BY object_type, status;

EXIT;
EOSQL

# Check critical packages
echo ""
echo -e "${BLUE}===============================================================================${NC}"
echo -e "${BLUE}Critical Packages Status${NC}"
echo -e "${BLUE}===============================================================================${NC}"

docker exec -i kis-oracle sqlplus -S "$CONNECTION_STRING" <<'EOSQL'
SET PAGESIZE 50
SET LINESIZE 200
COLUMN object_name FORMAT A30
COLUMN object_type FORMAT A20
COLUMN status FORMAT A10

SELECT object_name, object_type, status
FROM user_objects
WHERE object_name IN ('KAP_PROJEKT', 'KAP_DOKUMENT', 'KAP_BUDGET', 'KAP_DOKLAD', 'KAP_NASTROJE')
ORDER BY object_type, object_name;

EXIT;
EOSQL

echo ""
echo -e "${GREEN}Deployment completed!${NC}"
echo -e "Log file: ${GREEN}$LOG_FILE${NC}"

if [ $FAILED_COUNT -gt 0 ]; then
    exit 1
else
    exit 0
fi
