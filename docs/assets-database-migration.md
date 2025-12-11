# Assets Module - Databázová Migrace

## Přehled

Tento dokument popisuje databázovou migraci pro modul **Majetkové Účasti (Assets Module)** v KIS Banking App.

**Datum:** 2025-12-10
**Databáze:** Oracle 23ai Free Release (Docker)
**Schema:** DB_JT

## Chybějící Tabulky

Backend očekává následující 5 tabulek, které zatím v databázi neexistují:

### 1. Číselníky (2 tabulky)

| Tabulka | Účel | Počet záznamů |
|---------|------|---------------|
| `KP_CIS_MAJETKOVAUCASTTYPTRAN` | Typy transakcí (Nákup, Prodej, Transfer, ...) | 5 |
| `KP_CIS_MAJETKOVAUCASTZPUSOB` | Způsoby účasti (Přímá, Nepřímá, Smíšená) | 3 |

### 2. Datové Tabulky (3 tabulky)

| Tabulka | Účel | Počet záznamů |
|---------|------|---------------|
| `KP_KTG_FINANCNIINVESTICE` | Finanční investice (cenné papíry) | 5 |
| `KP_KTG_FININVESTICEEMISE` | Emise cenných papírů s historií | 5 |
| `KP_DAT_MAJETKOVAUCAST` | Majetkové účasti (vlastnictví akcií) | 8 (7 aktivních) |

## Datový Model

### Vztahy mezi tabulkami

```
KP_DAT_SPOLECNOST (existuje)
        ↓
KP_KTG_FINANCNIINVESTICE ← ID_KTGSPOLECNOST
        ↓
KP_KTG_FININVESTICEEMISE ← ID_KTGFINANCNIINVESTICE
        ↓
KP_DAT_MAJETKOVAUCAST ← ID_KTGFININVESTICEEMISE
        ↓
        ├── FK → KP_DAT_SPOLECNOST (ID_KTGUCETNISPOLECNOST)
        ├── FK → KP_CIS_MAJETKOVAUCASTTYPTRAN (ID_CISMUTYPTRANSAKCE)
        └── FK → KP_CIS_MAJETKOVAUCASTZPUSOB (ID_CISMUZPUSOB)
```

## Struktura Tabulek

### KP_KTG_FINANCNIINVESTICE
```sql
ID                  NUMBER          PK
ID_KTGSPOLECNOST    NUMBER          FK → KP_DAT_SPOLECNOST
S_MENA              VARCHAR2(3)     Měna (EUR, USD, CZK)
S_ISIN              VARCHAR2(13)    ISIN kód
DT_ZMENA            DATE
S_UZIVATEL          VARCHAR2(20)
```

**Sequence:** `SQ_KP_KTG_FININVESTICE`

### KP_KTG_FININVESTICEEMISE
```sql
ID                          NUMBER          PK
ID_KTGFINANCNIINVESTICE     NUMBER          FK → KP_KTG_FINANCNIINVESTICE
ID_PARENT                   NUMBER          FK → self (historie)
DT_PLATNOSTOD               DATE            Platnost od
DT_PLATNOSTDO               DATE            Platnost do (NULL = aktuální)
NL_POCETKUSU                NUMBER(10,0)    Počet akcií
NL_NOMINAL                  NUMBER(17,4)    Nominální hodnota
ND_ZAKLADNIJMENI            NUMBER(15,2)    Základní jmění
DT_ZMENA                    DATE
S_UZIVATEL                  VARCHAR2(20)
C_INVESTICE                 CHAR(1)         F/T
C_NENULOVY                  CHAR(1)         0/1
```

**Sequence:** `SQ_KP_KTG_FININVESTICEEMISE`

