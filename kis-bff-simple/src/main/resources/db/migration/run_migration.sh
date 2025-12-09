#!/bin/bash

# ============================================================================
# KIS Database Migration Script Runner
# ============================================================================
# This script executes the test data migration SQL script on Oracle database
# ============================================================================

# Configuration
DB_USER="DB_JT"
DB_PASSWORD="kis_db_jt_2024"
DB_CONNECT="localhost:1521/FREEPDB1"
SQL_FILE="insert_test_data.sql"

echo "============================================================================"
echo "KIS Project Module - Database Migration"
echo "============================================================================"
echo "Database: ${DB_CONNECT}"
echo "User: ${DB_USER}"
echo "SQL File: ${SQL_FILE}"
echo "============================================================================"
echo ""

# Check if SQL file exists
if [ ! -f "${SQL_FILE}" ]; then
    echo "ERROR: SQL file ${SQL_FILE} not found!"
    exit 1
fi

# Execute SQL script using SQLPlus
echo "Executing SQL script..."
echo ""

sqlplus -S ${DB_USER}/${DB_PASSWORD}@${DB_CONNECT} @${SQL_FILE}

if [ $? -eq 0 ]; then
    echo ""
    echo "============================================================================"
    echo "Migration completed successfully!"
    echo "============================================================================"
    exit 0
else
    echo ""
    echo "============================================================================"
    echo "ERROR: Migration failed!"
    echo "============================================================================"
    exit 1
fi
