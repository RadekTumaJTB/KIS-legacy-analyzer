-- ============================================================================
-- KIS Project Module - Test Data Migration Script
-- ============================================================================
-- This script populates the DB_JT schema with test data for the Project module
-- Execute as DB_JT user on FREEPDB1 database
-- ============================================================================

-- Clear existing data (if any)
DELETE FROM DB_JT.KP_DAT_PROJEKTCASHFLOW;
DELETE FROM DB_JT.KP_KTG_PROJEKT;

-- ============================================================================
-- 1. LOOKUP TABLES (Číselníky)
-- ============================================================================

-- KP_CIS_PROJEKTSTATUS - Project Statuses
INSERT INTO DB_JT.KP_CIS_PROJEKTSTATUS (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (1, 'Aktivní', 'ACTIVE', SYSDATE, 'SYSTEM');

INSERT INTO DB_JT.KP_CIS_PROJEKTSTATUS (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (2, 'V přípravě', 'IN_PLANNING', SYSDATE, 'SYSTEM');

INSERT INTO DB_JT.KP_CIS_PROJEKTSTATUS (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (3, 'Probíhá', 'IN_PROGRESS', SYSDATE, 'SYSTEM');

INSERT INTO DB_JT.KP_CIS_PROJEKTSTATUS (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (4, 'Pozastaveno', 'ON_HOLD', SYSDATE, 'SYSTEM');

INSERT INTO DB_JT.KP_CIS_PROJEKTSTATUS (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (5, 'Ukončeno', 'COMPLETED', SYSDATE, 'SYSTEM');

-- KP_CIS_PROJEKTKATEGORIE - Project Categories
INSERT INTO DB_JT.KP_CIS_PROJEKTKATEGORIE (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (1, 'IT Projekty', 'IT', SYSDATE, 'SYSTEM');

INSERT INTO DB_JT.KP_CIS_PROJEKTKATEGORIE (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (2, 'Business Development', 'BD', SYSDATE, 'SYSTEM');

INSERT INTO DB_JT.KP_CIS_PROJEKTKATEGORIE (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (3, 'Infrastruktura', 'INFRA', SYSDATE, 'SYSTEM');

-- KP_CIS_PROJEKTFREKVENCE - Project Frequencies
INSERT INTO DB_JT.KP_CIS_PROJEKTFREKVENCE (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (1, 'Měsíční', 'MONTHLY', SYSDATE, 'SYSTEM');

INSERT INTO DB_JT.KP_CIS_PROJEKTFREKVENCE (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (2, 'Čtvrtletní', 'QUARTERLY', SYSDATE, 'SYSTEM');

INSERT INTO DB_JT.KP_CIS_PROJEKTFREKVENCE (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (3, 'Roční', 'YEARLY', SYSDATE, 'SYSTEM');

-- KP_CIS_MNGSEGMENT - Management Segments
INSERT INTO DB_JT.KP_CIS_MNGSEGMENT (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (1, 'IT oddělení', 'IT_DEPT', SYSDATE, 'SYSTEM');

INSERT INTO DB_JT.KP_CIS_MNGSEGMENT (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (2, 'Business Development', 'BD_DEPT', SYSDATE, 'SYSTEM');

INSERT INTO DB_JT.KP_CIS_MNGSEGMENT (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (3, 'Finance', 'FIN_DEPT', SYSDATE, 'SYSTEM');

-- KP_CIS_MENA - Currencies
INSERT INTO DB_JT.KP_CIS_MENA (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (1, 'Česká koruna', 'CZK', SYSDATE, 'SYSTEM');

INSERT INTO DB_JT.KP_CIS_MENA (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (2, 'Euro', 'EUR', SYSDATE, 'SYSTEM');

INSERT INTO DB_JT.KP_CIS_MENA (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (3, 'Americký dolar', 'USD', SYSDATE, 'SYSTEM');

-- KP_CIS_TYPBUDGETUPROJEKTU - Budget Types
INSERT INTO DB_JT.KP_CIS_TYPBUDGETUPROJEKTU (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (1, 'CAPEX', 'CAPEX', SYSDATE, 'SYSTEM');

INSERT INTO DB_JT.KP_CIS_TYPBUDGETUPROJEKTU (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (2, 'OPEX', 'OPEX', SYSDATE, 'SYSTEM');

-- KP_CIS_TYPPROJEKTOVEBILANCE - Balance Types
INSERT INTO DB_JT.KP_CIS_TYPPROJEKTOVEBILANCE (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (1, 'Aktivní', 'ACTIVE', SYSDATE, 'SYSTEM');

INSERT INTO DB_JT.KP_CIS_TYPPROJEKTOVEBILANCE (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (2, 'Pasivní', 'PASSIVE', SYSDATE, 'SYSTEM');

-- KP_CIS_PROJEKTCASHFLOWTYP - Cash Flow Types
INSERT INTO DB_JT.KP_CIS_PROJEKTCASHFLOWTYP (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (1, 'Investice', 'INVESTMENT', SYSDATE, 'SYSTEM');

INSERT INTO DB_JT.KP_CIS_PROJEKTCASHFLOWTYP (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (2, 'Provozní náklady', 'OPERATING_COST', SYSDATE, 'SYSTEM');

INSERT INTO DB_JT.KP_CIS_PROJEKTCASHFLOWTYP (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (3, 'Licence', 'LICENSE', SYSDATE, 'SYSTEM');

INSERT INTO DB_JT.KP_CIS_PROJEKTCASHFLOWTYP (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (4, 'Služby', 'SERVICES', SYSDATE, 'SYSTEM');

-- KP_CIS_PROJEKTINOOUTTYP - In/Out Types
INSERT INTO DB_JT.KP_CIS_PROJEKTINOOUTTYP (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (1, 'Příjem', 'IN', SYSDATE, 'SYSTEM');

INSERT INTO DB_JT.KP_CIS_PROJEKTINOOUTTYP (ID, S_NAZEV, S_KOD, DT_CREATED, S_UZIVATEL)
VALUES (2, 'Výdaj', 'OUT', SYSDATE, 'SYSTEM');

-- ============================================================================
-- 2. MAIN DATA - PROJECTS
-- ============================================================================

-- Project 1: Implementace CRM systému
INSERT INTO DB_JT.KP_KTG_PROJEKT (
    ID, S_NAZEV, S_CISLO, ID_STATUS, ID_PMANAGER, ID_MNGSEGMENT, ID_MENA,
    DT_HODNOCENI_OD, ID_FREKVENCE, S_POPIS,
    ND_DOCUROVEN1, ND_DOCUROVEN2, ND_DOCUROVEN3,
    C_SLEDUJEBUDGET, ND_BUDGETNAVYSENI_PM, ND_BUDGETNAVYSENI_TOP,
    ID_TYPBILANCE, ID_TYPBUDGETU, ID_KATEGORIE,
    DT_CREATED, S_UZIVATEL, DT_UPDATED
) VALUES (
    1, 'Implementace CRM systému', 'PRJ-2025-001', 1, 101, 1, 1,
    TO_DATE('2025-01-15', 'YYYY-MM-DD'), 1,
    'Implementace nového CRM systému pro zlepšení vztahů se zákazníky. Projekt zahrnuje analýzu požadavků, výběr dodavatele, implementaci a školení uživatelů.',
    500000, 1000000, 2000000,
    'A', 100000, 200000,
    1, 1, 1,
    SYSDATE, 'admin', SYSDATE
);

-- Project 2: Modernizace IT infrastruktury
INSERT INTO DB_JT.KP_KTG_PROJEKT (
    ID, S_NAZEV, S_CISLO, ID_STATUS, ID_PMANAGER, ID_MNGSEGMENT, ID_MENA,
    DT_HODNOCENI_OD, ID_FREKVENCE, S_POPIS,
    ND_DOCUROVEN1, ND_DOCUROVEN2, ND_DOCUROVEN3,
    C_SLEDUJEBUDGET, ND_BUDGETNAVYSENI_PM, ND_BUDGETNAVYSENI_TOP,
    ID_TYPBILANCE, ID_TYPBUDGETU, ID_KATEGORIE,
    DT_CREATED, S_UZIVATEL, DT_UPDATED
) VALUES (
    2, 'Modernizace IT infrastruktury', 'PRJ-2025-002', 1, 102, 1, 1,
    TO_DATE('2025-02-01', 'YYYY-MM-DD'), 1,
    'Upgrade serverů a síťové infrastruktury pro zvýšení výkonu a spolehlivosti systémů.',
    800000, 1500000, 3000000,
    'A', 150000, 300000,
    1, 1, 3,
    SYSDATE, 'admin', SYSDATE
);

-- Project 3: Digitalizace procesů
INSERT INTO DB_JT.KP_KTG_PROJEKT (
    ID, S_NAZEV, S_CISLO, ID_STATUS, ID_PMANAGER, ID_MNGSEGMENT, ID_MENA,
    DT_HODNOCENI_OD, ID_FREKVENCE, S_POPIS,
    ND_DOCUROVEN1, ND_DOCUROVEN2, ND_DOCUROVEN3,
    C_SLEDUJEBUDGET, ND_BUDGETNAVYSENI_PM, ND_BUDGETNAVYSENI_TOP,
    ID_TYPBILANCE, ID_TYPBUDGETU, ID_KATEGORIE,
    DT_CREATED, S_UZIVATEL, DT_UPDATED
) VALUES (
    3, 'Digitalizace procesů', 'PRJ-2025-003', 2, 103, 2, 1,
    TO_DATE('2025-03-01', 'YYYY-MM-DD'), 2,
    'Digitalizace klíčových obchodních procesů pro zvýšení efektivity a snížení nákladů.',
    300000, 600000, 1000000,
    'A', 50000, 100000,
    1, 2, 2,
    SYSDATE, 'admin', SYSDATE
);

-- ============================================================================
-- 3. CASH FLOW DATA
-- ============================================================================

-- Cash Flow for Project 1
INSERT INTO DB_JT.KP_DAT_PROJEKTCASHFLOW (
    ID, ID_KTGPROJEKT, ID_CASHFLOWTYP, DT_DATUM, ND_CASTKA, ID_MENA,
    ID_INOUTTTYP, ID_POZICETYP, S_POZNAMKA, DT_CREATED, S_UZIVATEL
) VALUES (
    1, 1, 1, TO_DATE('2025-01-15', 'YYYY-MM-DD'), 500000, 1,
    2, 1, 'Počáteční investice do projektu', SYSDATE, 'admin'
);

INSERT INTO DB_JT.KP_DAT_PROJEKTCASHFLOW (
    ID, ID_KTGPROJEKT, ID_CASHFLOWTYP, DT_DATUM, ND_CASTKA, ID_MENA,
    ID_INOUTTTYP, ID_POZICETYP, S_POZNAMKA, DT_CREATED, S_UZIVATEL
) VALUES (
    2, 1, 2, TO_DATE('2025-02-15', 'YYYY-MM-DD'), 150000, 1,
    2, 2, 'Měsíční provozní náklady', SYSDATE, 'admin'
);

INSERT INTO DB_JT.KP_DAT_PROJEKTCASHFLOW (
    ID, ID_KTGPROJEKT, ID_CASHFLOWTYP, DT_DATUM, ND_CASTKA, ID_MENA,
    ID_INOUTTTYP, ID_POZICETYP, S_POZNAMKA, DT_CREATED, S_UZIVATEL
) VALUES (
    3, 1, 3, TO_DATE('2025-03-01', 'YYYY-MM-DD'), 200000, 1,
    2, 2, 'Roční licence software', SYSDATE, 'admin'
);

-- Cash Flow for Project 2
INSERT INTO DB_JT.KP_DAT_PROJEKTCASHFLOW (
    ID, ID_KTGPROJEKT, ID_CASHFLOWTYP, DT_DATUM, ND_CASTKA, ID_MENA,
    ID_INOUTTTYP, ID_POZICETYP, S_POZNAMKA, DT_CREATED, S_UZIVATEL
) VALUES (
    4, 2, 1, TO_DATE('2025-02-01', 'YYYY-MM-DD'), 1200000, 1,
    2, 1, 'Nákup serverů a síťového vybavení', SYSDATE, 'admin'
);

INSERT INTO DB_JT.KP_DAT_PROJEKTCASHFLOW (
    ID, ID_KTGPROJEKT, ID_CASHFLOWTYP, DT_DATUM, ND_CASTKA, ID_MENA,
    ID_INOUTTTYP, ID_POZICETYP, S_POZNAMKA, DT_CREATED, S_UZIVATEL
) VALUES (
    5, 2, 4, TO_DATE('2025-02-15', 'YYYY-MM-DD'), 300000, 1,
    2, 2, 'Implementační služby', SYSDATE, 'admin'
);

-- Cash Flow for Project 3
INSERT INTO DB_JT.KP_DAT_PROJEKTCASHFLOW (
    ID, ID_KTGPROJEKT, ID_CASHFLOWTYP, DT_DATUM, ND_CASTKA, ID_MENA,
    ID_INOUTTTYP, ID_POZICETYP, S_POZNAMKA, DT_CREATED, S_UZIVATEL
) VALUES (
    6, 3, 2, TO_DATE('2025-03-01', 'YYYY-MM-DD'), 80000, 1,
    2, 2, 'Analytické služby', SYSDATE, 'admin'
);

INSERT INTO DB_JT.KP_DAT_PROJEKTCASHFLOW (
    ID, ID_KTGPROJEKT, ID_CASHFLOWTYP, DT_DATUM, ND_CASTKA, ID_MENA,
    ID_INOUTTTYP, ID_POZICETYP, S_POZNAMKA, DT_CREATED, S_UZIVATEL
) VALUES (
    7, 3, 3, TO_DATE('2025-04-01', 'YYYY-MM-DD'), 120000, 1,
    2, 2, 'Licence workflow systému', SYSDATE, 'admin'
);

-- ============================================================================
-- COMMIT TRANSACTION
-- ============================================================================
COMMIT;

-- ============================================================================
-- VERIFICATION QUERIES
-- ============================================================================
SELECT 'Projects count: ' || COUNT(*) as INFO FROM DB_JT.KP_KTG_PROJEKT;
SELECT 'Cash Flow count: ' || COUNT(*) as INFO FROM DB_JT.KP_DAT_PROJEKTCASHFLOW;
SELECT 'Statuses count: ' || COUNT(*) as INFO FROM DB_JT.KP_CIS_PROJEKTSTATUS;
SELECT 'Categories count: ' || COUNT(*) as INFO FROM DB_JT.KP_CIS_PROJEKTKATEGORIE;

-- ============================================================================
-- END OF SCRIPT
-- ============================================================================
