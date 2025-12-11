# Oracle Backend Analysis - Kritické Problémy

## Datum: 2025-12-10

## Executive Summary

Analýza pomocí Playwright testů a Oracle CLI odhalila **kritické problémy** v backend implementaci. 
Frontend funguje korektně, ale **backend NEpoužívá Oracle stored procedures** jak bylo zamýšleno.

---

## Kritické Nálezy

### ❌ 1. Oracle Package KAP_PROJEKT Neexistuje v Databázi

**Status:** CRITICAL

**Problém:**
- Package `KAP_PROJEKT` byl vytvořen s `Package specification` (VALID)
- Package body má status **INVALID** kvůli chybějícím závislostem
- Backend Spring Boot aplikace NEMŮŽE volat procedury, které neexistují

**Důsledky:**
- Všechny CRUD operace na projektech jsou nefunkční
- CREATE/UPDATE/DELETE projektu selže při runtime
- Business logika, validace, audit logging a email notifikace NEFUNGUJÍ

**Lokace:**
- SQL soubory: `/Users/radektuma/DEV/KIS/sources/DB/DB_JT/PACKAGES/KAP_PROJEKT.sql`
- Package body: `/Users/radektuma/DEV/KIS/sources/DB/DB_JT/PACKAGE_BODIES/KAP_PROJEKT.sql`

**Potřebná akce:**
1. Identifikovat chybějící závislosti (tabulky, sekvence, jiné package)
2. Vytvořit kompletní databázový schema
3. Znovu zkompilovat KAP_PROJEKT package body

---

### ❌ 2. ProjectService Má Špatné Parametry Procedury

**Status:** CRITICAL

**Problém:**
Implementovaný `ProjectService.java` používá **jiné parametry** než skutečná Oracle procedura.

**Skutečná Oracle procedura `p_KpProjekt`:**
```sql
procedure p_KpProjekt (
    aAkce char,               -- 'I'=Insert, 'U'=Update, 'D'=Delete
    aIdProjekt int,           -- ID projektu
    aNazev varchar2,          -- Název
    aCisloOld varchar2,       -- Staré číslo
    aIdStatus int,            -- Status ID
    aIdNavrhuje int,          -- Navrhl (user ID)
    aIdPManager int,          -- Project manager ID
    aIdMngSeg int,            -- Management segment ID
    aMenaNaklady varchar2,    -- Měna nákladů (currency code)
    aStartOceneni date,       -- Start přecenění
    aIdFrekvence int,         -- Frekvence ID
    aPopis varchar2,          -- Popis
    aIdNavrh int,             -- Návrh ID
    aDtMDalsi date,           -- Další memorandum datum
    aMMesicu int,             -- Měsíců
    aUzivatel varchar2,       -- Uživatel
    aIdTypBilance int,        -- Typ bilance
    aSledujeBudget char,      -- Sleduje budget ('1'/'0')
    aIdTypBudgetu int,        -- Typ rozpočtu
    aKategorie int            -- Kategorie ID
);
```

**Implementovaný ProjectService (CHYBNÝ):**
```java
new SqlParameter("p_id", Types.NUMERIC),
new SqlOutParameter("p_id", Types.NUMERIC),  // ❌ Neexistuje v Oracle
new SqlParameter("p_cisloOld", Types.VARCHAR),
new SqlParameter("p_nazev", Types.VARCHAR),
new SqlParameter("p_popis", Types.VARCHAR),
new SqlParameter("p_status", Types.NUMERIC),
new SqlParameter("p_kategorie", Types.NUMERIC),
new SqlParameter("p_mngsegment", Types.NUMERIC),
new SqlParameter("p_pmanager", Types.NUMERIC),
new SqlParameter("p_valuationStartDate", Types.DATE),
new SqlParameter("p_valuationEndDate", Types.DATE),  // ❌ Neexistuje v Oracle
new SqlParameter("p_typBilance", Types.NUMERIC),
new SqlParameter("p_typBudgetu", Types.NUMERIC),
new SqlParameter("p_sledujebudget", Types.CHAR),
new SqlParameter("p_action", Types.VARCHAR)
```

**Chybějící parametry:**
- `aIdNavrhuje` (Navrhl - user ID)
- `aMenaNaklady` (Currency code)
- `aIdFrekvence` (Frekvence ID)
- `aIdNavrh` (Návrh projektu ID)
- `aDtMDalsi` (Další memorandum datum)
- `aMMesicu` (Počet měsíců)
- `aUzivatel` (Uživatelské jméno)

**Lokace:**
`/Users/radektuma/DEV/KIS/kis-bff-simple/src/main/java/cz/jtbank/kis/bff/service/ProjectService.java:66-82`

---

### ❌ 3. ProjectFormData DTO Je Nekompletní

**Status:** HIGH

**Problém:**
`ProjectFormData.java` nemá fields pro všechny parametry Oracle procedury.

**Chybějící fields:**
```java
private Long proposedById;        // aIdNavrhuje
private String currencyCode;      // aMenaNaklady
private Long frequencyId;         // aIdFrekvence
private Long proposalId;          // aIdNavrh
private LocalDate nextMemorandumDate;  // aDtMDalsi
private Integer monthsCount;      // aMMesicu
private String username;          // aUzivatel
```

