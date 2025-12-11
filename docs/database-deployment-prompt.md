# Oracle Database Deployment - Comprehensive Prompt

## Cíl
Nasadit všechny databázové objekty z `/Users/radektuma/DEV/KIS/sources/DB/` do Oracle 23ai Free Docker kontejneru.

## Target Schemas
- **DB_JT**: `/Users/radektuma/DEV/KIS/sources/DB/DB_JT/` → schema DB_JT
- **DB_DSA**: `/Users/radektuma/DEV/KIS/sources/DB/DB_DSA/` → schema DB_DSA

## Oracle Connection
```bash
# DB_JT schema
Connection: DB_JT/kis_db_jt_2024@//localhost:1521/FREEPDB1

# DB_DSA schema
Connection: DB_DSA/kis_db_dsa_2024@//localhost:1521/FREEPDB1
```

## KRITICKÉ POŽADAVKY

### 1. Posloupnost Nasazení Objektů (DEPENDENCY ORDER)

**Nasazovat objekty v tomto přesném pořadí:**

```
FÁZE 1: Základní objekty (bez závislostí)
├── 1.1. TYPES                    # Uživatelské datové typy (pokud existují)
├── 1.2. SEQUENCES                # Sekvence pro auto-increment ID
└── 1.3. TABLES (základní)        # Tabulky bez foreign keys

FÁZE 2: Vztahy mezi tabulkami
├── 2.1. CONSTRAINTS              # Primary keys, unique constraints, check constraints
└── 2.2. REF_CONSTRAINTS          # Foreign keys (AFTER all tables exist)

FÁZE 3: Indexy a materialized views
├── 3.1. INDEXES                  # Indexy pro rychlost (DB_DSA only)
└── 3.2. MATERIALIZED_VIEWS       # Materialized views (DB_JT only)

FÁZE 4: Logika a datové objekty
├── 4.1. VIEWS                    # Views (mohou záviset na tabulkách)
├── 4.2. FUNCTIONS                # Standalone functions
├── 4.3. PROCEDURES               # Standalone procedures
├── 4.4. PACKAGES (spec)          # Package specifications FIRST
└── 4.5. PACKAGE_BODIES           # Package bodies AFTER specs

FÁZE 5: Triggery a další
├── 5.1. TRIGGERS                 # Triggers (po všech tabulkách a packages)
├── 5.2. SYNONYMS                 # Synonyms (aliasy)
└── 5.3. DATABASE_LINKS           # Database links (pokud jsou potřeba)
```

### 2. Handling Missing Dependencies (Řešení Chybějících Závislostí)

**Pokud nasazení objektu SELŽE kvůli chybějící závislosti:**

```python
def deploy_object_with_dependency_resolution(object_file):
    attempt = 0
    max_attempts = 3
    dependency_stack = []

    while attempt < max_attempts:
        result = execute_sql(object_file)

        if result.success:
            return True

        if result.error_contains("ORA-00942"):  # Table or view does not exist
            missing_object = extract_missing_object_name(result.error)
            dependency_stack.append(missing_object)

            # Find and deploy missing object first
            missing_sql = find_sql_file_for_object(missing_object)
            if missing_sql:
                deploy_object_with_dependency_resolution(missing_sql)
                attempt += 1
                continue

        elif result.error_contains("ORA-04043"):  # Object does not exist
            missing_object = extract_missing_object_name(result.error)
            # Same as above

        elif result.error_contains("ORA-02289"):  # Sequence does not exist
            missing_sequence = extract_sequence_name(result.error)
            # Find and deploy sequence first

        elif result.error_contains("PLS-00201"):  # Identifier must be declared
            missing_identifier = extract_identifier(result.error)
            # Find and deploy package spec or type first

        else:
            # Log error and skip (other type of error)
            log_error(object_file, result.error)
            return False

    return False
```

### 3. TABLESPACE Mapping

