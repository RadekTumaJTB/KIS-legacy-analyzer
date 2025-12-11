--------------------------------------------------------
-- SQL Script: Fix KP_KTG_PROJEKT Structure
-- Účel: Opravit neúplnou strukturu tabulky KP_KTG_PROJEKT
-- Datum: 2025-12-10
-- KRITICKÉ: Tabulka má pouze 22 sloupců místo 44!
--------------------------------------------------------

SET SERVEROUTPUT ON;

BEGIN
    DBMS_OUTPUT.PUT_LINE('=================================================');
    DBMS_OUTPUT.PUT_LINE('OPRAVA STRUKTURY KP_KTG_PROJEKT');
    DBMS_OUTPUT.PUT_LINE('=================================================');
END;
/

-- KROK 1: Backup současné tabulky
BEGIN
    DBMS_OUTPUT.PUT_LINE('Krok 1: Backup současné tabulky...');
    EXECUTE IMMEDIATE 'ALTER TABLE DB_JT.KP_KTG_PROJEKT RENAME TO KP_KTG_PROJEKT_OLD_20251210';
    DBMS_OUTPUT.PUT_LINE('✓ Backup vytvořen: KP_KTG_PROJEKT_OLD_20251210');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('✗ Backup selhal: ' || SQLERRM);
        RAISE;
END;
/

-- KROK 2: Vytvoření správné tabulky KP_KTG_PROJEKT
BEGIN
    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('Krok 2: Vytvoření správné struktury (44 sloupců)...');
END;
/

CREATE TABLE DB_JT.KP_KTG_PROJEKT
(
    ID                          NUMBER,
    S_NAZEV                     VARCHAR2(100 BYTE),
    S_CISLOOLD                  VARCHAR2(6 BYTE),
    ID_STATUS                   NUMBER,
    DT_DATUMSTATUSZMENA         DATE,
    ID_NAVRHUJE                 NUMBER,
    ID_SPONZOR                  NUMBER,
    ID_TOP                      NUMBER,
    ID_PMANAGER                 NUMBER,
    ID_KATEGORIE                NUMBER,
    ID_TYPKATEGORIE             NUMBER,
    ID_KTGUCETNISPOLECNOST      NUMBER,
    ND_NAKLADY                  NUMBER(15,2),
    S_MENANAKLADY               VARCHAR2(3 BYTE),
    ND_INVESTICE                NUMBER(15,2),
    S_MENAINVESTICE             VARCHAR2(3 BYTE),
    ND_OCENENI                  NUMBER(15,2),
    S_MENAOCENENI               VARCHAR2(3 BYTE),
    DT_STARTOCENENI             DATE,
    ID_FREKVENCE                NUMBER,
    DT_KONECOCENENI             DATE,
    S_POPIS                     VARCHAR2(250 BYTE),
    S_UZIVATEL                  VARCHAR2(20 BYTE),
    DT_PLATNOSTOD               DATE,
    DT_PLATNOSTDO               DATE,
    ND_DOCUROVEN1               NUMBER(15,2) DEFAULT 0,
    ID_PROJEKTNAVRH             NUMBER,
    DT_MEMORANDUMDALSI          DATE,
    NL_MEMORANDUMMESICU         NUMBER(*,0),
    ID_IFRSSEGMENT              NUMBER(*,0),
    ID_IFRSSUBSEGMENT           NUMBER(*,0),
    ID_MNGSEGMENT               NUMBER(*,0),
    ID_TYPBILANCE               NUMBER(*,0),
    ID_TYPBUDGETU               NUMBER(*,0),
    C_SLEDUJEBUDGET             CHAR(1 BYTE) DEFAULT '0',
    ND_BUDGETNAVYSENI_PM        NUMBER(15,2) DEFAULT 0,
    ND_BUDGETNAVYSENI_TOP       NUMBER(15,2) DEFAULT 1000,
    ID_MNGSEGMENTBOSS           NUMBER(*,0),
    C_POUZITMNGSEGMENT          CHAR(1 BYTE) DEFAULT '0'
) TABLESPACE USERS;

BEGIN
    DBMS_OUTPUT.PUT_LINE('✓ Tabulka vytvořena s 44 sloupci (dle originální DDL)');
