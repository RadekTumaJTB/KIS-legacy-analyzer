-- ============================================================================
-- KIS Test Data Migration Script - Schema-Aligned
-- Modules: Projects, Documents, Budgets, Assets (Financial Investments)
-- ============================================================================

SET DEFINE OFF;

-- ============================================================================
-- 1. LOOKUP TABLES (Číselníky) - Required for Foreign Keys
-- ============================================================================

-- KP_CIS_PROJEKTSTATUS - Project Statuses
-- Structure: ID (NUMBER), S_POPIS (VARCHAR2(64))
INSERT INTO KP_CIS_PROJEKTSTATUS (ID, S_POPIS) VALUES (1, 'Aktivní');
INSERT INTO KP_CIS_PROJEKTSTATUS (ID, S_POPIS) VALUES (2, 'Ukončený');
INSERT INTO KP_CIS_PROJEKTSTATUS (ID, S_POPIS) VALUES (3, 'Pozastavený');
INSERT INTO KP_CIS_PROJEKTSTATUS (ID, S_POPIS) VALUES (4, 'Zrušený');
INSERT INTO KP_CIS_PROJEKTSTATUS (ID, S_POPIS) VALUES (5, 'Ve schvalování');

-- KP_CIS_MENA - Currencies
INSERT INTO KP_CIS_MENA (S_KOD, S_POPIS) VALUES ('CZK', 'Česká koruna');
INSERT INTO KP_CIS_MENA (S_KOD, S_POPIS) VALUES ('EUR', 'Euro');
INSERT INTO KP_CIS_MENA (S_KOD, S_POPIS) VALUES ('USD', 'Americký dolar');

-- KP_CIS_MNGSEGMENT - Management Segments
INSERT INTO KP_CIS_MNGSEGMENT (ID, S_NAZEV, S_KOD) VALUES (1, 'IT a Digitalizace', 'IT');
INSERT INTO KP_CIS_MNGSEGMENT (ID, S_NAZEV, S_KOD) VALUES (2, 'Finance', 'FIN');
INSERT INTO KP_CIS_MNGSEGMENT (ID, S_NAZEV, S_KOD) VALUES (3, 'Provoz', 'OPS');

-- KP_CIS_PROJEKTKATEGORIE - Project Categories
INSERT INTO KP_CIS_PROJEKTKATEGORIE (ID, S_NAZEV, S_KOD) VALUES (1, 'IT Projekty', 'IT');
INSERT INTO KP_CIS_PROJEKTKATEGORIE (ID, S_NAZEV, S_KOD) VALUES (2, 'Procesní Optimalizace', 'PROC');
INSERT INTO KP_CIS_PROJEKTKATEGORIE (ID, S_NAZEV, S_KOD) VALUES (3, 'Infrastruktura', 'INFRA');

-- KP_CIS_PROJEKTFREKVENCE - Project Frequencies
INSERT INTO KP_CIS_PROJEKTFREKVENCE (ID, S_NAZEV, S_KOD) VALUES (1, 'Měsíčně', 'M');
INSERT INTO KP_CIS_PROJEKTFREKVENCE (ID, S_NAZEV, S_KOD) VALUES (2, 'Čtvrtletně', 'Q');
INSERT INTO KP_CIS_PROJEKTFREKVENCE (ID, S_NAZEV, S_KOD) VALUES (3, 'Ročně', 'Y');

-- KP_CIS_TYPPROJEKTOVEBILANCE - Project Balance Types
INSERT INTO KP_CIS_TYPPROJEKTOVEBILANCE (ID, S_NAZEV, S_KOD) VALUES (1, 'Standard', 'STD');
INSERT INTO KP_CIS_TYPPROJEKTOVEBILANCE (ID, S_NAZEV, S_KOD) VALUES (2, 'Rozšířený', 'EXT');

-- KP_CIS_TYPBUDGETUPROJEKTU - Project Budget Types
INSERT INTO KP_CIS_TYPBUDGETUPROJEKTU (ID, S_NAZEV, S_KOD) VALUES (1, 'CAPEX', 'CAPEX');
INSERT INTO KP_CIS_TYPBUDGETUPROJEKTU (ID, S_NAZEV, S_KOD) VALUES (2, 'OPEX', 'OPEX');

