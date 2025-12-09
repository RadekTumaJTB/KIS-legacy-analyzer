-- ============================================================================
-- KIS Project Module - Create Missing Tables DDL
-- ============================================================================
-- This script creates the 9 missing tables required for the Project module
-- Run as DB_JT user on FREEPDB1 database
-- ============================================================================

-- ============================================================================
-- 1. KP_DAT_PROJEKTCASHFLOW - Project Cash Flow Transactions
-- ============================================================================
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

COMMENT ON TABLE DB_JT.KP_DAT_PROJEKTCASHFLOW IS 'Project cash flow transactions';
COMMENT ON COLUMN DB_JT.KP_DAT_PROJEKTCASHFLOW.ID IS 'Primary key';
COMMENT ON COLUMN DB_JT.KP_DAT_PROJEKTCASHFLOW.ID_KTGPROJEKT IS 'Foreign key to project';
COMMENT ON COLUMN DB_JT.KP_DAT_PROJEKTCASHFLOW.ND_CASTKA IS 'Amount';
COMMENT ON COLUMN DB_JT.KP_DAT_PROJEKTCASHFLOW.DT_DATUM IS 'Transaction date';

-- ============================================================================
-- 2. KP_CIS_PROJEKTSTATUS - Project Status Lookup
-- ============================================================================
CREATE TABLE DB_JT.KP_CIS_PROJEKTSTATUS (
    ID NUMBER(19) PRIMARY KEY,
    S_NAZEV VARCHAR2(100) NOT NULL,
    S_KOD VARCHAR2(50) NOT NULL UNIQUE,
    S_POPIS VARCHAR2(500),
    DT_CREATED DATE DEFAULT SYSDATE,
    S_UZIVATEL VARCHAR2(50),
    DT_UPDATED DATE
);

COMMENT ON TABLE DB_JT.KP_CIS_PROJEKTSTATUS IS 'Project status lookup table';
COMMENT ON COLUMN DB_JT.KP_CIS_PROJEKTSTATUS.S_NAZEV IS 'Status name';
COMMENT ON COLUMN DB_JT.KP_CIS_PROJEKTSTATUS.S_KOD IS 'Status code';

-- ============================================================================
-- 3. KP_CIS_PROJEKTKATEGORIE - Project Category Lookup
-- ============================================================================
CREATE TABLE DB_JT.KP_CIS_PROJEKTKATEGORIE (
    ID NUMBER(19) PRIMARY KEY,
    S_NAZEV VARCHAR2(100) NOT NULL,
    S_KOD VARCHAR2(50) NOT NULL UNIQUE,
    S_POPIS VARCHAR2(500),
    DT_CREATED DATE DEFAULT SYSDATE,
    S_UZIVATEL VARCHAR2(50),
    DT_UPDATED DATE
);

COMMENT ON TABLE DB_JT.KP_CIS_PROJEKTKATEGORIE IS 'Project category lookup table';
COMMENT ON COLUMN DB_JT.KP_CIS_PROJEKTKATEGORIE.S_NAZEV IS 'Category name';
COMMENT ON COLUMN DB_JT.KP_CIS_PROJEKTKATEGORIE.S_KOD IS 'Category code';

-- ============================================================================
-- 4. KP_CIS_PROJEKTFREKVENCE - Project Frequency Lookup
-- ============================================================================
CREATE TABLE DB_JT.KP_CIS_PROJEKTFREKVENCE (
    ID NUMBER(19) PRIMARY KEY,
    S_NAZEV VARCHAR2(100) NOT NULL,
    S_KOD VARCHAR2(50) NOT NULL UNIQUE,
    S_POPIS VARCHAR2(500),
    DT_CREATED DATE DEFAULT SYSDATE,
    S_UZIVATEL VARCHAR2(50),
    DT_UPDATED DATE
);

COMMENT ON TABLE DB_JT.KP_CIS_PROJEKTFREKVENCE IS 'Project evaluation frequency lookup table';
COMMENT ON COLUMN DB_JT.KP_CIS_PROJEKTFREKVENCE.S_NAZEV IS 'Frequency name';
COMMENT ON COLUMN DB_JT.KP_CIS_PROJEKTFREKVENCE.S_KOD IS 'Frequency code';

-- ============================================================================
-- 5. KP_CIS_MNGSEGMENT - Management Segment Lookup
-- ============================================================================
CREATE TABLE DB_JT.KP_CIS_MNGSEGMENT (
    ID NUMBER(19) PRIMARY KEY,
    S_NAZEV VARCHAR2(100) NOT NULL,
    S_KOD VARCHAR2(50) NOT NULL UNIQUE,
    S_POPIS VARCHAR2(500),
    DT_CREATED DATE DEFAULT SYSDATE,
    S_UZIVATEL VARCHAR2(50),
    DT_UPDATED DATE
);

COMMENT ON TABLE DB_JT.KP_CIS_MNGSEGMENT IS 'Management segment lookup table';
COMMENT ON COLUMN DB_JT.KP_CIS_MNGSEGMENT.S_NAZEV IS 'Segment name';
COMMENT ON COLUMN DB_JT.KP_CIS_MNGSEGMENT.S_KOD IS 'Segment code';