END;
/

-- KROK 3: Primary key a constraints
BEGIN
    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('Krok 3: Vytvoření constraints...');
END;
/

ALTER TABLE DB_JT.KP_KTG_PROJEKT
ADD CONSTRAINT PK_KPKTGPROJEKT PRIMARY KEY (ID);

ALTER TABLE DB_JT.KP_KTG_PROJEKT
ADD CONSTRAINT NN_KPKTGPROJEKT_ID CHECK (ID IS NOT NULL);

ALTER TABLE DB_JT.KP_KTG_PROJEKT
ADD CONSTRAINT NN_KPKTGPROJEKT_NAZEV CHECK (S_NAZEV IS NOT NULL);

BEGIN
    DBMS_OUTPUT.PUT_LINE('✓ Constraints vytvořeny');
END;
/

-- KROK 4: Foreign keys
BEGIN
    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('Krok 4: Vytvoření foreign keys...');
END;
/

-- FK na KP_CIS_PROJEKTSTATUS
ALTER TABLE DB_JT.KP_KTG_PROJEKT
ADD CONSTRAINT FK_KTGPROJEKT_STATUS
FOREIGN KEY (ID_STATUS)
REFERENCES DB_JT.KP_CIS_PROJEKTSTATUS(ID);

-- FK na KP_CIS_PROJEKTFREKVENCE
ALTER TABLE DB_JT.KP_KTG_PROJEKT
ADD CONSTRAINT FK_KTGPROJEKT_FREKVENCE
FOREIGN KEY (ID_FREKVENCE)
REFERENCES DB_JT.KP_CIS_PROJEKTFREKVENCE(ID);

-- FK na KP_CIS_PROJEKTKATEGORIE
ALTER TABLE DB_JT.KP_KTG_PROJEKT
ADD CONSTRAINT FK_KTGPROJEKT_KATEGORIE
FOREIGN KEY (ID_KATEGORIE)
REFERENCES DB_JT.KP_CIS_PROJEKTKATEGORIE(ID);

-- FK na KP_CIS_MNGSEGMENT
ALTER TABLE DB_JT.KP_KTG_PROJEKT
ADD CONSTRAINT FK_KTGPROJEKT_MNGSEG
FOREIGN KEY (ID_MNGSEGMENT)
REFERENCES DB_JT.KP_CIS_MNGSEGMENT(ID);

-- FK na KP_CIS_TYPPROJEKTOVEBILANCE
ALTER TABLE DB_JT.KP_KTG_PROJEKT
ADD CONSTRAINT FK_KTGPROJEKT_TYPBILANCE
FOREIGN KEY (ID_TYPBILANCE)
REFERENCES DB_JT.KP_CIS_TYPPROJEKTOVEBILANCE(ID);

-- FK na KP_CIS_TYPBUDGETUPROJEKTU
ALTER TABLE DB_JT.KP_KTG_PROJEKT
ADD CONSTRAINT FK_KTGPROJEKT_TYPBUDGETU
FOREIGN KEY (ID_TYPBUDGETU)
REFERENCES DB_JT.KP_CIS_TYPBUDGETUPROJEKTU(ID);

-- FK na KP_KTG_PROJEKTNAVRH
ALTER TABLE DB_JT.KP_KTG_PROJEKT
ADD CONSTRAINT FK_KTGPROJEKT_NAVRH
FOREIGN KEY (ID_PROJEKTNAVRH)
REFERENCES DB_JT.KP_KTG_PROJEKTNAVRH(ID);

-- FK na KP_KTG_UCETNISPOLECNOST
BEGIN
    BEGIN
        EXECUTE IMMEDIATE 'ALTER TABLE DB_JT.KP_KTG_PROJEKT
            ADD CONSTRAINT FK_KTGPROJEKT_UCETSPOLEC
            FOREIGN KEY (ID_KTGUCETNISPOLECNOST)
            REFERENCES DB_JT.KP_KTG_UCETNISPOLECNOST(ID)';
        DBMS_OUTPUT.PUT_LINE('✓ FK_KTGPROJEKT_UCETSPOLEC vytvořen');
    EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE('⚠ FK_KTGPROJEKT_UCETSPOLEC: ' || SQLERRM);
    END;