**KRITICKÉ:** Oracle 23ai Free NEMÁ "DBAJT" a "DBADSA" tablespace!

```sql
-- Najít a nahradit v SQL scriptech:
TABLESPACE "DBAJT"    →  TABLESPACE USERS
TABLESPACE "DBADSA"   →  TABLESPACE USERS
TABLESPACE DBAJT      →  TABLESPACE USERS
TABLESPACE DBADSA     →  TABLESPACE USERS
```

### 4. Schema Prefix Handling

**Každý SQL script obsahuje schema prefix. Příklady:**

```sql
-- DB_JT scripts
CREATE TABLE "DB_JT"."KP_KTG_PROJEKT" (...)
CREATE SEQUENCE "DB_JT"."SQ_KP_PROJEKT"
CREATE PACKAGE "DB_JT"."KAP_PROJEKT"

-- DB_DSA scripts
CREATE TABLE "DB_DSA"."DSA_REPORTING_DATA" (...)
```

**Řešení:**
- Nechat schema prefix v SQL scriptech BEZ změn
- Spustit v kontextu správného schema (DB_JT nebo DB_DSA)

### 5. Error Handling Strategy

**Pro každý SQL file:**

```python
error_types = {
    "ORA-00955": "Name already used",        # Skip (already exists)
    "ORA-00942": "Table does not exist",     # Deploy dependency first
    "ORA-04043": "Object does not exist",    # Deploy dependency first
    "ORA-02289": "Sequence does not exist",  # Deploy sequence first
    "ORA-02298": "Cannot validate FK",       # Parent table missing or no PK
    "ORA-02270": "No matching unique/PK",    # Create PK on parent first
    "PLS-00201": "Identifier not declared",  # Deploy package spec first
    "ORA-24344": "Success with compilation error", # Note and compile later
}

def handle_error(sql_file, error_code, error_message):
    if error_code == "ORA-00955":
        log_info(f"{sql_file}: Object already exists, skipping")
        return SKIP

    elif error_code in ["ORA-00942", "ORA-04043", "ORA-02289"]:
        missing = extract_missing_object(error_message)
        return RETRY_AFTER_DEPENDENCY(missing)

    elif error_code in ["ORA-02298", "ORA-02270"]:
        parent_table = extract_parent_table(error_message)
        return RETRY_AFTER_PARENT(parent_table)

    elif error_code == "PLS-00201":
        missing_package = extract_package_name(error_message)
        return RETRY_AFTER_PACKAGE_SPEC(missing_package)

    elif error_code == "ORA-24344":
        log_warning(f"{sql_file}: Compiled with errors, will recompile later")
        return MARK_FOR_RECOMPILE

    else:
        log_error(f"{sql_file}: {error_code} - {error_message}")
        return FAIL
```

## DEPLOYMENT PLAN - DB_JT Schema

### Step 1: Deploy TYPES (if any)
```bash
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_JT/TYPES/*.sql; do
    deploy_with_retry "$file" "DB_JT"
done
```

### Step 2: Deploy SEQUENCES (116 files)
```bash
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_JT/SEQUENCES/*.sql; do
    deploy_with_retry "$file" "DB_JT"
done
```

### Step 3: Deploy TABLES (711 files)
```bash
# First pass: Try to deploy all tables
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_JT/TABLES/*.sql; do
    deploy_with_retry "$file" "DB_JT"
done

# Second pass: Retry failed tables (dependencies might exist now)
retry_failed_tables "DB_JT"
```

### Step 4: Deploy CONSTRAINTS (503 files)
```bash
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_JT/CONSTRAINTS/*.sql; do
    deploy_with_retry "$file" "DB_JT"
done
```

### Step 5: Deploy REF_CONSTRAINTS (215 files)
```bash
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_JT/REF_CONSTRAINTS/*.sql; do
    deploy_with_retry "$file" "DB_JT"
done
```