### KP_DAT_MAJETKOVAUCAST
```sql
ID                              NUMBER          PK
ID_PARENT                       NUMBER          FK → self (historie)
ID_KTGUCETNISPOLECNOST          NUMBER          FK → KP_DAT_SPOLECNOST (vlastník)
ID_KTGFININVESTICEEMISE         NUMBER          FK → KP_KTG_FININVESTICEEMISE
S_UCET                          VARCHAR2(12)    Číslo účtu
DT_PLATNOSTOD                   DATE            Platnost od
DT_PLATNOSTDO                   DATE            Platnost do
ID_CISMUTYPTRANSAKCE            NUMBER          FK → KP_CIS_MAJETKOVAUCASTTYPTRAN
ID_CISMUZPUSOB                  NUMBER          FK → KP_CIS_MAJETKOVAUCASTZPUSOB
NL_POCETKUSU                    NUMBER(17,4)    Počet kusů

-- Transakční měna
S_MENATRANSAKCE                 VARCHAR2(3)
ND_CENATRANSAKCEKUS             NUMBER(15,2)
ND_OBJEMTRANSAKCE               NUMBER(15,2)

-- Kurz
ND_KURZ                         NUMBER(10,6)

-- Účetní měna
S_MENAUCETNI                    VARCHAR2(3)
ND_CENAUCETNIKUS                NUMBER(15,2)
ND_OBJEMUCETNI                  NUMBER(15,2)

-- Audit
DT_ZMENA                        DATE
S_UZIVATEL                      VARCHAR2(20)

-- Další
ID_KTGUCETNISPOLECNOSTKOUPENO   NUMBER          FK → KP_DAT_SPOLECNOST
C_IGNOROVAT                     CHAR(1)         0/1
```

**Sequence:** `SQ_KP_DAT_MAJETKOVAUCAST`

## Testovací Data

### Finanční Investice (5 záznamů)

| ID | Společnost | Měna | ISIN | Popis |
|----|------------|------|------|-------|
| 1 | JTBank a.s. | CZK | CZ0008020240 | Investice do ČSOB |
| 2 | IT Solutions | CZK | CZ0008019106 | Investice do Komerční Banky |
| 3 | Marketing Pro | CZK | CZ0005112300 | Investice do ČEZ |
| 4 | Office Supplies | EUR | ES0178430E18 | Investice do Telefónica |
| 5 | JTBank a.s. | USD | US7181721090 | Investice do Philip Morris |

### Emise (5 záznamů)

| ID | Investice | Počet akcií | Nominál | Základní jmění |
|----|-----------|-------------|---------|----------------|
| 1 | ČSOB | 1 000 000 | 1000 Kč | 1 000 000 000 Kč |
| 2 | Komerční Banka | 500 000 | 1500 Kč | 750 000 000 Kč |
| 3 | ČEZ | 2 000 000 | 500 Kč | 1 000 000 000 Kč |
| 4 | Telefónica | 800 000 | 10 EUR | 8 000 000 EUR |
| 5 | Philip Morris | 1 200 000 | 50 USD | 60 000 000 USD |

### Majetkové Účasti (8 záznamů)

| ID | Vlastník | Investice | Účet | Počet akcií | % | Měna Trans. | Měna Účetní | Status |
|----|----------|-----------|------|-------------|---|-------------|-------------|--------|
| 1 | J&T Banka | ČSOB | 0611111111 | 100 000 | 10% | CZK | CZK | ✅ Aktivní |
| 2 | J&T Banka | KB | 0611111112 | 25 000 | 5% | EUR | CZK | ✅ Aktivní |
| 3 | J&T Banka | ČEZ | 0621111111 | 60 000 | 3% | CZK | CZK | ✅ Aktivní |
| 4 | J&T Banka | Telefónica | 0611111113 | 16 000 | 2% | EUR | CZK | ✅ Aktivní |
| 5 | J&T Banka | Philip Morris | 0611111114 | 18 000 | 1.5% | USD | CZK | ✅ Aktivní |
| 6 | IT Solutions | ČSOB | 0612222222 | 80 000 | 8% | CZK | CZK | ✅ Aktivní |
| 7 | Marketing Pro | ČEZ | 0623333333 | 80 000 | 4% | CZK | CZK | ✅ Aktivní |
| 8 | J&T Banka | ČSOB | 0611111111 | 20 000 | 2% | CZK | CZK | ❌ Historický |

**Pozn.:** Záznam 8 je historický (DT_PLATNOSTDO = 2024-01-14), ostatní jsou aktivní (DT_PLATNOSTDO = NULL)

## Spuštění Migrace

### Automatická Metoda (doporučeno)

```bash
cd kis-bff-simple
./run_assets_migration.sh
```

