-- ============================================================================
-- Clean Schema Script
-- Drops all objects from schema to allow clean redeployment
-- ============================================================================

SET SERVEROUTPUT ON SIZE UNLIMITED

PROMPT Dropping all objects from current schema...

BEGIN
    -- Drop tables with CASCADE CONSTRAINTS
    FOR t IN (SELECT table_name FROM user_tables) LOOP
        BEGIN
            EXECUTE IMMEDIATE 'DROP TABLE ' || t.table_name || ' CASCADE CONSTRAINTS PURGE';
            DBMS_OUTPUT.PUT_LINE('Dropped table: ' || t.table_name);
        EXCEPTION
            WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE('Error dropping table ' || t.table_name || ': ' || SQLERRM);
        END;
    END LOOP;

    -- Drop views
    FOR v IN (SELECT view_name FROM user_views) LOOP
        BEGIN
            EXECUTE IMMEDIATE 'DROP VIEW ' || v.view_name;
            DBMS_OUTPUT.PUT_LINE('Dropped view: ' || v.view_name);
        EXCEPTION
            WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE('Error dropping view ' || v.view_name || ': ' || SQLERRM);
        END;
    END LOOP;

    -- Drop materialized views
    FOR mv IN (SELECT mview_name FROM user_mviews) LOOP
        BEGIN
            EXECUTE IMMEDIATE 'DROP MATERIALIZED VIEW ' || mv.mview_name;
            DBMS_OUTPUT.PUT_LINE('Dropped materialized view: ' || mv.mview_name);
        EXCEPTION
            WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE('Error dropping mview ' || mv.mview_name || ': ' || SQLERRM);
        END;
    END LOOP;

    -- Drop sequences
    FOR s IN (SELECT sequence_name FROM user_sequences) LOOP
        BEGIN
            EXECUTE IMMEDIATE 'DROP SEQUENCE ' || s.sequence_name;
            DBMS_OUTPUT.PUT_LINE('Dropped sequence: ' || s.sequence_name);
        EXCEPTION
            WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE('Error dropping sequence ' || s.sequence_name || ': ' || SQLERRM);
        END;
    END LOOP;

    -- Drop package bodies first
    FOR p IN (SELECT object_name FROM user_objects WHERE object_type = 'PACKAGE BODY') LOOP
        BEGIN
            EXECUTE IMMEDIATE 'DROP PACKAGE BODY ' || p.object_name;
            DBMS_OUTPUT.PUT_LINE('Dropped package body: ' || p.object_name);
        EXCEPTION
            WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE('Error dropping package body ' || p.object_name || ': ' || SQLERRM);
        END;
    END LOOP;

    -- Drop packages
    FOR p IN (SELECT object_name FROM user_objects WHERE object_type = 'PACKAGE') LOOP
        BEGIN
            EXECUTE IMMEDIATE 'DROP PACKAGE ' || p.object_name;
            DBMS_OUTPUT.PUT_LINE('Dropped package: ' || p.object_name);
        EXCEPTION
            WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE('Error dropping package ' || p.object_name || ': ' || SQLERRM);
        END;
    END LOOP;

    -- Drop functions
    FOR f IN (SELECT object_name FROM user_objects WHERE object_type = 'FUNCTION') LOOP
        BEGIN
            EXECUTE IMMEDIATE 'DROP FUNCTION ' || f.object_name;
            DBMS_OUTPUT.PUT_LINE('Dropped function: ' || f.object_name);
        EXCEPTION
            WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE('Error dropping function ' || f.object_name || ': ' || SQLERRM);
        END;
    END LOOP;

    -- Drop procedures
    FOR p IN (SELECT object_name FROM user_objects WHERE object_type = 'PROCEDURE') LOOP
        BEGIN
            EXECUTE IMMEDIATE 'DROP PROCEDURE ' || p.object_name;
            DBMS_OUTPUT.PUT_LINE('Dropped procedure: ' || p.object_name);
        EXCEPTION
            WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE('Error dropping procedure ' || p.object_name || ': ' || SQLERRM);
        END;
    END LOOP;

    -- Drop triggers
    FOR t IN (SELECT trigger_name FROM user_triggers) LOOP
        BEGIN
            EXECUTE IMMEDIATE 'DROP TRIGGER ' || t.trigger_name;
            DBMS_OUTPUT.PUT_LINE('Dropped trigger: ' || t.trigger_name);
        EXCEPTION
            WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE('Error dropping trigger ' || t.trigger_name || ': ' || SQLERRM);
        END;
    END LOOP;

    -- Drop type bodies
    FOR t IN (SELECT object_name FROM user_objects WHERE object_type = 'TYPE BODY') LOOP
        BEGIN
            EXECUTE IMMEDIATE 'DROP TYPE BODY ' || t.object_name;
            DBMS_OUTPUT.PUT_LINE('Dropped type body: ' || t.object_name);
        EXCEPTION
            WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE('Error dropping type body ' || t.object_name || ': ' || SQLERRM);
        END;
    END LOOP;

    -- Drop types
    FOR t IN (SELECT object_name FROM user_objects WHERE object_type = 'TYPE') LOOP
        BEGIN
            EXECUTE IMMEDIATE 'DROP TYPE ' || t.object_name || ' FORCE';
            DBMS_OUTPUT.PUT_LINE('Dropped type: ' || t.object_name);
        EXCEPTION
            WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE('Error dropping type ' || t.object_name || ': ' || SQLERRM);
        END;
    END LOOP;

    -- Drop synonyms
    FOR s IN (SELECT synonym_name FROM user_synonyms) LOOP
        BEGIN
            EXECUTE IMMEDIATE 'DROP SYNONYM ' || s.synonym_name;
            DBMS_OUTPUT.PUT_LINE('Dropped synonym: ' || s.synonym_name);
        EXCEPTION
            WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE('Error dropping synonym ' || s.synonym_name || ': ' || SQLERRM);
        END;
    END LOOP;

    DBMS_OUTPUT.PUT_LINE('');
    DBMS_OUTPUT.PUT_LINE('Schema cleanup completed.');
END;
/

-- Show remaining objects
SELECT object_type, COUNT(*) as count
FROM user_objects
GROUP BY object_type
ORDER BY object_type;

EXIT;