END;
/

BEGIN
    DBMS_OUTPUT.PUT_LINE('✓ Foreign keys vytvořeny');
END;
/

-- KROK 5: Indexes
BEGIN
    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('Krok 5: Vytvoření indexes...');
END;
/

CREATE INDEX IDX_KTGPROJEKT_STATUS ON DB_JT.KP_KTG_PROJEKT(ID_STATUS);
CREATE INDEX IDX_KTGPROJEKT_PMANAGER ON DB_JT.KP_KTG_PROJEKT(ID_PMANAGER);
CREATE INDEX IDX_KTGPROJEKT_KATEGORIE ON DB_JT.KP_KTG_PROJEKT(ID_KATEGORIE);
CREATE INDEX IDX_KTGPROJEKT_NAVRH ON DB_JT.KP_KTG_PROJEKT(ID_PROJEKTNAVRH);

BEGIN
    DBMS_OUTPUT.PUT_LINE('✓ Indexes vytvořeny');
END;
/

-- KROK 6: Comments
BEGIN
    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('Krok 6: Přidání comments...');
END;
/

COMMENT ON TABLE DB_JT.KP_KTG_PROJEKT IS 'Katalog projektů - hlavní tabulka';
COMMENT ON COLUMN DB_JT.KP_KTG_PROJEKT.ID IS 'Primární klíč projektu';
COMMENT ON COLUMN DB_JT.KP_KTG_PROJEKT.S_NAZEV IS 'Název projektu';
COMMENT ON COLUMN DB_JT.KP_KTG_PROJEKT.S_CISLOOLD IS 'Staré číslo projektu';
COMMENT ON COLUMN DB_JT.KP_KTG_PROJEKT.ID_STATUS IS 'FK na KP_CIS_PROJEKTSTATUS';
COMMENT ON COLUMN DB_JT.KP_KTG_PROJEKT.DT_DATUMSTATUSZMENA IS 'Datum poslední změny statusu';
COMMENT ON COLUMN DB_JT.KP_KTG_PROJEKT.ID_NAVRHUJE IS 'ID osoby, která navrhla projekt';
COMMENT ON COLUMN DB_JT.KP_KTG_PROJEKT.ID_PMANAGER IS 'ID project managera';
COMMENT ON COLUMN DB_JT.KP_KTG_PROJEKT.NL_MEMORANDUMMESICU IS 'Počet měsíců pro memorandum';
COMMENT ON COLUMN DB_JT.KP_KTG_PROJEKT.C_SLEDUJEBUDGET IS '1 = sleduje rozpočet, 0 = nesleduje';

BEGIN
    DBMS_OUTPUT.PUT_LINE('✓ Comments přidány');
END;
/

-- KROK 7: Migrace dat ze staré tabulky
BEGIN
    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('Krok 7: Migrace dat ze staré tabulky...');
END;
/

-- Počet řádků ve staré tabulce
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM DB_JT.KP_KTG_PROJEKT_OLD_20251210;
    DBMS_OUTPUT.PUT_LINE('Počet záznamů ve staré tabulce: ' || v_count);
END;
/

-- Migrace dat (map old columns to new columns)
INSERT INTO DB_JT.KP_KTG_PROJEKT (
    ID,
    S_NAZEV,
    S_CISLOOLD,          -- Bylo S_CISLO
    ID_STATUS,
    ID_PMANAGER,
    ID_KATEGORIE,
    ID_MNGSEGMENT,
    ID_FREKVENCE,
    S_POPIS,
    ND_DOCUROVEN1,
    C_SLEDUJEBUDGET,
    ND_BUDGETNAVYSENI_PM,
    ND_BUDGETNAVYSENI_TOP,
    ID_TYPBILANCE,
    ID_TYPBUDGETU,
    S_UZIVATEL
)
SELECT
    ID,
    S_NAZEV,
    S_CISLO,             -- Přejmenování S_CISLO -> S_CISLOOLD
    ID_STATUS,
    ID_PMANAGER,
    ID_KATEGORIE,
    ID_MNGSEGMENT,
    ID_FREKVENCE,
    S_POPIS,
    ND_DOCUROVEN1,
    C_SLEDUJEBUDGET,
    ND_BUDGETNAVYSENI_PM,
    ND_BUDGETNAVYSENI_TOP,
    ID_TYPBILANCE,
    ID_TYPBUDGETU,
    S_UZIVATEL