Skript provede:
1. ✅ Smazání existujících tabulek (pokud existují)
2. ✅ Vytvoření sequences
3. ✅ Vytvoření všech 5 tabulek
4. ✅ Vložení číselníků
5. ✅ Vložení testovacích dat
6. ✅ Ověření počtu záznamů

### Manuální Metoda

```bash
# 1. Připojení k Oracle databázi
sqlplus DB_JT/heslo@//localhost:1521/FREEPDB1

# 2. Vytvoření tabulek
SQL> @sql/create_assets_tables.sql

# 3. Vložení testovacích dat
SQL> @sql/insert_assets_test_data.sql

# 4. Ověření
SQL> SELECT table_name, num_rows FROM user_tables
     WHERE table_name LIKE 'KP_%MAJETKOV%' OR table_name LIKE 'KP_%FININ%';
```

### Docker Oracle - Připojení

Pokud běží Oracle v Dockeru:

```bash
# Zjistit container ID
docker ps | grep oracle

# Připojit se do containeru
docker exec -it <container_id> bash

# Spustit sqlplus uvnitř containeru
sqlplus DB_JT/heslo@FREEPDB1
```

## Ověření Migrace

### 1. Kontrola tabulek

```sql
SELECT table_name, num_rows
FROM user_tables
WHERE table_name IN (
    'KP_KTG_FINANCNIINVESTICE',
    'KP_KTG_FININVESTICEEMISE',
    'KP_DAT_MAJETKOVAUCAST',
    'KP_CIS_MAJETKOVAUCASTTYPTRAN',
    'KP_CIS_MAJETKOVAUCASTZPUSOB'
)
ORDER BY table_name;
```

**Očekávaný výsledek:**
```
TABLE_NAME                         NUM_ROWS
---------------------------------- --------
KP_CIS_MAJETKOVAUCASTTYPTRAN              5
KP_CIS_MAJETKOVAUCASTZPUSOB               3
KP_DAT_MAJETKOVAUCAST                     8
KP_KTG_FINANCNIINVESTICE                  5
KP_KTG_FININVESTICEEMISE                  5
```

### 2. Kontrola Foreign Keys

```sql
SELECT constraint_name, table_name, r_constraint_name
FROM user_constraints
WHERE constraint_type = 'R'
  AND table_name LIKE 'KP_%MAJETKOV%' OR table_name LIKE 'KP_%FININ%'
ORDER BY table_name;
```

### 3. Test dotazů

```sql
-- Finanční investice společnosti J&T Banka
SELECT fi.ID, fi.S_ISIN, s.S_NAZEV, fi.S_MENA
FROM KP_KTG_FINANCNIINVESTICE fi
JOIN KP_DAT_SPOLECNOST s ON fi.ID_KTGSPOLECNOST = s.ID
WHERE s.S_NAZEV LIKE '%J&T%';

-- Aktivní majetkové účasti
SELECT ma.ID, s.S_NAZEV AS vlastnik, ma.S_UCET, ma.NL_POCETKUSU
FROM KP_DAT_MAJETKOVAUCAST ma
JOIN KP_DAT_SPOLECNOST s ON ma.ID_KTGUCETNISPOLECNOST = s.ID
WHERE ma.DT_PLATNOSTDO IS NULL;

-- Emise s počtem akcií
SELECT e.ID, e.NL_POCETKUSU, e.NL_NOMINAL,
       (e.NL_POCETKUSU * e.NL_NOMINAL) AS objem
FROM KP_KTG_FININVESTICEEMISE e
WHERE e.DT_PLATNOSTDO IS NULL;
```

## Testování Backend API

Po úspěšné migraci otestujte endpointy:

```bash
# 1. Restart backend serveru
cd kis-bff-simple
mvn spring-boot:run

# 2. Test emissions endpoint
curl http://localhost:8081/bff/emissions | jq '.'

# 3. Test companies endpoint
curl http://localhost:8081/bff/assets/companies | jq '.'

# 4. Test participations endpoint (pro společnost ID=1)
curl http://localhost:8081/bff/assets/companies/1/participations | jq '.'

# 5. Test control rules endpoint
curl http://localhost:8081/bff/assets/controls | jq '.'
```

**Očekávané výsledky:**
- `/bff/emissions` → 5 finančních investic s emisemi
- `/bff/assets/companies` → 5 společností s permissions
- `/bff/assets/companies/1/participations` → 5 aktivních účastí J&T Banky
- `/bff/assets/controls` → 2 kontrolní pravidla

