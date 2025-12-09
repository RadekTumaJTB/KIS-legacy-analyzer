#!/bin/bash
# Script to create Oracle schema from PL/SQL source files

ORACLE_USER="DB_JT"
ORACLE_PASS="kis_db_jt_2024"
ORACLE_SERVICE="FREEPDB1"
SOURCE_DIR="/Users/radektuma/DEV/KIS/sources/DB/DB_JT"

echo "Creating schema for $ORACLE_USER in $ORACLE_SERVICE..."

# Main tables to create
MAIN_TABLES=(
    "KP_DAT_DOKUMENT"
    "KP_DAT_DOKUMENTRADEK"
    "KP_DAT_BUDGET"
    "KP_DAT_BUDGETPOLOZKA"
    "KP_CIS_DOKUMENT"
    "KP_CIS_DOKUMENTSTATUS"
    "KP_KTG_ODBOR"
    "KP_KTG_SPOLECNOST"
    "KP_KTG_UCETNISPOLECNOST"
    "KP_KTG_PROJEKT"
    "KP_CIS_TYPTRANSAKCE"
    "KP_CIS_MENA"
)

# Create tables
for table in "${MAIN_TABLES[@]}"; do
    TABLE_FILE="$SOURCE_DIR/TABLES/${table}.sql"
    if [ -f "$TABLE_FILE" ]; then
        echo "Creating table: $table"
        # Remove "DB_JT". prefix and execute
        docker exec kis-oracle bash -c "cat <<'EOSQL' | sqlplus -s $ORACLE_USER/$ORACLE_PASS@$ORACLE_SERVICE
SET ECHO ON
SET FEEDBACK ON
$(sed 's/"DB_JT"\.//' "$TABLE_FILE")
EXIT;
EOSQL"
    else
        echo "Warning: Table file not found: $TABLE_FILE"
    fi
done

echo "Schema creation completed!"