FROM DB_JT.KP_KTG_PROJEKT_OLD_20251210;

-- Počet migrovaných řádků
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM DB_JT.KP_KTG_PROJEKT;
    DBMS_OUTPUT.PUT_LINE('✓ Migrováno ' || v_count || ' záznamů');
END;
/

COMMIT;

BEGIN
    DBMS_OUTPUT.PUT_LINE('✓ Data úspěšně migrována a commitnuta');
END;
/

-- KROK 8: Vytvoření sekvence (pokud neexistuje)
BEGIN
    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('Krok 8: Vytvoření sekvence SQ_KP_PROJEKT...');

    BEGIN
        EXECUTE IMMEDIATE 'CREATE SEQUENCE DB_JT.SQ_KP_PROJEKT
            START WITH 1000
            INCREMENT BY 1
            NOCACHE
            NOCYCLE';
        DBMS_OUTPUT.PUT_LINE('✓ Sekvence SQ_KP_PROJEKT vytvořena');
    EXCEPTION
        WHEN OTHERS THEN
            IF SQLCODE = -955 THEN -- ORA-00955: name is already used by an existing object
                DBMS_OUTPUT.PUT_LINE('✓ Sekvence SQ_KP_PROJEKT již existuje');
            ELSE
                DBMS_OUTPUT.PUT_LINE('✗ Chyba při vytváření sekvence: ' || SQLERRM);
                RAISE;
            END IF;
    END;
END;
/

-- KROK 9: Zkompilování KAP_PROJEKT package body
BEGIN
    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('Krok 9: Kompilace KAP_PROJEKT package body...');

    BEGIN
        EXECUTE IMMEDIATE 'ALTER PACKAGE DB_JT.KAP_PROJEKT COMPILE BODY';
        DBMS_OUTPUT.PUT_LINE('✓ KAP_PROJEKT package body zkompilován');
    EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE('✗ Kompilace selhala: ' || SQLERRM);
            DBMS_OUTPUT.PUT_LINE('   Pro detaily spusťte: SELECT * FROM user_errors WHERE name=''KAP_PROJEKT'' AND type=''PACKAGE BODY'';');
    END;
END;
/

-- KROK 10: Verifikace
BEGIN
    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('=================================================');
    DBMS_OUTPUT.PUT_LINE('VERIFIKACE');
    DBMS_OUTPUT.PUT_LINE('=================================================');
END;
/

-- Počet sloupců v nové tabulce
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM user_tab_columns
    WHERE table_name = 'KP_KTG_PROJEKT';
    DBMS_OUTPUT.PUT_LINE('Počet sloupců v KP_KTG_PROJEKT: ' || v_count || ' (očekáváno: 44)');
END;
/

-- Status package
SELECT object_name, object_type, status
FROM user_objects
WHERE object_name = 'KAP_PROJEKT'
ORDER BY object_type;

-- Počet záznamů
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM DB_JT.KP_KTG_PROJEKT;
    DBMS_OUTPUT.PUT_LINE('Počet záznamů v KP_KTG_PROJEKT: ' || v_count);
END;
/

BEGIN
    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('=================================================');
    DBMS_OUTPUT.PUT_LINE('OPRAVA DOKONČENA');
    DBMS_OUTPUT.PUT_LINE('=================================================');
    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('Další kroky:');
    DBMS_OUTPUT.PUT_LINE('1. Zkontrolujte status KAP_PROJEKT package (měl by být VALID)');
    DBMS_OUTPUT.PUT_LINE('2. Otestujte volání procedury p_KpProjekt');
    DBMS_OUTPUT.PUT_LINE('3. Aktualizujte ProjectService.java s opravenými parametry');
    DBMS_OUTPUT.PUT_LINE('4. Rozšiřte ProjectFormData.java o chybějící fields');
    DBMS_OUTPUT.PUT_LINE('5. Updatujte frontend formuláře');
END;
/