### Step 6: Deploy MATERIALIZED_VIEWS (9 files)
```bash
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_JT/MATERIALIZED_VIEWS/*.sql; do
    deploy_with_retry "$file" "DB_JT"
done
```

### Step 7: Deploy VIEWS (396 files)
```bash
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_JT/VIEWS/*.sql; do
    deploy_with_retry "$file" "DB_JT"
done
```

### Step 8: Deploy FUNCTIONS (92 files)
```bash
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_JT/FUNCTIONS/*.sql; do
    deploy_with_retry "$file" "DB_JT"
done
```

### Step 9: Deploy PROCEDURES (32 files)
```bash
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_JT/PROCEDURES/*.sql; do
    deploy_with_retry "$file" "DB_JT"
done
```

### Step 10: Deploy PACKAGE SPECIFICATIONS (38 files)
```bash
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_JT/PACKAGES/*.sql; do
    deploy_with_retry "$file" "DB_JT"
done
```

### Step 11: Deploy PACKAGE BODIES (38 files)
```bash
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_JT/PACKAGE_BODIES/*.sql; do
    deploy_with_retry "$file" "DB_JT"
done
```

### Step 12: Deploy TRIGGERS (294 files)
```bash
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_JT/TRIGGERS/*.sql; do
    deploy_with_retry "$file" "DB_JT"
done
```

### Step 13: Deploy SYNONYMS (3 files)
```bash
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_JT/SYNONYMS/*.sql; do
    deploy_with_retry "$file" "DB_JT"
done
```

### Step 14: Deploy DATABASE_LINKS (27 files - if needed)
```bash
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_JT/DATABASE_LINKS/*.sql; do
    deploy_with_retry "$file" "DB_JT"
done
```

## DEPLOYMENT PLAN - DB_DSA Schema

### Step 1: Deploy SEQUENCES (12 files)
```bash
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/SEQUENCES/*.sql; do
    deploy_with_retry "$file" "DB_DSA"
done
```

### Step 2: Deploy TABLES (45 files)
```bash
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/TABLES/*.sql; do
    deploy_with_retry "$file" "DB_DSA"
done
```

### Step 3: Deploy CONSTRAINTS (36 files)
```bash
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/CONSTRAINTS/*.sql; do
    deploy_with_retry "$file" "DB_DSA"
done
```

### Step 4: Deploy REF_CONSTRAINTS (3 files)
```bash
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/REF_CONSTRAINTS/*.sql; do
    deploy_with_retry "$file" "DB_DSA"
done
```

### Step 5: Deploy INDEXES (54 files)
```bash
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/INDEXES/*.sql; do
    deploy_with_retry "$file" "DB_DSA"
done
```

### Step 6: Deploy VIEWS (3 files)
```bash
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/VIEWS/*.sql; do
    deploy_with_retry "$file" "DB_DSA"
done
```

### Step 7: Deploy FUNCTIONS (8 files)
```bash
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/FUNCTIONS/*.sql; do
    deploy_with_retry "$file" "DB_DSA"
done
```

### Step 8: Deploy PROCEDURES (13 files)
```bash
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/PROCEDURES/*.sql; do
    deploy_with_retry "$file" "DB_DSA"
done
```

### Step 9: Deploy PACKAGE SPECIFICATIONS (15 files)
```bash
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/PACKAGES/*.sql; do
    deploy_with_retry "$file" "DB_DSA"
done
```

### Step 10: Deploy PACKAGE BODIES (15 files)
```bash
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/PACKAGE_BODIES/*.sql; do
    deploy_with_retry "$file" "DB_DSA"
done
```

### Step 11: Deploy TRIGGERS (16 files)
```bash
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/TRIGGERS/*.sql; do
    deploy_with_retry "$file" "DB_DSA"
done
```

### Step 12: Deploy DATABASE_LINKS (23 files - if needed)
```bash
for file in /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/DATABASE_LINKS/*.sql; do
    deploy_with_retry "$file" "DB_DSA"
done
```