-- KP_CIS_DOKUMENTSTATUS - Document Statuses
INSERT INTO KP_CIS_DOKUMENTSTATUS (ID, S_NAZEV, S_KOD) VALUES (1, 'Nový', 'NEW');
INSERT INTO KP_CIS_DOKUMENTSTATUS (ID, S_NAZEV, S_KOD) VALUES (2, 'Ke schválení', 'PENDING');
INSERT INTO KP_CIS_DOKUMENTSTATUS (ID, S_NAZEV, S_KOD) VALUES (3, 'Schválený', 'APPROVED');
INSERT INTO KP_CIS_DOKUMENTSTATUS (ID, S_NAZEV, S_KOD) VALUES (4, 'Zamítnutý', 'REJECTED');
INSERT INTO KP_CIS_DOKUMENTSTATUS (ID, S_NAZEV, S_KOD) VALUES (5, 'Zaúčtovaný', 'POSTED');

-- KP_CIS_DOKUMENT - Document Types
INSERT INTO KP_CIS_DOKUMENT (ID, S_NAZEV, S_KOD) VALUES (1, 'Faktura přijatá', 'FAP');
INSERT INTO KP_CIS_DOKUMENT (ID, S_NAZEV, S_KOD) VALUES (2, 'Faktura vydaná', 'FAV');
INSERT INTO KP_CIS_DOKUMENT (ID, S_NAZEV, S_KOD) VALUES (3, 'Objednávka', 'OBJ');
INSERT INTO KP_CIS_DOKUMENT (ID, S_NAZEV, S_KOD) VALUES (4, 'Smlouva', 'SMV');

-- ============================================================================
-- 2. SHARED ENTITIES - Companies and Departments
-- ============================================================================

-- KP_KTG_SPOLECNOST - Companies
INSERT INTO KP_KTG_SPOLECNOST (ID, S_ICO, S_NAZEV)
VALUES (1, '12345678', 'J&T Banka, a.s.');

INSERT INTO KP_KTG_SPOLECNOST (ID, S_ICO, S_NAZEV)
VALUES (2, '87654321', 'Dodavatel IT s.r.o.');

INSERT INTO KP_KTG_SPOLECNOST (ID, S_ICO, S_NAZEV)
VALUES (3, '11223344', 'Konzultační firma a.s.');

-- KP_KTG_ODBOR - Departments
INSERT INTO KP_KTG_ODBOR (ID, S_NAZEV, S_KOD)
VALUES (1, 'IT oddělení', 'IT');

INSERT INTO KP_KTG_ODBOR (ID, S_NAZEV, S_KOD)
VALUES (2, 'Finanční oddělení', 'FIN');

INSERT INTO KP_KTG_ODBOR (ID, S_NAZEV, S_KOD)
VALUES (3, 'Provozní oddělení', 'OPS');

-- ============================================================================
-- 3. MODULE: PROJECTS (KP_KTG_PROJEKT)
-- ============================================================================

-- Project 1: CRM System Implementation
INSERT INTO KP_KTG_PROJEKT (
    ID, S_NAZEV, ID_STATUS, ID_PMANAGER, ID_MNGSEGMENT,
    DT_PLATNOSTOD, DT_PLATNOSTDO, ID_KATEGORIE, ID_TYPBILANCE, ID_TYPBUDGETU,
    S_POPIS, C_SLEDUJEBUDGET, ND_BUDGETNAVYSENI_PM, ND_BUDGETNAVYSENI_TOP
) VALUES (
    1, 'Implementace CRM systému', 1, 101, 1,
    TO_DATE('2025-01-01', 'YYYY-MM-DD'), TO_DATE('2025-12-31', 'YYYY-MM-DD'),
    1, 1, 1,
    'Nový CRM systém pro zlepšení vztahů se zákazníky', 'A', 100000, 200000
);

-- Project 2: Cloud Migration
INSERT INTO KP_KTG_PROJEKT (
    ID, S_NAZEV, ID_STATUS, ID_PMANAGER, ID_MNGSEGMENT,
    DT_PLATNOSTOD, DT_PLATNOSTDO, ID_KATEGORIE, ID_TYPBILANCE, ID_TYPBUDGETU,
    S_POPIS, C_SLEDUJEBUDGET, ND_BUDGETNAVYSENI_PM, ND_BUDGETNAVYSENI_TOP
) VALUES (
    2, 'Migrace do cloudu', 1, 102, 1,
    TO_DATE('2025-02-01', 'YYYY-MM-DD'), TO_DATE('2026-01-31', 'YYYY-MM-DD'),
    3, 1, 1,
    'Migrace klíčových aplikací do cloudové infrastruktury', 'A', 150000, 300000
);

