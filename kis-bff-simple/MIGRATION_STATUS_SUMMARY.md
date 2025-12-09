# KIS Project Module - Migration Status Summary

**Date:** 2025-12-09
**Database:** Oracle 23ai Free Release (FREEPDB1)
**Schema:** DB_JT

---

## Migration Progress

### Phase 1: Table Creation (Partially Successful)

**✓ Successfully Created (8 lookup tables):**
- DB_JT.KP_CIS_PROJEKTSTATUS
- DB_JT.KP_CIS_PROJEKTKATEGORIE
- DB_JT.KP_CIS_PROJEKTFREKVENCE
- DB_JT.KP_CIS_MNGSEGMENT
- DB_JT.KP_CIS_TYPBUDGETUPROJEKTU
- DB_JT.KP_CIS_TYPPROJEKTOVEBILANCE
- DB_JT.KP_CIS_PROJEKTCASHFLOWTYP
- DB_JT.KP_CIS_PROJEKTINOOUTTYP

**✓ Pre-existing Tables (created by user):**
- DB_JT.KP_KTG_PROJEKT
- DB_JT.KP_CIS_MENA

**✗ Failed to Create (1 table):**
- DB_JT.KP_DAT_PROJEKTCASHFLOW
  - **Reason:** Foreign key constraint error
  - **Issue:** References KP_KTG_PROJEKT(ID) but FK creation failed
  - **Likely cause:** KP_KTG_PROJEKT table may not have PRIMARY KEY constraint on ID column

**✗ Failed to Create (4 indexes):**
- All indexes depend on KP_DAT_PROJEKTCASHFLOW table which doesn't exist

**Current Status:** 10 out of 11 tables exist (90.9%)

---

## Phase 2: Data Population (Failed)

**Migration attempted but failed:**
- Successful statements: 26 (mostly lookup table data)
- Failed statements: 13

**Data inserted:**
- Projects: 0 ❌
- Cash Flow records: 0 ❌
- Lookup tables: Partial ⚠️

**Errors encountered:**
- All INSERT statements failed with "bad SQL grammar" error
- Issue: Spring Boot JdbcTemplate has compatibility issues with Oracle-specific SQL syntax (SYSDATE, TO_DATE)

---

## Root Cause Analysis

### Issue 1: Missing Primary Key
The pre-existing `KP_KTG_PROJEKT` table may not have a PRIMARY KEY constraint on the `ID` column, preventing foreign key creation.

**Verification needed:**
```sql
SELECT constraint_name, constraint_type, search_condition
FROM user_constraints
WHERE table_name = 'KP_KTG_PROJEKT';
```

### Issue 2: JdbcTemplate Oracle Compatibility
Spring Boot's JdbcTemplate is not handling Oracle-specific SQL properly:
- SYSDATE function calls
- TO_DATE function calls
- Multi-line INSERT statements

---

## Recommended Next Steps

### Option A: Manual SQL Execution (Recommended)

1. **Create Missing Table:**
   ```sql
   -- First, verify KP_KTG_PROJEKT has PRIMARY KEY
   ALTER TABLE DB_JT.KP_KTG_PROJEKT
   ADD CONSTRAINT PK_KTG_PROJEKT PRIMARY KEY (ID);

   -- Then create cash flow table
   CREATE TABLE DB_JT.KP_DAT_PROJEKTCASHFLOW (
       ID NUMBER(19) PRIMARY KEY,
       ID_KTGPROJEKT NUMBER(19) NOT NULL,
       ID_CASHFLOWTYP NUMBER(19),
       DT_DATUM DATE,
       ND_CASTKA NUMBER(20,2),
       ID_MENA NUMBER(19),
       ID_INOUTTTYP NUMBER(19),
       ID_POZICETYP NUMBER(19),
       S_POZNAMKA VARCHAR2(500),
       DT_CREATED DATE DEFAULT SYSDATE,
       S_UZIVATEL VARCHAR2(50),
       DT_UPDATED DATE,
       CONSTRAINT FK_CASHFLOW_PROJECT FOREIGN KEY (ID_KTGPROJEKT)
           REFERENCES DB_JT.KP_KTG_PROJEKT(ID)
   );

   CREATE INDEX IDX_CASHFLOW_PROJECT ON DB_JT.KP_DAT_PROJEKTCASHFLOW(ID_KTGPROJEKT);
   ```

2. **Populate Data:**
   Open `/Users/radektuma/DEV/KIS/kis-bff-simple/src/main/resources/db/migration/insert_test_data.sql` in SQL Developer or DBeaver and execute it.

### Option B: Check and Fix Existing Table

1. **Verify existing table structure:**
   ```sql
   DESC DB_JT.KP_KTG_PROJEKT;

   SELECT constraint_name, constraint_type
   FROM user_constraints
   WHERE table_name = 'KP_KTG_PROJEKT';
   ```

2. **Add missing constraints if needed:**
   ```sql
   ALTER TABLE DB_JT.KP_KTG_PROJEKT
   ADD CONSTRAINT PK_KTG_PROJEKT PRIMARY KEY (ID);
   ```

3. **Restart migration** after fixing constraints

---

## Files Created for Manual Migration

1. **DDL Script:** `src/main/resources/db/migration/create_missing_tables.sql`
   - Contains CREATE TABLE statement for KP_DAT_PROJEKTCASHFLOW
   - Contains CREATE INDEX statements

2. **Data Script:** `src/main/resources/db/migration/insert_test_data.sql`
   - Contains 3 test projects
   - Contains 7 cash flow records
   - Contains lookup table data (statuses, categories, etc.)

---

## After Successful Migration

Once data is populated:

1. **Disable automatic migration runners:**
   - Comment out `@Component` in `DatabaseTableCreator.java`
   - Comment out `@Component` in `DatabaseMigrationRunner.java`
   - Keep `DatabaseTableVerifier.java` active for verification

2. **Update ProjectAggregationService:**
   - Replace mock data with repository calls
   - File: `src/main/java/cz/jtbank/kis/bff/service/ProjectAggregationService.java`

3. **Restart server and test:**
   - Verify GET http://localhost:8081/bff/projects returns real data
   - Verify GET http://localhost:8081/bff/projects/1 returns project details
   - Test frontend at http://localhost:5173/projects

---

## Migration Files Reference

| File | Purpose | Status |
|------|---------|--------|
| `create_missing_tables.sql` | DDL for missing tables | ✓ Created |
| `insert_test_data.sql` | Test data population | ✓ Created |
| `DatabaseTableCreator.java` | Automatic table creation | ⚠️ Partially worked |
| `DatabaseTableVerifier.java` | Table existence verification | ✓ Working |
| `DatabaseMigrationRunner.java` | Automatic data population | ❌ Failed (Oracle syntax issues) |
| `run_migration.sh` | Bash script (requires SQLPlus) | ❌ Not usable (SQLPlus not installed) |

---

## Contact & Support

For issues or questions:
- Check Oracle error logs
- Verify table constraints with `user_constraints` view
- Use SQL Developer or DBeaver for manual SQL execution

---

**Status:** 🟡 **Partially Complete** - Manual intervention required to complete migration
