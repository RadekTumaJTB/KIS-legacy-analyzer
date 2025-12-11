# Database Deployment Script

Automatický deployment Oracle databáze z SQL skriptů s inteligentním řešením závislostí.

## Funkce

✅ **Automatické řazení objektů** - Správné pořadí nasazení (tabulky → view → sekvence → procedury → packages)
✅ **Řešení závislostí** - Automatický retry při chybějících závislostech
✅ **Smazání existujících objektů** - Před nasazením smaže všechny objekty
✅ **Detailní logování** - Kompletní log všech operací
✅ **Podpora obou schémat** - DB_JT i DB_DSA
✅ **Dry-run mód** - Ukáže co by se nasadilo bez skutečného provedení
✅ **Retry mechanismus** - Opakované pokusy při selhání kvůli závislostem

## Požadavky

```bash
# Nainstalovat Oracle klient knihovnu
pip install oracledb

# Nebo starší cx_Oracle (obě jsou podporovány)
pip install cx_Oracle
```

## Použití

### Základní použití

```bash
# S connection stringem (nejjednodušší)
python scripts/deploy_database.py \
  --connection "db_jt/password@localhost:1521/FREEPDB1"

# S jednotlivými parametry
python scripts/deploy_database.py \
  --host localhost \
  --port 1521 \
  --service FREEPDB1 \
  --user db_jt \
  --password your_password
```

### Pokročilé použití

```bash
# Deploy pouze DB_JT schématu
python scripts/deploy_database.py \
  --connection "db_jt/password@localhost:1521/FREEPDB1" \
  --schema DB_JT

# Dry-run (ukáže co by se nasadilo)
python scripts/deploy_database.py \
  --connection "db_jt/password@localhost:1521/FREEPDB1" \
  --dry-run

# Bez mazání existujících objektů
python scripts/deploy_database.py \
  --connection "db_jt/password@localhost:1521/FREEPDB1" \
  --skip-drop

# Vlastní log soubor
python scripts/deploy_database.py \
  --connection "db_jt/password@localhost:1521/FREEPDB1" \
  --log-file my_deployment.log

# Zvýšit počet retry pokusů
python scripts/deploy_database.py \
  --connection "db_jt/password@localhost:1521/FREEPDB1" \
  --max-retries 5

# Vlastní zdrojová složka
python scripts/deploy_database.py \
  --connection "db_jt/password@localhost:1521/FREEPDB1" \
  --source-dir /path/to/sql/scripts
```

### Pomocný bash script

Použijte `deploy_db.sh` pro rychlé spuštění:

```bash
# Editujte proměnné v souboru
./scripts/deploy_db.sh

# Nebo předejte parametry
DB_USER=db_jt DB_PASSWORD=pass ./scripts/deploy_db.sh
```

## Pořadí nasazení

Script dodržuje následující pořadí nasazení:

1. **TYPES** - Vlastní datové typy
2. **SEQUENCES** - Sekvence pro primární klíče
3. **TABLES** - Tabulky (nejprve bez constraintů)
4. **VIEWS** - Pohledy
5. **MATERIALIZED_VIEWS** - Materializované pohledy
6. **FUNCTIONS** - Funkce
7. **PROCEDURES** - Procedury
8. **PACKAGES** - Package specifikace
9. **PACKAGE_BODIES** - Package bodies
10. **TRIGGERS** - Triggery
11. **SYNONYMS** - Synonyma
12. **DATABASE_LINKS** - Databázové linky
13. **CONSTRAINTS** - Constrainty
14. **REF_CONSTRAINTS** - Referenční integrita
15. **INDEXES** - Indexy

## Řešení závislostí

Když objekt selže kvůli chybějící závislosti:

1. Script detekuje dependency error (např. "table does not exist")
2. Objekt je přidán do retry fronty
3. Po nasazení všech objektů daného typu se zkouší retry
4. Retry se opakuje až 3× (konfigurovatelné pomocí `--max-retries`)
5. Pokud se nepodaří ani po retries, objekt je označen jako failed

## Smazání objektů