-- ============================================================================
-- 6. KP_CIS_TYPBUDGETUPROJEKTU - Budget Type Lookup
-- ============================================================================
CREATE TABLE DB_JT.KP_CIS_TYPBUDGETUPROJEKTU (
    ID NUMBER(19) PRIMARY KEY,
    S_NAZEV VARCHAR2(100) NOT NULL,
    S_KOD VARCHAR2(50) NOT NULL UNIQUE,
    S_POPIS VARCHAR2(500),
    DT_CREATED DATE DEFAULT SYSDATE,
    S_UZIVATEL VARCHAR2(50),
    DT_UPDATED DATE
);

COMMENT ON TABLE DB_JT.KP_CIS_TYPBUDGETUPROJEKTU IS 'Project budget type lookup table';
COMMENT ON COLUMN DB_JT.KP_CIS_TYPBUDGETUPROJEKTU.S_NAZEV IS 'Budget type name';
COMMENT ON COLUMN DB_JT.KP_CIS_TYPBUDGETUPROJEKTU.S_KOD IS 'Budget type code';

-- ============================================================================
-- 7. KP_CIS_TYPPROJEKTOVEBILANCE - Balance Type Lookup
-- ============================================================================
CREATE TABLE DB_JT.KP_CIS_TYPPROJEKTOVEBILANCE (
    ID NUMBER(19) PRIMARY KEY,
    S_NAZEV VARCHAR2(100) NOT NULL,
    S_KOD VARCHAR2(50) NOT NULL UNIQUE,
    S_POPIS VARCHAR2(500),
    DT_CREATED DATE DEFAULT SYSDATE,
    S_UZIVATEL VARCHAR2(50),
    DT_UPDATED DATE
);

COMMENT ON TABLE DB_JT.KP_CIS_TYPPROJEKTOVEBILANCE IS 'Project balance type lookup table';
COMMENT ON COLUMN DB_JT.KP_CIS_TYPPROJEKTOVEBILANCE.S_NAZEV IS 'Balance type name';
COMMENT ON COLUMN DB_JT.KP_CIS_TYPPROJEKTOVEBILANCE.S_KOD IS 'Balance type code';

-- ============================================================================
-- 8. KP_CIS_PROJEKTCASHFLOWTYP - Cash Flow Type Lookup
-- ============================================================================
CREATE TABLE DB_JT.KP_CIS_PROJEKTCASHFLOWTYP (
    ID NUMBER(19) PRIMARY KEY,
    S_NAZEV VARCHAR2(100) NOT NULL,
    S_KOD VARCHAR2(50) NOT NULL UNIQUE,
    S_POPIS VARCHAR2(500),
    DT_CREATED DATE DEFAULT SYSDATE,
    S_UZIVATEL VARCHAR2(50),
    DT_UPDATED DATE
);

COMMENT ON TABLE DB_JT.KP_CIS_PROJEKTCASHFLOWTYP IS 'Cash flow type lookup table';
COMMENT ON COLUMN DB_JT.KP_CIS_PROJEKTCASHFLOWTYP.S_NAZEV IS 'Cash flow type name';
COMMENT ON COLUMN DB_JT.KP_CIS_PROJEKTCASHFLOWTYP.S_KOD IS 'Cash flow type code';

-- ============================================================================
-- 9. KP_CIS_PROJEKTINOOUTTYP - In/Out Type Lookup
-- ============================================================================
CREATE TABLE DB_JT.KP_CIS_PROJEKTINOOUTTYP (
    ID NUMBER(19) PRIMARY KEY,
    S_NAZEV VARCHAR2(100) NOT NULL,
    S_KOD VARCHAR2(50) NOT NULL UNIQUE,
    S_POPIS VARCHAR2(500),
    DT_CREATED DATE DEFAULT SYSDATE,
    S_UZIVATEL VARCHAR2(50),
    DT_UPDATED DATE
);

COMMENT ON TABLE DB_JT.KP_CIS_PROJEKTINOOUTTYP IS 'Cash flow in/out type lookup table';
COMMENT ON COLUMN DB_JT.KP_CIS_PROJEKTINOOUTTYP.S_NAZEV IS 'In/out type name';
COMMENT ON COLUMN DB_JT.KP_CIS_PROJEKTINOOUTTYP.S_KOD IS 'In/out type code';

-- ============================================================================
-- Create Indexes for Foreign Keys
-- ============================================================================
CREATE INDEX IDX_CASHFLOW_PROJECT ON DB_JT.KP_DAT_PROJEKTCASHFLOW(ID_KTGPROJEKT);
CREATE INDEX IDX_CASHFLOW_TYPE ON DB_JT.KP_DAT_PROJEKTCASHFLOW(ID_CASHFLOWTYP);
CREATE INDEX IDX_CASHFLOW_MENA ON DB_JT.KP_DAT_PROJEKTCASHFLOW(ID_MENA);
CREATE INDEX IDX_CASHFLOW_INOUT ON DB_JT.KP_DAT_PROJEKTCASHFLOW(ID_INOUTTTYP);

-- ============================================================================
-- Verification Query
-- ============================================================================
SELECT 'Tables created successfully!' FROM DUAL;

-- Count all project tables
SELECT table_name,
       (SELECT COUNT(*) FROM user_tab_columns WHERE table_name = ut.table_name) as column_count
FROM user_tables ut
WHERE table_name LIKE 'KP_%'
ORDER BY table_name;

-- ============================================================================
-- END OF SCRIPT
-- ============================================================================
COMMIT;