-- Project 3: Process Digitalization
INSERT INTO KP_KTG_PROJEKT (
    ID, S_NAZEV, ID_STATUS, ID_PMANAGER, ID_MNGSEGMENT,
    DT_PLATNOSTOD, DT_PLATNOSTDO, ID_KATEGORIE, ID_TYPBILANCE, ID_TYPBUDGETU,
    S_POPIS, C_SLEDUJEBUDGET, ND_BUDGETNAVYSENI_PM, ND_BUDGETNAVYSENI_TOP
) VALUES (
    3, 'Digitalizace procesů', 2, 103, 2,
    TO_DATE('2024-06-01', 'YYYY-MM-DD'), TO_DATE('2024-12-31', 'YYYY-MM-DD'),
    2, 1, 2,
    'Automatizace a digitalizace klíčových bankovních procesů', 'A', 80000, 150000
);

-- Project 4: Cybersecurity Enhancement
INSERT INTO KP_KTG_PROJEKT (
    ID, S_NAZEV, ID_STATUS, ID_PMANAGER, ID_MNGSEGMENT,
    DT_PLATNOSTOD, DT_PLATNOSTDO, ID_KATEGORIE, ID_TYPBILANCE, ID_TYPBUDGETU,
    S_POPIS, C_SLEDUJEBUDGET
) VALUES (
    4, 'Posílení kybernetické bezpečnosti', 1, 101, 1,
    TO_DATE('2025-03-01', 'YYYY-MM-DD'), TO_DATE('2025-09-30', 'YYYY-MM-DD'),
    1, 1, 1,
    'Implementace pokročilých bezpečnostních opatření', 'N'
);

-- Project 5: Data Warehouse Upgrade
INSERT INTO KP_KTG_PROJEKT (
    ID, S_NAZEV, ID_STATUS, ID_PMANAGER, ID_MNGSEGMENT,
    DT_PLATNOSTOD, DT_PLATNOSTDO, ID_KATEGORIE, ID_TYPBILANCE, ID_TYPBUDGETU,
    S_POPIS, C_SLEDUJEBUDGET, ND_BUDGETNAVYSENI_PM
) VALUES (
    5, 'Upgrade datového skladu', 3, 102, 1,
    TO_DATE('2024-09-01', 'YYYY-MM-DD'), TO_DATE('2025-03-31', 'YYYY-MM-DD'),
    1, 1, 1,
    'Modernizace datového skladu a BI nástrojů', 'A', 120000
);

-- ============================================================================
-- 4. MODULE: BUDGETS (KP_DAT_BUDGET)
-- ============================================================================

-- Budget 1: IT Department Budget 2025
INSERT INTO KP_DAT_BUDGET (
    ID, ID_KTGODBOR, DT_PLATNOSTOD, DT_PLATNOSTDO, ID_KTGPROJEKT
) VALUES (
    1, 1, TO_DATE('2025-01-01', 'YYYY-MM-DD'), TO_DATE('2025-12-31', 'YYYY-MM-DD'), 1
);

-- Budget 2: Finance Department Budget 2025
INSERT INTO KP_DAT_BUDGET (
    ID, ID_KTGODBOR, DT_PLATNOSTOD, DT_PLATNOSTDO, ID_KTGPROJEKT
) VALUES (
    2, 2, TO_DATE('2025-01-01', 'YYYY-MM-DD'), TO_DATE('2025-12-31', 'YYYY-MM-DD'), 3
);

-- Budget 3: Operations Budget Q1 2025
INSERT INTO KP_DAT_BUDGET (
    ID, ID_KTGODBOR, DT_PLATNOSTOD, DT_PLATNOSTDO
) VALUES (
    3, 3, TO_DATE('2025-01-01', 'YYYY-MM-DD'), TO_DATE('2025-03-31', 'YYYY-MM-DD')
);

-- Budget 4: Cloud Migration Budget
INSERT INTO KP_DAT_BUDGET (
    ID, ID_KTGODBOR, DT_PLATNOSTOD, DT_PLATNOSTDO, ID_KTGPROJEKT
) VALUES (
    4, 1, TO_DATE('2025-02-01', 'YYYY-MM-DD'), TO_DATE('2026-01-31', 'YYYY-MM-DD'), 2
);

