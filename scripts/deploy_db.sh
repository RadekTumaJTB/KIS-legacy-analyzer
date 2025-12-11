#!/bin/bash

# ============================================================================
# Oracle Database Deployment Helper Script
# ============================================================================
#
# Tento script usnadňuje spuštění Python deployment scriptu
#
# Použití:
#   1. Editujte proměnné níže podle vašeho prostředí
#   2. Spusťte: ./scripts/deploy_db.sh
#
#   Nebo předejte proměnné:
#   DB_USER=db_jt DB_PASSWORD=pass ./scripts/deploy_db.sh
#
# ============================================================================

# Databázové připojení
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-1521}"
DB_SERVICE="${DB_SERVICE:-FREEPDB1}"
DB_USER="${DB_USER:-db_jt}"
DB_PASSWORD="${DB_PASSWORD:-}"

# Deployment nastavení
SCHEMA="${SCHEMA:-BOTH}"           # BOTH, DB_JT, nebo DB_DSA
SOURCE_DIR="${SOURCE_DIR:-$(pwd)/sources/DB}"
SKIP_DROP="${SKIP_DROP:-false}"    # true pro přeskočení mazání objektů
DRY_RUN="${DRY_RUN:-false}"        # true pro dry-run
MAX_RETRIES="${MAX_RETRIES:-3}"    # Počet retry pokusů

# Barvy pro výstup
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# ============================================================================
# Funkce
# ============================================================================

print_header() {
    echo -e "${BLUE}================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}================================${NC}"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

check_requirements() {
    print_header "Kontrola požadavků"

    # Kontrola Pythonu
    if ! command -v python3 &> /dev/null; then
        print_error "Python 3 není nainstalován"
        exit 1
    fi
    print_success "Python 3: $(python3 --version)"

    # Kontrola oracledb/cx_Oracle
    if ! python3 -c "import oracledb" 2>/dev/null && ! python3 -c "import cx_Oracle" 2>/dev/null; then
        print_error "oracledb ani cx_Oracle není nainstalován"
        echo "Instalace: pip install oracledb"
        exit 1
    fi
    print_success "Oracle klient knihovna je nainstalována"

    # Kontrola deployment scriptu
    if [ ! -f "scripts/deploy_database.py" ]; then
        print_error "Deployment script nenalezen: scripts/deploy_database.py"
        exit 1
    fi
    print_success "Deployment script nalezen"

    # Kontrola zdrojové složky
    if [ ! -d "$SOURCE_DIR" ]; then
        print_error "Zdrojová složka nenalezena: $SOURCE_DIR"
        exit 1
    fi
    print_success "Zdrojová složka: $SOURCE_DIR"

    echo ""
}

prompt_password() {
    if [ -z "$DB_PASSWORD" ]; then
        echo -n "Zadejte heslo pro uživatele $DB_USER: "
        read -s DB_PASSWORD
        echo ""
        echo ""
    fi
}

show_config() {
    print_header "Konfigurace"
    echo "Host:         $DB_HOST"
    echo "Port:         $DB_PORT"
    echo "Service:      $DB_SERVICE"
    echo "User:         $DB_USER"
    echo "Schema:       $SCHEMA"
    echo "Source Dir:   $SOURCE_DIR"
    echo "Skip Drop:    $SKIP_DROP"
    echo "Dry Run:      $DRY_RUN"
    echo "Max Retries:  $MAX_RETRIES"
    echo ""
}

confirm_deployment() {
    if [ "$DRY_RUN" = "true" ]; then
        print_warning "DRY RUN mód - žádné změny nebudou provedeny"
        return 0
    fi

    print_warning "VAROVÁNÍ: Tento script smaže všechny existující objekty v databázi!"
    echo -n "Opravdu chcete pokračovat? (ano/ne): "
    read -r response

    if [ "$response" != "ano" ]; then
        print_error "Deployment zrušen uživatelem"
        exit 0
    fi
    echo ""
}

run_deployment() {
    print_header "Spouštím deployment"

    # Sestavení parametrů
    CONNECTION_STRING="${DB_USER}/${DB_PASSWORD}@${DB_HOST}:${DB_PORT}/${DB_SERVICE}"

    PARAMS=(
        --connection "$CONNECTION_STRING"
        --schema "$SCHEMA"
        --source-dir "$SOURCE_DIR"
        --max-retries "$MAX_RETRIES"
    )

    if [ "$SKIP_DROP" = "true" ]; then
        PARAMS+=(--skip-drop)
    fi

    if [ "$DRY_RUN" = "true" ]; then
        PARAMS+=(--dry-run)
    fi

    # Spuštění Python scriptu
    python3 scripts/deploy_database.py "${PARAMS[@]}"

    RESULT=$?

    echo ""
    if [ $RESULT -eq 0 ]; then
        print_success "Deployment úspěšně dokončen!"

        # Zobrazení log souboru
        LATEST_LOG=$(ls -t deployment_*.log 2>/dev/null | head -1)
        if [ -n "$LATEST_LOG" ]; then
            echo ""
            echo "Log soubor: $LATEST_LOG"
            echo "Pro zobrazení: cat $LATEST_LOG"
        fi
    else
        print_error "Deployment selhal s chybou!"
        exit $RESULT
    fi
}

# ============================================================================
# Main
# ============================================================================

clear

print_header "Oracle Database Deployment"
echo ""

check_requirements
prompt_password
show_config
confirm_deployment
run_deployment

echo ""
print_header "Hotovo!"