**Lokace:**
`/Users/radektuma/DEV/KIS/kis-bff-simple/src/main/java/cz/jtbank/kis/bff/dto/project/ProjectFormData.java`

---

### ⚠️ 4. Dokumenty a Rozpočty Stále Používají JPA

**Status:** HIGH

**Problém:**
- `DocumentService` - používá JPA `documentRepository.save()`
- `BudgetService` - používá JPA `budgetRepository.save()`
- **Nepoužívají Oracle procedury:**
  - `KAP_DOKUMENT` package
  - `KAP_BUDGET` package

**Důsledky:**
- Business logika v Oracle proceduře se nespustí
- Multilevel approval workflow nefunguje
- Email notifikace se neposílají
- Audit logging neexistuje

---

## Frontend Analýza (Playwright Test)

### ✅ Frontend Funguje Korektně

**Test Results:**
- ✅ Page loads: http://localhost:5173/projects/1
- ✅ Project detail zobrazuje všechna data
- ✅ Edit modal otevírá se správně
- ✅ 20 form fields včetně required/optional
- ✅ Validation pomocí react-hook-form + Zod
- ✅ Žádné console errors
- ✅ Screenshots zachyceny v `/Users/radektuma/DEV/KIS/kis-frontend/test-results/`

**Test File:**
`/Users/radektuma/DEV/KIS/kis-frontend/e2e/project-detail-analysis.spec.ts`

---

## Porovnání s Legacy JSP Aplikací

### Legacy Implementace (SPRÁVNÁ)

**Soubor:** `ProjektyNew.jsp`

```java
// Direct procedure call from JSP
dbTransaction.begin();
String sql = "{call DB_JT.KAP_PROJEKT.p_KpProjekt(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
CallableStatement stmt = conn.prepareCall(sql);
stmt.setString(1, action);        // aAkce
stmt.setInt(2, projectId);        // aIdProjekt
stmt.setString(3, name);          // aNazev
// ... all 20 parameters
stmt.execute();
dbTransaction.commit();
```

**Funguje protože:**
1. Package `KAP_PROJEKT` existoval v produkční databázi
2. Všechny závislé tabulky existovaly
3. Business logika v proceduře se spouštěla
4. Email notifikace fungovaly
5. Audit logging zapisoval změny

### React/BFF Implementace (CHYBNÁ)

```java
// ProjectService.java - calls non-existent procedure
SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
    .withSchemaName("DB_JT")
    .withCatalogName("KAP_PROJEKT")
    .withProcedureName("p_KpProjekt");
    
// ❌ Package body je INVALID
// ❌ Procedura není executable
// ❌ Runtime selže s ORA-04068: existing state of packages has been discarded
```

---

## Kritické Akce (Priorita)

### 1. Opravit Oracle Package ⚠️ URGENT

**Kroky:**
1. Analyzovat chybějící závislosti v KAP_PROJEKT.sql
2. Vytvořit kompletní database schema:
   - Všechny lookup tabulky (KP_CIS_*)
   - Log tabulky (KP_LOG_*)
   - Sekvence
   - Triggery
3. Znovu zkompilovat package body
4. Ověřit status = VALID

### 2. Opravit ProjectService ⚠️ HIGH

**Kroky:**
1. Updatovat `declareParameters()` podle skutečné Oracle procedury
2. Přidat všech 20 parametrů ve správném pořadí
3. Updatovat `ProjectFormData` DTO
4. Updatovat frontend formuláře

### 3. Přepsat DocumentService a BudgetService ⚠️ HIGH

**Kroky:**
1. Najít Oracle package SQL soubory:
   - `KAP_DOKUMENT.sql`
   - `KAP_BUDGET.sql`
2. Nahrát do databáze
3. Přepsat services aby volaly Oracle procedury
4. Testovat

### 4. E2E Integrace Test ⚠️ MEDIUM

**Kroky:**
1. Vytvořit Playwright test pro complete CRUD flow
2. Test musí volat skutečné Oracle procedury
3. Verifikovat audit logging
4. Verifikovat email notifikace (mock)

---

## Docker Containers Status

```
NAMES                  STATUS                   PORTS
kis-analytics-qdrant   Up 4 days                6333-6334
kis-analytics-neo4j    Up 4 days                7474, 7687
kis-oracle             Up 4 days (healthy)      1521, 5500
```

**Connection String:**
```
jdbc:oracle:thin:@localhost:1521/FREEPDB1
User: DB_JT
Password: kis_db_jt_2024
```

---

## Závěr

**Aktuální stav:**
- ❌ Backend není kompatibilní s legacy aplikací
- ❌ Oracle procedury nejsou funkční
- ❌ Business logika nefunguje
- ✅ Frontend je korektně implementován

**Potřebné investice času:**
- **1-2 dny:** Oprava Oracle package a ProjectService
- **1 den:** Přepis DocumentService a BudgetService
- **0.5 dne:** E2E testy

**Doporučení:**
Priorita #1 je **opravit Oracle database schema a package**, protože bez toho aplikace nemůže fungovat podle legacy specifikace.
