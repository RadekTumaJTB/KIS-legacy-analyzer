#!/bin/bash

# ============================================================================
# Skript pro vytvoření tabulek a vložení testovacích dat pro modul Assets
# Oracle 23ai Free Release v Dockeru
# ============================================================================

echo "============================================================================"
echo "KIS Banking App - Assets Module Database Migration"
echo "============================================================================"
echo ""

# Nastavení proměnných
ORACLE_HOST="localhost"
ORACLE_PORT="1521"
ORACLE_SID="FREEPDB1"
ORACLE_USER="DB_JT"
ORACLE_PASSWORD="heslo"  # ZMĚŇTE pokud máte jiné heslo

SQL_DIR="sql"
CREATE_TABLES_SQL="$SQL_DIR/create_assets_tables.sql"
INSERT_DATA_SQL="$SQL_DIR/insert_assets_test_data.sql"

echo "Připojení k Oracle databázi:"
echo "  Host: $ORACLE_HOST:$ORACLE_PORT"
echo "  SID: $ORACLE_SID"
echo "  User: $ORACLE_USER"
echo ""

# Kontrola, zda existují SQL soubory
if [ ! -f "$CREATE_TABLES_SQL" ]; then
    echo "❌ Chyba: Soubor $CREATE_TABLES_SQL nenalezen!"
    exit 1
fi

if [ ! -f "$INSERT_DATA_SQL" ]; then
    echo "❌ Chyba: Soubor $INSERT_DATA_SQL nenalezen!"
    exit 1
fi

echo "============================================================================"
echo "Krok 1: Vytvoření tabulek"
echo "============================================================================"
echo ""

# Spuštění create_assets_tables.sql
sqlplus -S $ORACLE_USER/$ORACLE_PASSWORD@//$ORACLE_HOST:$ORACLE_PORT/$ORACLE_SID @$CREATE_TABLES_SQL

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Tabulky úspěšně vytvořeny"
else
    echo ""
    echo "❌ Chyba při vytváření tabulek"
    exit 1
fi

echo ""
echo "============================================================================"
echo "Krok 2: Vložení testovacích dat"
echo "============================================================================"
echo ""

# Spuštění insert_assets_test_data.sql
sqlplus -S $ORACLE_USER/$ORACLE_PASSWORD@//$ORACLE_HOST:$ORACLE_PORT/$ORACLE_SID @$INSERT_DATA_SQL

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Testovací data úspěšně vložena"
else
    echo ""
    echo "❌ Chyba při vkládání testovacích dat"
    exit 1
fi

echo ""
echo "============================================================================"
echo "Ověření vytvořených tabulek"
echo "============================================================================"
echo ""

# Ověření počtu záznamů v tabulkách
sqlplus -S $ORACLE_USER/$ORACLE_PASSWORD@//$ORACLE_HOST:$ORACLE_PORT/$ORACLE_SID <<EOF
SET PAGESIZE 100
SET LINESIZE 150
COLUMN table_name FORMAT A35
COLUMN num_rows FORMAT 999999

SELECT
    table_name,
    num_rows
FROM user_tables
WHERE table_name IN (
    'KP_KTG_FINANCNIINVESTICE',
    'KP_KTG_FININVESTICEEMISE',
    'KP_DAT_MAJETKOVAUCAST',
    'KP_CIS_MAJETKOVAUCASTTYPTRAN',
    'KP_CIS_MAJETKOVAUCASTZPUSOB'
)
ORDER BY table_name;

-- Aktualizovat statistiky
EXEC DBMS_STATS.GATHER_SCHEMA_STATS('DB_JT');

-- Znovu zobrazit počty
SELECT
    table_name,
    num_rows
FROM user_tables
WHERE table_name IN (
    'KP_KTG_FINANCNIINVESTICE',
    'KP_KTG_FININVESTICEEMISE',
    'KP_DAT_MAJETKOVAUCAST',
    'KP_CIS_MAJETKOVAUCASTTYPTRAN',
    'KP_CIS_MAJETKOVAUCASTZPUSOB'
)
ORDER BY table_name;

EXIT;
EOF

echo ""
echo "============================================================================"
echo "✅ MIGRACE ÚSPĚŠNĚ DOKONČENA"
echo "============================================================================"
echo ""
echo "Vytvořené tabulky:"
echo "  ✓ KP_CIS_MAJETKOVAUCASTTYPTRAN (5 záznamů)"
echo "  ✓ KP_CIS_MAJETKOVAUCASTZPUSOB (3 záznamy)"
echo "  ✓ KP_KTG_FINANCNIINVESTICE (5 záznamů)"
echo "  ✓ KP_KTG_FININVESTICEEMISE (5 záznamů)"
echo "  ✓ KP_DAT_MAJETKOVAUCAST (8 záznamů - 7 aktivních, 1 historický)"
echo ""
echo "Další kroky:"
echo "  1. Restartujte Spring Boot backend: mvn spring-boot:run"
echo "  2. Otestujte endpointy:"
echo "     curl http://localhost:8081/bff/emissions"
echo "     curl http://localhost:8081/bff/assets/companies"
echo "  3. Spusťte E2E testy:"
echo "     cd kis-frontend && npx playwright test e2e/assets-*.spec.ts"
echo ""
echo "============================================================================"