-- Budget 5: Cybersecurity Budget
INSERT INTO KP_DAT_BUDGET (
    ID, ID_KTGODBOR, DT_PLATNOSTOD, DT_PLATNOSTDO, ID_KTGPROJEKT
) VALUES (
    5, 1, TO_DATE('2025-03-01', 'YYYY-MM-DD'), TO_DATE('2025-09-30', 'YYYY-MM-DD'), 4
);

-- ============================================================================
-- 5. MODULE: DOCUMENTS (KP_DAT_DOKUMENT)
-- ============================================================================

-- Document 1: Invoice for CRM System
INSERT INTO KP_DAT_DOKUMENT (
    ID, ID_CISDOKUMENT, S_CISLODOKLADU, ID_SPOLECNOST,
    DT_DATUMZADANI, S_POPIS, S_MENA, ID_CISSTATUS, ID_BUDGET
) VALUES (
    1, 1, 'FAP-2025-001', 2,
    TO_DATE('2025-01-15', 'YYYY-MM-DD'),
    'Faktura za implementaci CRM systému - fáze 1',
    'CZK', 1, 1
);

-- Document 2: Service Contract
INSERT INTO KP_DAT_DOKUMENT (
    ID, ID_CISDOKUMENT, S_CISLODOKLADU, ID_SPOLECNOST,
    DT_DATUMZADANI, DT_DATUMSPLATNOSTI, S_POPIS, S_MENA, ID_CISSTATUS, ID_BUDGET
) VALUES (
    2, 4, 'SMV-2025-001', 3,
    TO_DATE('2025-01-10', 'YYYY-MM-DD'),
    TO_DATE('2025-12-31', 'YYYY-MM-DD'),
    'Smlouva na konzultační služby',
    'CZK', 3, 1
);

-- Document 3: Cloud Services Invoice
INSERT INTO KP_DAT_DOKUMENT (
    ID, ID_CISDOKUMENT, S_CISLODOKLADU, ID_SPOLECNOST,
    DT_DATUMZADANI, DT_DATUMSPLATNOSTI, S_POPIS, S_MENA, ID_CISSTATUS, ID_BUDGET
) VALUES (
    3, 1, 'FAP-2025-002', 2,
    TO_DATE('2025-02-05', 'YYYY-MM-DD'),
    TO_DATE('2025-02-20', 'YYYY-MM-DD'),
    'Faktura za cloudové služby - únor 2025',
    'EUR', 2, 4
);

-- Document 4: Purchase Order for Hardware
INSERT INTO KP_DAT_DOKUMENT (
    ID, ID_CISDOKUMENT, S_CISLODOKLADU, ID_SPOLECNOST,
    DT_DATUMZADANI, S_POPIS, S_MENA, ID_CISSTATUS, ID_BUDGET
) VALUES (
    4, 3, 'OBJ-2025-001', 2,
    TO_DATE('2025-01-20', 'YYYY-MM-DD'),
    'Objednávka hardware pro datové centrum',
    'CZK', 2, 5
);

-- Document 5: Software License Invoice
INSERT INTO KP_DAT_DOKUMENT (
    ID, ID_CISDOKUMENT, S_CISLODOKLADU, ID_SPOLECNOST,
    DT_DATUMZADANI, DT_DATUMSPLATNOSTI, S_POPIS, S_MENA, ID_CISSTATUS, ID_BUDGET
) VALUES (
    5, 1, 'FAP-2025-003', 2,
    TO_DATE('2025-01-25', 'YYYY-MM-DD'),
    TO_DATE('2025-02-10', 'YYYY-MM-DD'),
    'Faktura za softwarové licence - roční prodloužení',
    'USD', 1, 1
);

-- Document 6: Consulting Services Invoice
INSERT INTO KP_DAT_DOKUMENT (
    ID, ID_CISDOKUMENT, S_CISLODOKLADU, ID_SPOLECNOST,
    DT_DATUMZADANI, DT_DATUMSPLATNOSTI, S_POPIS, S_MENA, ID_CISSTATUS, ID_BUDGET
) VALUES (
    6, 1, 'FAP-2025-004', 3,
    TO_DATE('2025-02-01', 'YYYY-MM-DD'),
    TO_DATE('2025-02-15', 'YYYY-MM-DD'),
    'Faktura za konzultační služby - leden 2025',
    'CZK', 3, 2
);