## POST-DEPLOYMENT VERIFICATION

### 1. Check INVALID Objects
```sql
-- DB_JT schema
SELECT object_name, object_type, status
FROM user_objects
WHERE status = 'INVALID'
ORDER BY object_type, object_name;
```

### 2. Recompile INVALID Objects
```sql
-- Recompile all invalid objects
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

            DBMS_OUTPUT.PUT_LINE('✓ ' || obj.object_type || ' ' || obj.object_name || ' recompiled');
        EXCEPTION
            WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE('✗ ' || obj.object_type || ' ' || obj.object_name || ': ' || SQLERRM);
        END;
    END LOOP;
END;
/
```

### 3. Generate Deployment Report

**Report musí obsahovat:**

```markdown
# Oracle Database Deployment Report
Date: 2025-12-10

## DB_JT Schema Deployment

### Objects Deployed Successfully
- SEQUENCES: 116/116 (100%)
- TABLES: 711/711 (100%)
- CONSTRAINTS: 503/503 (100%)
- REF_CONSTRAINTS: 215/215 (100%)
- VIEWS: 396/396 (100%)
- FUNCTIONS: 92/92 (100%)
- PROCEDURES: 32/32 (100%)
- PACKAGES: 38/38 (100%)
- PACKAGE_BODIES: 38/38 (100%)
- TRIGGERS: 294/294 (100%)
- SYNONYMS: 3/3 (100%)
- MATERIALIZED_VIEWS: 9/9 (100%)

### Objects with Errors
- [List any failed objects with error messages]

### INVALID Objects After Deployment
- [List INVALID objects]

### Critical Packages Status
- KAP_PROJEKT: VALID ✅
- KAP_DOKUMENT: VALID ✅
- KAP_BUDGET: VALID ✅

## DB_DSA Schema Deployment
[Same structure as above]

## Recommendations
[Any fixes needed]
```

## EXPECTED OUTPUT

Vytvořit následující soubory:

1. **`/Users/radektuma/DEV/KIS/kis-bff-simple/src/main/resources/db/migration/complete/deploy_db_jt_complete.sql`**
   - Kompletní deployment script pro DB_JT schema
   - Obsahuje všechny objekty ve správném pořadí
   - Obsahuje error handling
   - Obsahuje TABLESPACE replacement

2. **`/Users/radektuma/DEV/KIS/kis-bff-simple/src/main/resources/db/migration/complete/deploy_db_dsa_complete.sql`**
   - Kompletní deployment script pro DB_DSA schema
   - Stejná struktura jako DB_JT

3. **`/Users/radektuma/DEV/KIS/docs/database-deployment-report.md`**
   - Detailní report z nasazení
   - Seznam úspěšných objektů
   - Seznam chybných objektů
   - Status všech INVALID objektů
   - Doporučení pro opravu

## EXECUTION

```bash
# Deploy DB_JT schema
sqlplus DB_JT/kis_db_jt_2024@//localhost:1521/FREEPDB1 @deploy_db_jt_complete.sql

# Deploy DB_DSA schema
sqlplus DB_DSA/kis_db_dsa_2024@//localhost:1521/FREEPDB1 @deploy_db_dsa_complete.sql

# Verify deployment
sqlplus DB_JT/kis_db_jt_2024@//localhost:1521/FREEPDB1 <<EOF
SELECT COUNT(*), object_type, status
FROM user_objects
GROUP BY object_type, status
ORDER BY object_type, status;
EOF
```

## NOTES

- **Total Objects DB_JT**: ~2,500+ SQL files
- **Total Objects DB_DSA**: ~200+ SQL files
- **Estimated Time**: 10-20 minut (závisí na rychlosti Oracle)
- **Critical Success**: KAP_PROJEKT package body musí být VALID po nasazení