## Spuštění E2E Testů

```bash
cd kis-frontend

# Test všech Assets modulů
npx playwright test e2e/assets-emissions.spec.ts
npx playwright test e2e/assets-participations.spec.ts
npx playwright test e2e/assets-overview-controls.spec.ts

# Test konkrétního browseru
npx playwright test e2e/assets-participations.spec.ts --project=chromium

# Zobrazit UI test runneru
npx playwright test --ui
```

**Očekávaný výsledek po migraci:**
- Emise testy: ↑ z 9/45 na ~35/45 passed (data jsou k dispozici)
- Participace testy: ↑ z 30/45 na ~40/45 passed (data jsou k dispozici)
- Overview testy: zůstává ~33/48 passed (selector issues)

## Rollback Migrace

Pokud chcete vrátit databázi do původního stavu:

```sql
-- Smazat všechny tabulky
DROP TABLE KP_DAT_MAJETKOVAUCAST CASCADE CONSTRAINTS;
DROP TABLE KP_KTG_FININVESTICEEMISE CASCADE CONSTRAINTS;
DROP TABLE KP_KTG_FINANCNIINVESTICE CASCADE CONSTRAINTS;
DROP TABLE KP_CIS_MAJETKOVAUCASTTYPTRAN CASCADE CONSTRAINTS;
DROP TABLE KP_CIS_MAJETKOVAUCASTZPUSOB CASCADE CONSTRAINTS;

-- Smazat sequences
DROP SEQUENCE SQ_KP_DAT_MAJETKOVAUCAST;
DROP SEQUENCE SQ_KP_KTG_FININVESTICEEMISE;
DROP SEQUENCE SQ_KP_KTG_FININVESTICE;

COMMIT;
```

## Časté Problémy a Řešení

### Problém 1: "ORA-00942: table or view does not exist"
**Příčina:** Backend se snaží dotazovat neexistující tabulky.
**Řešení:** Spusťte migrační skript `./run_assets_migration.sh`

### Problém 2: "Sequence does not exist"
**Příčina:** Sequences nebyly vytvořeny.
**Řešení:** Spusťte `create_assets_tables.sql`

### Problém 3: "ORA-02291: integrity constraint violated - parent key not found"
**Příčina:** Neexistuje společnost s ID=1 v KP_DAT_SPOLECNOST.
**Řešení:** Zkontrolujte, že tabulka KP_DAT_SPOLECNOST obsahuje záznamy (měla by z předchozí migrace)

### Problém 4: "Cannot connect to Oracle"
**Příčina:** Oracle Docker container neběží nebo špatné přihlašovací údaje.
**Řešení:**
```bash
# Zkontrolovat běžící Docker containery
docker ps | grep oracle

# Spustit Oracle container (pokud není running)
docker start <container_id>

# Ověřit connection string
sqlplus DB_JT/heslo@//localhost:1521/FREEPDB1
```

## Další Kroky

Po úspěšné migraci:

1. ✅ **Restart Backend** - `mvn spring-boot:run`
2. ✅ **Test API Endpoints** - curl testy výše
3. ✅ **Run E2E Tests** - `npx playwright test e2e/assets-*.spec.ts`
4. ✅ **Manual UI Testing** - Otevřít http://localhost:5173/assets/companies
5. ⏳ **Fix Remaining Test Failures** - Playwright selector issues
6. ⏳ **Implement Excel Export** - Replace mock endpoints
7. ⏳ **Connect Oracle PL/SQL Procedures** - KAP_MAJETEK, KAP_FININV

## Reference

- SQL Skripty: `kis-bff-simple/sql/`
- Entity Definice: `kis-bff-simple/src/main/java/cz/jtbank/kis/bff/entity/`
- Repository: `kis-bff-simple/src/main/java/cz/jtbank/kis/bff/repository/`
- Controllers: `kis-bff-simple/src/main/java/cz/jtbank/kis/bff/controller/`
- E2E Tests: `kis-frontend/e2e/assets-*.spec.ts`

---

**Datum:** 2025-12-10
**Autor:** Claude Code
**Status:** ✅ Ready for Execution