-- Document 7: Security Audit Contract
INSERT INTO KP_DAT_DOKUMENT (
    ID, ID_CISDOKUMENT, S_CISLODOKLADU, ID_SPOLECNOST,
    DT_DATUMZADANI, S_POPIS, S_MENA, ID_CISSTATUS, ID_BUDGET
) VALUES (
    7, 4, 'SMV-2025-002', 3,
    TO_DATE('2025-03-01', 'YYYY-MM-DD'),
    'Smlouva na bezpečnostní audit',
    'CZK', 1, 5
);

-- Document 8: Training Services Invoice
INSERT INTO KP_DAT_DOKUMENT (
    ID, ID_CISDOKUMENT, S_CISLODOKLADU, ID_SPOLECNOST,
    DT_DATUMZADANI, DT_DATUMSPLATNOSTI, S_POPIS, S_MENA, ID_CISSTATUS, ID_BUDGET
) VALUES (
    8, 1, 'FAP-2025-005', 3,
    TO_DATE('2025-02-10', 'YYYY-MM-DD'),
    TO_DATE('2025-02-28', 'YYYY-MM-DD'),
    'Faktura za školení zaměstnanců - CRM systém',
    'CZK', 2, 1
);

-- ============================================================================
-- 6. MODULE: ASSETS / FINANCIAL INVESTMENTS (KP_KTG_FINANCNIINVESTICE)
-- ============================================================================

-- Asset 1: Investment in Tech Subsidiary
INSERT INTO KP_KTG_FINANCNIINVESTICE (
    ID, ID_KTGSPOLECNOST, S_MENA, DT_ZMENA, S_UZIVATEL
) VALUES (
    1, 2, 'CZK', SYSDATE, 'admin'
);

-- Asset 2: Strategic Investment in Fintech
INSERT INTO KP_KTG_FINANCNIINVESTICE (
    ID, ID_KTGSPOLECNOST, S_MENA, DT_ZMENA, S_UZIVATEL, S_ISIN
) VALUES (
    2, 3, 'EUR', SYSDATE, 'admin', 'CZ0001234567'
);

-- Asset 3: Investment in Consulting Firm
INSERT INTO KP_KTG_FINANCNIINVESTICE (
    ID, ID_KTGSPOLECNOST, S_MENA, DT_ZMENA, S_UZIVATEL
) VALUES (
    3, 3, 'CZK', SYSDATE, 'admin'
);

-- Asset 4: Technology Partnership Investment
INSERT INTO KP_KTG_FINANCNIINVESTICE (
    ID, ID_KTGSPOLECNOST, S_MENA, DT_ZMENA, S_UZIVATEL, S_ISIN
) VALUES (
    4, 2, 'USD', SYSDATE, 'admin', 'US1234567890'
);

-- Asset 5: Innovation Fund Investment
INSERT INTO KP_KTG_FINANCNIINVESTICE (
    ID, ID_KTGSPOLECNOST, S_MENA, DT_ZMENA, S_UZIVATEL
) VALUES (
    5, 2, 'EUR', SYSDATE, 'admin'
);

COMMIT;

-- ============================================================================
-- VERIFICATION QUERIES
-- ============================================================================

SET SERVEROUTPUT ON;
BEGIN
    DBMS_OUTPUT.PUT_LINE('=== Test Data Summary ===');
    DBMS_OUTPUT.PUT_LINE('Projects: ' || (SELECT COUNT(*) FROM KP_KTG_PROJEKT));
    DBMS_OUTPUT.PUT_LINE('Budgets: ' || (SELECT COUNT(*) FROM KP_DAT_BUDGET));
    DBMS_OUTPUT.PUT_LINE('Documents: ' || (SELECT COUNT(*) FROM KP_DAT_DOKUMENT));
    DBMS_OUTPUT.PUT_LINE('Financial Investments: ' || (SELECT COUNT(*) FROM KP_KTG_FINANCNIINVESTICE));
    DBMS_OUTPUT.PUT_LINE('Companies: ' || (SELECT COUNT(*) FROM KP_KTG_SPOLECNOST));
    DBMS_OUTPUT.PUT_LINE('Departments: ' || (SELECT COUNT(*) FROM KP_KTG_ODBOR));
END;
/

EXIT;
