# Oracle Database Deployment Analysis
## Date: 2025-12-10 12:02

## Summary

After fixing the deleted tablespace issue (`"_$deleted$10$0"` → `"USERS"`), the deployment still has **120 tables failing** with **NO progress** in retry iterations.

**Current Status:**
- ✓ **632 tables deployed successfully** (84%)
- ✗ **120 tables failing** (16%)
- **0% progress** in continuous retry loop

## Error Analysis

### Top Oracle Errors (from deployment_20251210_120155.log)

| Error Code | Count | Description | Impact |
|------------|-------|-------------|--------|
| **ORA-03405** | 586 | "clause subquery cannot reference FOREIGN KEY constraints" | **CRITICAL** - Reference-partitioned tables |
| **ORA-02270** | 43 | "no matching unique or primary key for this column-list" | Foreign key constraints |
| **ORA-00936** | 32 | "missing expression" | SQL syntax errors |
| **ORA-02153** | 26 | "invalid specification for EXTERNAL data type" | External table definitions |
| **ORA-02000** | 16 | "missing keyword or invalid keyword" | SQL syntax errors |

## Root Cause Analysis

### Problem 1: ORA-03405 - Reference-Partitioned Tables (586 errors)

**What is PARTITION BY REFERENCE?**

Reference partitioning allows a child table to inherit its partitioning strategy from a parent table via a foreign key relationship. Example from `KP_DAT_DOKLADZDROJDAT.sql`:

```sql
CREATE TABLE "DB_JT"."KP_DAT_DOKLADZDROJDAT"
(
    "ID" NUMBER(*,0),
    "ID_DOKLAD" NUMBER(*,0),
    ...
    CONSTRAINT "FK_KPZDROJDAT_DOKLAD2" FOREIGN KEY ("ID_DOKLAD")
      REFERENCES "DB_JT"."KP_DAT_DOKLAD" ("ID") ENABLE
)
PARTITION BY REFERENCE ("FK_KPZDROJDAT_DOKLAD2")  -- ← References parent table's partitioning
```

**Why It's Failing:**

ORA-03405 occurs when:
1. The parent table (e.g., `KP_DAT_DOKLAD`) doesn't exist yet
2. The foreign key constraint is defined inline (within CREATE TABLE) rather than via separate ALTER TABLE
3. Oracle cannot resolve the foreign key reference during table creation

**Failed Parent Tables:**

Critical parent tables that are failing (and blocking child tables):
- `KP_DAT_DOKLAD.sql` - Main document table (has ~50 child tables)
- `KP_DAT_DOKLADPOLOZKA.sql` - Document items
- `KP_KTG_UCETNISPOLECNOST.sql` - Accounting company
- `KP_KTG_SPOLECNOST.sql` - Company master

### Problem 2: Why Are Parent Tables Failing?

**Investigation needed:** If `KP_DAT_DOKLAD` has no tablespace issues anymore, what's preventing it from deploying?

Possibilities:
1. Missing dependent objects (types, sequences)
2. Invalid column data types
3. Invalid partition definitions
4. Missing referenced tables in foreign keys
5. Oracle 23ai Free Edition limitations

### Problem 3: Circular Dependencies

Many tables have circular foreign key references, making deployment order critical. Example:
- Table A → references → Table B
- Table B → references → Table C
- Table C → references → Table A

## Failed Table Categories

### Category 1: Migration/Metadata Tables (MD_*)
Failed metadata tables (likely Oracle-specific system catalog tables):
- `MD_TABLES.sql`
- `MD_COLUMNS.sql`
- `MD_INDEXES.sql`
- `MD_CONSTRAINTS.sql`
- `MD_STORED_PROGRAMS.sql`
- `MD_TRIGGERS.sql`
- etc.

**Impact:** These may be SQL Server migration artifacts that don't apply to Oracle.

### Category 2: Data Warehouse Tables (DW_*)
- `DW_CIS_ISIT_APLIKACE.sql`
- `DW_CIS_ISIT_TYP_SW.sql`
- `DW_CIS_ISIT_TYP_POLOZKY.sql`
- `DW_CIS_ISIT_DRUH_NAKLADU.sql`

### Category 3: Core Banking Tables (KP_*)
Critical business tables that are failing:
- `KP_DAT_DOKLAD.sql` - **CRITICAL PARENT TABLE**
- `KP_DAT_DOKLADZDROJDAT.sql` - References KP_DAT_DOKLAD
- `KP_DAT_DOKLADPOLOZKA.sql` - Document items
- `KP_KTG_SPOLECNOST.sql` - Company master
- `KP_CIS_*.sql` - Various code tables
- `KP_DAT_DOKUMENT.sql` - Document management

## Recommended Solutions

### Option 1: Two-Phase Deployment (RECOMMENDED)

**Phase 1: Deploy tables WITHOUT foreign key constraints**
- Strip out all FOREIGN KEY constraints from CREATE TABLE statements
- Deploy all tables first
- This allows parent tables to exist before children reference them

**Phase 2: Add constraints separately**
- After all tables exist, add foreign key constraints via ALTER TABLE
- This resolves circular dependencies and reference partition issues

**Implementation:**
1. Create `scripts/strip_foreign_keys.py` to remove FK constraints from SQL files
2. Deploy tables without constraints
3. Deploy constraints as separate ALTER TABLE statements

### Option 2: Manual Dependency Ordering

Analyze foreign key dependencies and deploy tables in topological sort order:
1. Tables with no foreign keys (leaf tables)
2. Tables that reference level 1 tables
3. Continue until all tables deployed

**Challenge:** Circular references make this impossible without constraint separation.

### Option 3: Disable Reference Partitioning (TEMPORARY)

- Convert `PARTITION BY REFERENCE` to simple `PARTITION BY RANGE` or remove partitioning
- Deploy all tables
- Re-enable reference partitioning later if needed

**Trade-off:** Loses partition inheritance benefits but allows deployment to proceed.

## Next Steps

1. **Investigate KP_DAT_DOKLAD failure** - Why isn't the main parent table deploying?
   ```bash
   grep -B 5 -A 10 "KP_DAT_DOKLAD.sql" deployment_20251210_120155.log | grep -A 5 "Error:"
   ```

2. **Implement Two-Phase Deployment** - Most likely to succeed
   - Create script to extract and remove FK constraints
   - Deploy tables first
   - Deploy constraints second

3. **Test on sample tables** - Try the two-phase approach on 5-10 failing tables to validate

4. **Check MD_* tables** - Determine if these are needed or can be skipped

## Impact Assessment

**Current State:**
- 84% of tables deployed successfully (632/752)
- Core functionality may be partially working
- Reference-partitioned tables are non-functional

**Business Impact:**
- Document management system (`KP_DAT_DOKLAD*`) - **DOWN**
- Company/accounting entities - **DOWN**
- Some reporting/analytics - **DOWN**
- Migration metadata - **DOWN**

**Technical Debt:**
- 120 failed tables
- 586 foreign key constraint issues
- Potentially invalid migration artifacts (MD_* tables)