Před nasazením script automaticky smaže všechny existující objekty v opačném pořadí:

```
INDEXES → REF_CONSTRAINTS → CONSTRAINTS → ... → SEQUENCES → TYPES
```

Používá `CASCADE CONSTRAINTS` a `PURGE` pro kompletní odstranění.

## Logování

Script vytváří detailní log obsahující:

- Datum a čas každé operace
- Seznam všech nasazovaných souborů
- Úspěšně nasazené objekty
- Chyby s detailními chybovými hláškami
- Finální souhrn (úspěšné/neúspěšné objekty)

Formát log souboru: `deployment_YYYYMMDD_HHMMSS.log`

## Příklad výstupu

```
================================================================================
ORACLE DATABASE DEPLOYMENT
================================================================================
Start time: 2025-12-10 15:30:00
Source directory: /Users/radektuma/DEV/KIS/sources/DB
Schema: BOTH
Log file: deployment_20251210_153000.log

Scanning for SQL files in /Users/radektuma/DEV/KIS/sources/DB
  Found 710 TABLES files in DB_JT
  Found 116 SEQUENCES files in DB_JT
  Found 396 VIEWS files in DB_JT
  Found 38 PACKAGES files in DB_JT
  Found 38 PACKAGE_BODIES files in DB_JT
  ...
Total SQL files found: 2547

================================================================================
DROPPING EXISTING OBJECTS
================================================================================

Dropping objects in schema: DB_JT
  Dropping MATERIALIZED VIEWs...
  Dropping PACKAGEs...
  Dropping PROCEDUREs...
  ...

================================================================================
DEPLOYING DATABASE OBJECTS
================================================================================

Deploying TYPES...
  ✓ Success: 22, ✗ Failed: 0

Deploying SEQUENCES...
  ✓ Success: 116, ✗ Failed: 0

Deploying TABLES...
  ✓ Success: 710, ✗ Failed: 5
  Retrying 5 failed objects...
    ✓ Retry successful: KP_DOCUMENT_HISTORY.sql

Deploying VIEWS...
  ✓ Success: 396, ✗ Failed: 3
  ...

================================================================================
DEPLOYMENT SUMMARY
================================================================================
✓ Successfully deployed: 2540 objects
✗ Failed: 7 objects

Failed objects:
  ✗ COMPLEX_VIEW_WITH_MISSING_TABLE.sql
  ✗ TRIGGER_ON_NON_EXISTENT_TABLE.sql
  ...
```

## Řešení problémů

### Chyba: "Neither oracledb nor cx_Oracle is installed"

```bash
pip install oracledb
```

### Chyba: "Connection refused"

- Zkontrolujte, zda běží Oracle databáze
- Ověřte host, port a service name
- Zkontrolujte firewall

### Příliš mnoho failed objektů

1. Zkontrolujte log soubor pro detailní chyby
2. Zkuste zvýšit `--max-retries`
3. Některé objekty mohou vyžadovat manuální opravu SQL skriptů

### Chyba encodingu

Script používá `encoding='utf-8', errors='ignore'` pro čtení souborů.
Pokud máte problémy s kódováním, zkontrolujte kódování zdrojových SQL souborů.

## Struktura složek

Očekávaná struktura zdrojových souborů:

```
sources/DB/
├── DB_JT/
│   ├── TABLES/
│   │   ├── TABLE1.sql
│   │   └── TABLE2.sql
│   ├── SEQUENCES/
│   ├── VIEWS/
│   ├── PROCEDURES/
│   ├── PACKAGES/
│   ├── PACKAGE_BODIES/
│   └── ...
└── DB_DSA/
    ├── TABLES/
    ├── PROCEDURES/
    └── ...
```

## Bezpečnost

⚠️ **VAROVÁNÍ**: Script maže všechny existující objekty!

- Vždy použijte `--dry-run` před prvním nasazením
- Záloha databáze před nasazením
- Nepoužívejte na produkční databázi bez důkladného testování
- Hesla v příkazové řádce jsou viditelná v historii - zvažte použití environmentálních proměnných

## Nápověda

```bash
python scripts/deploy_database.py --help
```
