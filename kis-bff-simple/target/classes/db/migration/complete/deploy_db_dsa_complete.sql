-- ============================================================================
-- Oracle Database Deployment Script
-- Schema: DB_DSA
-- Generated: 2025-12-10
-- Target: Oracle 23ai Free (Docker)
-- Connection: DB_DSA/kis_db_dsa_2024@//localhost:1521/FREEPDB1
-- ============================================================================

SET SERVEROUTPUT ON SIZE UNLIMITED
SET VERIFY OFF
SET FEEDBACK ON
SET ECHO ON

WHENEVER SQLERROR CONTINUE

-- Enable output buffering
ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD HH24:MI:SS';

PROMPT ============================================================================
PROMPT Starting DB_DSA Schema Deployment
PROMPT ============================================================================
PROMPT


-- ============================================================================
-- PHASE: Deploy SEQUENCES (11 files)
-- ============================================================================

PROMPT
PROMPT ============================================================================
PROMPT Deploying SEQUENCES (11 files)
PROMPT ============================================================================
PROMPT


-- ============================================================================
-- Deploy: PLSQL_PROFILER_RUNNUMBER.sql
-- Category: sequences
-- ============================================================================

PROMPT Deploying PLSQL_PROFILER_RUNNUMBER.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Sequence PLSQL_PROFILER_RUNNUMBER
--------------------------------------------------------

   CREATE SEQUENCE  "DB_DSA"."PLSQL_PROFILER_RUNNUMBER"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 12 NOCACHE  NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PLSQL_PROFILER_RUNNUMBER.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PLSQL_PROFILER_RUNNUMBER.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PLSQL_PROFILER_RUNNUMBER.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: SQ_KP_DAT_DOKLADAGRTYPNASTCP.sql
-- Category: sequences
-- ============================================================================

PROMPT Deploying SQ_KP_DAT_DOKLADAGRTYPNASTCP.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Sequence SQ_KP_DAT_DOKLADAGRTYPNASTCP
--------------------------------------------------------

   CREATE SEQUENCE  "DB_DSA"."SQ_KP_DAT_DOKLADAGRTYPNASTCP"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: SQ_KP_DAT_DOKLADAGRTYPNASTCP.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: SQ_KP_DAT_DOKLADAGRTYPNASTCP.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: SQ_KP_DAT_DOKLADAGRTYPNASTCP.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: SQ_KP_DOKLAD.sql
-- Category: sequences
-- ============================================================================

PROMPT Deploying SQ_KP_DOKLAD.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Sequence SQ_KP_DOKLAD
--------------------------------------------------------

   CREATE SEQUENCE  "DB_DSA"."SQ_KP_DOKLAD"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: SQ_KP_DOKLAD.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: SQ_KP_DOKLAD.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: SQ_KP_DOKLAD.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: SQ_KP_KTG_PROTISTRANAVYJIMKY.sql
-- Category: sequences
-- ============================================================================

PROMPT Deploying SQ_KP_KTG_PROTISTRANAVYJIMKY.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Sequence SQ_KP_KTG_PROTISTRANAVYJIMKY
--------------------------------------------------------

   CREATE SEQUENCE  "DB_DSA"."SQ_KP_KTG_PROTISTRANAVYJIMKY"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 15483 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: SQ_KP_KTG_PROTISTRANAVYJIMKY.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: SQ_KP_KTG_PROTISTRANAVYJIMKY.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: SQ_KP_KTG_PROTISTRANAVYJIMKY.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: SQ_KP_KTG_SPOLECNOST.sql
-- Category: sequences
-- ============================================================================

PROMPT Deploying SQ_KP_KTG_SPOLECNOST.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Sequence SQ_KP_KTG_SPOLECNOST
--------------------------------------------------------

   CREATE SEQUENCE  "DB_DSA"."SQ_KP_KTG_SPOLECNOST"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1216739 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: SQ_KP_KTG_SPOLECNOST.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: SQ_KP_KTG_SPOLECNOST.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: SQ_KP_KTG_SPOLECNOST.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: SQ_KP_MANUALTEXT.sql
-- Category: sequences
-- ============================================================================

PROMPT Deploying SQ_KP_MANUALTEXT.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Sequence SQ_KP_MANUALTEXT
--------------------------------------------------------

   CREATE SEQUENCE  "DB_DSA"."SQ_KP_MANUALTEXT"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 113899 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: SQ_KP_MANUALTEXT.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: SQ_KP_MANUALTEXT.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: SQ_KP_MANUALTEXT.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: SQ_KP_REL_PROTISTRANA.sql
-- Category: sequences
-- ============================================================================

PROMPT Deploying SQ_KP_REL_PROTISTRANA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Sequence SQ_KP_REL_PROTISTRANA
--------------------------------------------------------

   CREATE SEQUENCE  "DB_DSA"."SQ_KP_REL_PROTISTRANA"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 34186096 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: SQ_KP_REL_PROTISTRANA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: SQ_KP_REL_PROTISTRANA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: SQ_KP_REL_PROTISTRANA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: SQ_LOG_CRMESS.sql
-- Category: sequences
-- ============================================================================

PROMPT Deploying SQ_LOG_CRMESS.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Sequence SQ_LOG_CRMESS
--------------------------------------------------------

   CREATE SEQUENCE  "DB_DSA"."SQ_LOG_CRMESS"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1424 NOCACHE  NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: SQ_LOG_CRMESS.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: SQ_LOG_CRMESS.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: SQ_LOG_CRMESS.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: SQ_LOG_QCZ.sql
-- Category: sequences
-- ============================================================================

PROMPT Deploying SQ_LOG_QCZ.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Sequence SQ_LOG_QCZ
--------------------------------------------------------

   CREATE SEQUENCE  "DB_DSA"."SQ_LOG_QCZ"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 2186 NOCACHE  NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: SQ_LOG_QCZ.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: SQ_LOG_QCZ.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: SQ_LOG_QCZ.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: SQ_Q_REP_KONTROLA.sql
-- Category: sequences
-- ============================================================================

PROMPT Deploying SQ_Q_REP_KONTROLA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Sequence SQ_Q_REP_KONTROLA
--------------------------------------------------------

   CREATE SEQUENCE  "DB_DSA"."SQ_Q_REP_KONTROLA"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 775375 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: SQ_Q_REP_KONTROLA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: SQ_Q_REP_KONTROLA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: SQ_Q_REP_KONTROLA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: SQ_SEP_PROBLEM_BILANCE.sql
-- Category: sequences
-- ============================================================================

PROMPT Deploying SQ_SEP_PROBLEM_BILANCE.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Sequence SQ_SEP_PROBLEM_BILANCE
--------------------------------------------------------

   CREATE SEQUENCE  "DB_DSA"."SQ_SEP_PROBLEM_BILANCE"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 9803840 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: SQ_SEP_PROBLEM_BILANCE.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: SQ_SEP_PROBLEM_BILANCE.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: SQ_SEP_PROBLEM_BILANCE.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


PROMPT Completed sequences deployment
PROMPT


-- ============================================================================
-- PHASE: Deploy TABLES (44 files)
-- ============================================================================

PROMPT
PROMPT ============================================================================
PROMPT Deploying TABLES (44 files)
PROMPT ============================================================================
PROMPT


-- ============================================================================
-- Deploy: ADMIN_MAIL_NOTIFICATION.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying ADMIN_MAIL_NOTIFICATION.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table ADMIN_MAIL_NOTIFICATION
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."ADMIN_MAIL_NOTIFICATION" 
   (	"USER_ID" VARCHAR2(30 BYTE), 
	"EMAIL" VARCHAR2(100 BYTE), 
	"LOADPROTISTRANY" CHAR(1 BYTE), 
	"LOADCRM" CHAR(1 BYTE), 
	"ESS_ZMENY_CRM" CHAR(1 BYTE), 
	"ESS_ZMENY_QCZ" CHAR(1 BYTE), 
	"AKTIVNI" CHAR(1 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: ADMIN_MAIL_NOTIFICATION.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: ADMIN_MAIL_NOTIFICATION.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: ADMIN_MAIL_NOTIFICATION.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: BCK_KP_S_TOPAS_EMITENTI.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying BCK_KP_S_TOPAS_EMITENTI.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table BCK_KP_S_TOPAS_EMITENTI
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."BCK_KP_S_TOPAS_EMITENTI" 
   (	"S_DBLINK" VARCHAR2(30 BYTE), 
	"EID" NUMBER(*,0), 
	"ICO" VARCHAR2(10 BYTE), 
	"NAZEV" VARCHAR2(56 BYTE), 
	"STAT" VARCHAR2(3 BYTE), 
	"ULICE" VARCHAR2(32 BYTE), 
	"PSC" VARCHAR2(6 BYTE), 
	"OBEC" VARCHAR2(40 BYTE), 
	"WWW" VARCHAR2(100 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: BCK_KP_S_TOPAS_EMITENTI.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: BCK_KP_S_TOPAS_EMITENTI.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: BCK_KP_S_TOPAS_EMITENTI.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_CIS_RKC_INVALID_ICO_QUA.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_CIS_RKC_INVALID_ICO_QUA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_CIS_RKC_INVALID_ICO_QUA
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_CIS_RKC_INVALID_ICO_QUA" 
   (	"FIKTIVNI_NUMBER0" VARCHAR2(20 BYTE), 
	"INVALID_RKC_ICO" VARCHAR2(35 BYTE), 
	"RKC_REQUESTED_NAME" VARCHAR2(80 BYTE), 
	"CREATE_DATE" DATE, 
	"CREATE_USER" VARCHAR2(50 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_CIS_RKC_INVALID_ICO_QUA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_CIS_RKC_INVALID_ICO_QUA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_CIS_RKC_INVALID_ICO_QUA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_KTG_PROTISTRANAVYJIMKY.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_KTG_PROTISTRANAVYJIMKY.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_KTG_PROTISTRANAVYJIMKY
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_KTG_PROTISTRANAVYJIMKY" 
   (	"ID" NUMBER(*,0), 
	"ID_DATABAZE" NUMBER(*,0), 
	"S_UCETMASKA" VARCHAR2(10 BYTE), 
	"S_PROTISTRANA" VARCHAR2(10 BYTE), 
	"NL_POZICEOD" NUMBER(*,0) DEFAULT 1
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_KTG_PROTISTRANAVYJIMKY.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_KTG_PROTISTRANAVYJIMKY.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_KTG_PROTISTRANAVYJIMKY.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_LOG_CRM_CUST_GROUP_ESS.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_LOG_CRM_CUST_GROUP_ESS.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_LOG_CRM_CUST_GROUP_ESS
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_LOG_CRM_CUST_GROUP_ESS" 
   (	"ID_LOG" NUMBER(38,0), 
	"DT_LOG" DATE, 
	"CUST_GROUP_NAME" VARCHAR2(100 BYTE), 
	"CUST_GROUP_CODE" VARCHAR2(8 BYTE), 
	"CUST_STRUCT_NAME" VARCHAR2(31 BYTE), 
	"CUST_STRUCT_CODE" VARCHAR2(3 BYTE), 
	"DT_PLATNOSTOD" DATE, 
	"DT_PLATNOSTDO" DATE, 
	"KIS_VALID" CHAR(1 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_LOG_CRM_CUST_GROUP_ESS.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_LOG_CRM_CUST_GROUP_ESS.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_LOG_CRM_CUST_GROUP_ESS.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_LOG_CRM_CUST_GROUP_MEM_ESS.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_LOG_CRM_CUST_GROUP_MEM_ESS.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_LOG_CRM_CUST_GROUP_MEM_ESS
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_LOG_CRM_CUST_GROUP_MEM_ESS" 
   (	"ID_LOG" NUMBER(38,0), 
	"DT_LOG" DATE, 
	"CUST_GROUP_NAME" VARCHAR2(100 BYTE), 
	"CUST_GROUP_CODE" VARCHAR2(8 BYTE), 
	"MEMBER_NAME" VARCHAR2(160 BYTE), 
	"MEM_ICO" VARCHAR2(100 BYTE), 
	"MEM_KLID" VARCHAR2(10 BYTE), 
	"MEM_NUMBER0_QCZ" VARCHAR2(10 BYTE), 
	"MEM_NUMBER0_QSK" VARCHAR2(10 BYTE), 
	"DT_PLATNOSTOD" DATE, 
	"DT_PLATNOSTDO" DATE, 
	"KIS_VALID" CHAR(1 BYTE), 
	"KIS_ID" NUMBER(38,0)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_LOG_CRM_CUST_GROUP_MEM_ESS.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_LOG_CRM_CUST_GROUP_MEM_ESS.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_LOG_CRM_CUST_GROUP_MEM_ESS.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_LOG_PROTISTRANA.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_LOG_PROTISTRANA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_LOG_PROTISTRANA
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_LOG_PROTISTRANA" 
   (	"ID" NUMBER(*,0), 
	"ID_DATABAZE" NUMBER(*,0), 
	"ID_SPOLECNOST" NUMBER(*,0), 
	"S_ICO" VARCHAR2(35 BYTE), 
	"S_NAZEV" VARCHAR2(200 BYTE), 
	"S_NUMBER0" VARCHAR2(20 BYTE), 
	"FIRMA_ID" NUMBER(*,0), 
	"EO_ID" NUMBER(*,0), 
	"BANKA_ID" NUMBER(*,0), 
	"DOID" VARCHAR2(10 BYTE), 
	"KID" NUMBER(*,0), 
	"PLATINST" VARCHAR2(10 BYTE), 
	"AKCE" CHAR(1 BYTE), 
	"DT_ZMENA" DATE, 
	"S_POM_ICO" VARCHAR2(20 BYTE), 
	"EID" NUMBER(*,0), 
	"DATUMZALOZENI" DATE, 
	"ULICE" VARCHAR2(40 BYTE), 
	"PSC" VARCHAR2(10 BYTE), 
	"MESTO" VARCHAR2(40 BYTE), 
	"STAT" VARCHAR2(3 BYTE), 
	"DIC" VARCHAR2(15 BYTE), 
	"UCETSCP" VARCHAR2(12 BYTE), 
	"REGRMS" VARCHAR2(9 BYTE), 
	"EKONOM_SEKTOR" VARCHAR2(10 BYTE), 
	"TYPOSOBY" CHAR(1 BYTE), 
	"S_AKCE" CHAR(1 BYTE), 
	"DT_DATUMZMENY" DATE DEFAULT SYSDATE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_LOG_PROTISTRANA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_LOG_PROTISTRANA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_LOG_PROTISTRANA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_LOG_QUAESTOR_CUST_GROUP.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_LOG_QUAESTOR_CUST_GROUP.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_LOG_QUAESTOR_CUST_GROUP
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_LOG_QUAESTOR_CUST_GROUP" 
   (	"ID_LOG" NUMBER(38,0), 
	"DT_LOG" DATE, 
	"NUMBER0" VARCHAR2(20 BYTE), 
	"CUST_GROUP_NAME" VARCHAR2(35 BYTE), 
	"CUST_GROUP_CODE" VARCHAR2(5 BYTE), 
	"CUST_STRUC_NAME" VARCHAR2(35 BYTE), 
	"CUST_STRUC_CODE" VARCHAR2(3 BYTE), 
	"MAIN_INDICATOR" CHAR(1 BYTE), 
	"S_DBLINK" VARCHAR2(30 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_LOG_QUAESTOR_CUST_GROUP.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_LOG_QUAESTOR_CUST_GROUP.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_LOG_QUAESTOR_CUST_GROUP.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_LOG_QUAESTOR_RELATION.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_LOG_QUAESTOR_RELATION.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_LOG_QUAESTOR_RELATION
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_LOG_QUAESTOR_RELATION" 
   (	"ID_LOG" NUMBER(38,0), 
	"DT_LOG" DATE, 
	"NUMBER0" VARCHAR2(20 BYTE), 
	"SEARCH_NAME" VARCHAR2(80 BYTE), 
	"SEARCH_IDENTIFICAT" VARCHAR2(35 BYTE), 
	"DATE_UPDATE" DATE, 
	"POM_ICO" VARCHAR2(20 BYTE), 
	"FK_RESIDE_COUNTRY" VARCHAR2(3 BYTE), 
	"INDICATOR_PC" CHAR(1 BYTE), 
	"ENONOM_SEKTOR" VARCHAR2(10 BYTE), 
	"KATEGORIEKOEF" NUMBER(6,3), 
	"OKEC" VARCHAR2(50 BYTE), 
	"DATE_OF_BIRTH_OR_F" DATE, 
	"STREET" VARCHAR2(35 BYTE), 
	"HOUSE_NUMBER_COMPL" VARCHAR2(12 BYTE), 
	"POSTCODE" VARCHAR2(9 BYTE), 
	"CITY" VARCHAR2(35 BYTE), 
	"S_DBLINK" VARCHAR2(30 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_LOG_QUAESTOR_RELATION.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_LOG_QUAESTOR_RELATION.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_LOG_QUAESTOR_RELATION.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_RADICIMPORTU.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_RADICIMPORTU.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_RADICIMPORTU
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_RADICIMPORTU" 
   (	"ID_FRONTA" NUMBER(*,0), 
	"S_DBLINK" VARCHAR2(32 BYTE), 
	"C_STAV" CHAR(1 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_RADICIMPORTU.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_RADICIMPORTU.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_RADICIMPORTU.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_REL_PROTISTRANA.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_REL_PROTISTRANA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_REL_PROTISTRANA
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_REL_PROTISTRANA" 
   (	"ID" NUMBER(*,0), 
	"ID_DATABAZE" NUMBER(*,0), 
	"ID_SPOLECNOST" NUMBER(*,0), 
	"S_ICO" VARCHAR2(35 BYTE), 
	"S_NAZEV" VARCHAR2(200 BYTE), 
	"S_NUMBER0" VARCHAR2(20 BYTE), 
	"FIRMA_ID" NUMBER(*,0), 
	"EO_ID" NUMBER(*,0), 
	"BANKA_ID" NUMBER(*,0), 
	"DOID" VARCHAR2(10 BYTE), 
	"KID" NUMBER(*,0), 
	"PLATINST" VARCHAR2(10 BYTE), 
	"AKCE" CHAR(1 BYTE), 
	"DT_ZMENA" DATE, 
	"S_POM_ICO" VARCHAR2(20 BYTE), 
	"EID" NUMBER(*,0), 
	"DATUMZALOZENI" DATE, 
	"ULICE" VARCHAR2(40 BYTE), 
	"PSC" VARCHAR2(10 BYTE), 
	"MESTO" VARCHAR2(40 BYTE), 
	"STAT" VARCHAR2(3 BYTE), 
	"DIC" VARCHAR2(15 BYTE), 
	"UCETSCP" VARCHAR2(12 BYTE), 
	"REGRMS" VARCHAR2(9 BYTE), 
	"EKONOM_SEKTOR" VARCHAR2(10 BYTE), 
	"TYPOSOBY" CHAR(1 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 1703936 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA" ;

   COMMENT ON COLUMN "DB_DSA"."KP_REL_PROTISTRANA"."FIRMA_ID" IS 'obsahuje SEP_ID'
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_REL_PROTISTRANA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_REL_PROTISTRANA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_REL_PROTISTRANA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_CRM_CUST_GROUP_ESS.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_S_CRM_CUST_GROUP_ESS.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_S_CRM_CUST_GROUP_ESS
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_S_CRM_CUST_GROUP_ESS" 
   (	"CUST_GROUP_NAME" NVARCHAR2(100), 
	"CUST_GROUP_CODE" NVARCHAR2(8), 
	"CUST_STRUCT_NAME" VARCHAR2(31 BYTE), 
	"CUST_STRUCT_CODE" VARCHAR2(3 BYTE), 
	"DT_PLATNOSTOD" DATE, 
	"DT_PLATNOSTDO" DATE, 
	"KIS_VALID" CHAR(1 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_CRM_CUST_GROUP_ESS.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_CRM_CUST_GROUP_ESS.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_CRM_CUST_GROUP_ESS.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_CRM_CUST_GROUP_MEM_ESS.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_S_CRM_CUST_GROUP_MEM_ESS.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_S_CRM_CUST_GROUP_MEM_ESS
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_S_CRM_CUST_GROUP_MEM_ESS" 
   (	"CUST_GROUP_NAME" NVARCHAR2(100), 
	"CUST_GROUP_CODE" NVARCHAR2(8), 
	"MEMBER_NAME" NVARCHAR2(160), 
	"MEM_ICO" NVARCHAR2(100), 
	"MEM_KLID" VARCHAR2(10 BYTE), 
	"MEM_NUMBER0_QCZ" VARCHAR2(10 BYTE), 
	"MEM_NUMBER0_QSK" VARCHAR2(10 BYTE), 
	"DT_PLATNOSTOD" DATE, 
	"DT_PLATNOSTDO" DATE, 
	"KIS_VALID" CHAR(1 BYTE), 
	"KIS_ID" NUMBER(38,0)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_CRM_CUST_GROUP_MEM_ESS.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_CRM_CUST_GROUP_MEM_ESS.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_CRM_CUST_GROUP_MEM_ESS.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_MANUAL.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_S_MANUAL.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_S_MANUAL
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_S_MANUAL" 
   (	"S_DBLINK" VARCHAR2(30 BYTE), 
	"S_EXTID" VARCHAR2(20 BYTE), 
	"S_NAZEV" VARCHAR2(256 BYTE), 
	"ID_KTGSPOLECNOST" NUMBER(*,0), 
	"ID_KTGSPOLECNOSTAUTO" NUMBER(*,0), 
	"DT_DATUM" DATE, 
	"S_UZIVATEL" VARCHAR2(30 BYTE), 
	 CONSTRAINT "PK_KPSMANUAL" PRIMARY KEY ("S_DBLINK", "S_EXTID") ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  ORGANIZATION INDEX NOCOMPRESS PCTFREE 10 INITRANS 2 MAXTRANS 255 LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA" 
 PCTTHRESHOLD 50;

   COMMENT ON COLUMN "DB_DSA"."KP_S_MANUAL"."S_DBLINK" IS 'Rozlisuje kazdou externe uctovanou spolecnost - tak aby se nekrizili pripadna externi ID protistran, protoze jsou naprosto nezavisla. Neni to vazba na nasi tabulku databazovych linku.';
   COMMENT ON COLUMN "DB_DSA"."KP_S_MANUAL"."S_EXTID" IS 'Unikatni identifikace protistrany v ucetnim systemu manualne uctovane spolecnosti.';
   COMMENT ON COLUMN "DB_DSA"."KP_S_MANUAL"."S_NAZEV" IS 'Nazev protistrany';
   COMMENT ON COLUMN "DB_DSA"."KP_S_MANUAL"."ID_KTGSPOLECNOST" IS 'Odkaz na nasi KIS spolecnost s kterou uzivatel tuto protistranu sparoval';
   COMMENT ON COLUMN "DB_DSA"."KP_S_MANUAL"."ID_KTGSPOLECNOSTAUTO" IS 'Odkaz na nasi KIS spolecnost, ktera byla zalozena automaticky, pri prvnim importu. Casem by melo dochazet k tomu, ze protistranu zalozi ''lepe'' primo v ucetnich systemech a bude se preparovavat. Zde pak zustane jenom historicky zaznam, ktery bude slouzit ke zruseni teto puvodni automaticky zalozene protistrany.';
   COMMENT ON COLUMN "DB_DSA"."KP_S_MANUAL"."DT_DATUM" IS 'Datum parovani.';
   COMMENT ON COLUMN "DB_DSA"."KP_S_MANUAL"."S_UZIVATEL" IS 'Kdo sparoval';
   COMMENT ON TABLE "DB_DSA"."KP_S_MANUAL"  IS 'Slouzi pro import protistran u manualne (externe) uctovanych spolecnosti. Externi spolecnosti puvodne dodavali primo doklad, ale z toho neslo delat uverovou/menovou pozici. Nove tedy dodavaji i tyto pozice a pouzivaji v nich svoje protistrany s jejich identifikaci, kterou KIS nezna. Tato tabulka slouzi k mapovani externich protistran do KIS unifikovanych protistran.'
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_MANUAL.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_MANUAL.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_MANUAL.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_MANUALTEXT.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_S_MANUALTEXT.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_S_MANUALTEXT
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_S_MANUALTEXT" 
   (	"ID" NUMBER(*,0), 
	"S_DBLINK" VARCHAR2(30 BYTE), 
	"S_TEXT" VARCHAR2(256 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_MANUALTEXT.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_MANUALTEXT.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_MANUALTEXT.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_QUAESTOR_CUST_GROUP.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_S_QUAESTOR_CUST_GROUP.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_S_QUAESTOR_CUST_GROUP
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_S_QUAESTOR_CUST_GROUP" 
   (	"NUMBER0" VARCHAR2(20 BYTE), 
	"CUST_GROUP_NAME" VARCHAR2(35 BYTE), 
	"CUST_GROUP_CODE" VARCHAR2(5 BYTE), 
	"CUST_STRUC_NAME" VARCHAR2(35 BYTE), 
	"CUST_STRUC_CODE" VARCHAR2(3 BYTE), 
	"MAIN_INDICATOR" CHAR(1 BYTE), 
	"S_DBLINK" VARCHAR2(30 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_QUAESTOR_CUST_GROUP.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_QUAESTOR_CUST_GROUP.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_QUAESTOR_CUST_GROUP.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_QUAESTOR_RELATION.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_S_QUAESTOR_RELATION.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_S_QUAESTOR_RELATION
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_S_QUAESTOR_RELATION" 
   (	"NUMBER0" VARCHAR2(20 BYTE), 
	"SEARCH_NAME" VARCHAR2(80 BYTE), 
	"SEARCH_IDENTIFICAT" VARCHAR2(35 BYTE), 
	"DATE_UPDATE" DATE, 
	"POM_ICO" VARCHAR2(20 BYTE), 
	"FK_RESIDE_COUNTRY" VARCHAR2(3 BYTE), 
	"INDICATOR_PC" CHAR(1 BYTE), 
	"ENONOM_SEKTOR" VARCHAR2(10 BYTE), 
	"KATEGORIEKOEF" NUMBER(6,3), 
	"OKEC" VARCHAR2(50 BYTE), 
	"DATE_OF_BIRTH_OR_F" DATE, 
	"STREET" VARCHAR2(35 BYTE), 
	"HOUSE_NUMBER_COMPL" VARCHAR2(12 BYTE), 
	"POSTCODE" VARCHAR2(9 BYTE), 
	"CITY" VARCHAR2(35 BYTE), 
	"S_DBLINK" VARCHAR2(30 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 393216 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_QUAESTOR_RELATION.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_QUAESTOR_RELATION.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_QUAESTOR_RELATION.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_SEP.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_S_SEP.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_S_SEP
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_S_SEP" 
   (	"SEP_ID" NUMBER(*,0), 
	"ICO" VARCHAR2(20 BYTE), 
	"RC" VARCHAR2(20 BYTE), 
	"NAZEV" VARCHAR2(128 BYTE), 
	"ID_KTGSPOLECNOST" NUMBER(*,0), 
	"COUNTRY" CHAR(2 BYTE), 
	"FLAG_IGNOROVAT" CHAR(1 BYTE) DEFAULT '0', 
	"POMCOUNT" NUMBER(*,0)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA" ;

   COMMENT ON COLUMN "DB_DSA"."KP_S_SEP"."SEP_ID" IS 'unikatni ID ze systemu SEP';
   COMMENT ON COLUMN "DB_DSA"."KP_S_SEP"."ICO" IS 'ico subjektu, ktere pouzijeme pri parovani, pokud uz neprijdou parovaci udaje primo ze SEP (id_ktgSpolecnost)';
   COMMENT ON COLUMN "DB_DSA"."KP_S_SEP"."NAZEV" IS 'pouze pro informaci a lepsi praci uzivatelu';
   COMMENT ON COLUMN "DB_DSA"."KP_S_SEP"."ID_KTGSPOLECNOST" IS 'stare zaznamy maji v SEP ulozeno KIS id - nove vznikle tam mit nebudou a budou se parovat pres ICO';
   COMMENT ON COLUMN "DB_DSA"."KP_S_SEP"."FLAG_IGNOROVAT" IS 'kdyz mi prijdou duplicity na Ico nebo RC tak si to pred zpracovanim oznacim na ''1'' a pak je dal ignoruji v procesu slucovani';
   COMMENT ON COLUMN "DB_DSA"."KP_S_SEP"."POMCOUNT" IS 'pocet stejnych zaznamu pri parovani podle IC a Country ... kdyz je tu vetsi jak 1 tak je to problem, ze existuje vice moznosti a neumim se rozhodnout k jake id_ktg_spolecnosti priradit';
   COMMENT ON TABLE "DB_DSA"."KP_S_SEP"  IS 'importovane spolecnosti ze systemu SEP'
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_SEP.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_SEP.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_SEP.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_SEP_PROBLEM_BILANCE.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_S_SEP_PROBLEM_BILANCE.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_S_SEP_PROBLEM_BILANCE
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_S_SEP_PROBLEM_BILANCE" 
   (	"ID" NUMBER, 
	"ID_DATABAZE" NUMBER(38,0), 
	"SEP_ID" NUMBER(38,0), 
	"ID_DOKLAD" NUMBER(38,0), 
	"VALID" CHAR(1 BYTE), 
	"DATUM" DATE, 
	"UPDATE_VALID" DATE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA" ;

   COMMENT ON COLUMN "DB_DSA"."KP_S_SEP_PROBLEM_BILANCE"."VALID" IS 'KP_S_SEP_STALKER.flag_ignorovat = 1 pre duplicty ico/rc a protistrany bez identifikatoru pouzit v doklade';
   COMMENT ON COLUMN "DB_DSA"."KP_S_SEP_PROBLEM_BILANCE"."DATUM" IS 'datum generovani dokladu';
   COMMENT ON COLUMN "DB_DSA"."KP_S_SEP_PROBLEM_BILANCE"."UPDATE_VALID" IS 'datum zmeny flag_ignorovat na 0 (oprava protistrany)'
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_SEP_PROBLEM_BILANCE.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_SEP_PROBLEM_BILANCE.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_SEP_PROBLEM_BILANCE.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_SEP_STALKER.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_S_SEP_STALKER.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_S_SEP_STALKER
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_S_SEP_STALKER" 
   (	"DBLINK" VARCHAR2(30 BYTE), 
	"STALKER_SEPID" NUMBER(38,0), 
	"STALKER_NAME" VARCHAR2(200 BYTE), 
	"SEP_ID" NUMBER(38,0), 
	"NAZEV" VARCHAR2(200 BYTE), 
	"ICO" VARCHAR2(35 BYTE), 
	"RC" VARCHAR2(35 BYTE), 
	"COUNTRY" CHAR(3 BYTE), 
	"TARGET_SYNC" CHAR(1 BYTE), 
	"FLAG_IGNOROVAT" CHAR(1 BYTE), 
	"TARGET_TYPE" CHAR(1 BYTE), 
	"S_POM_ICO" VARCHAR2(20 BYTE), 
	"ULICE" VARCHAR2(40 BYTE), 
	"PSC" VARCHAR2(10 BYTE), 
	"MESTO" VARCHAR2(40 BYTE), 
	"DIC" VARCHAR2(15 BYTE), 
	"NACE" CHAR(18 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA" ;

   COMMENT ON COLUMN "DB_DSA"."KP_S_SEP_STALKER"."TARGET_SYNC" IS 'sync = 0 - brat target, sync = 1 - brat subject';
   COMMENT ON COLUMN "DB_DSA"."KP_S_SEP_STALKER"."FLAG_IGNOROVAT" IS 'flag_ignorovat = 1 pre duplicty ico/rc a protistrany bez identifikatoru';
   COMMENT ON COLUMN "DB_DSA"."KP_S_SEP_STALKER"."TARGET_TYPE" IS 'target_type = 1 pre dodavatel nebo odberatel'
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_SEP_STALKER.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_SEP_STALKER.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_SEP_STALKER.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_SPIN_EK_OBJEKT.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_S_SPIN_EK_OBJEKT.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_S_SPIN_EK_OBJEKT
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_S_SPIN_EK_OBJEKT" 
   (	"EO_ID" NUMBER(*,0), 
	"NAZOV" VARCHAR2(50 BYTE), 
	"MANDANT_ID" NUMBER(*,0)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 393216 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_SPIN_EK_OBJEKT.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_SPIN_EK_OBJEKT.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_SPIN_EK_OBJEKT.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_SPIN_EO_FIRMA.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_S_SPIN_EO_FIRMA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_S_SPIN_EO_FIRMA
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_S_SPIN_EO_FIRMA" 
   (	"EO_ID" NUMBER(*,0), 
	"FIRMA_ID" NUMBER(*,0)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_SPIN_EO_FIRMA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_SPIN_EO_FIRMA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_SPIN_EO_FIRMA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_SPIN_FIRMA.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_S_SPIN_FIRMA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_S_SPIN_FIRMA
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_S_SPIN_FIRMA" 
   (	"FIRMA_ID" NUMBER(*,0), 
	"ICO" VARCHAR2(15 BYTE), 
	"NAZOV_FIRMY" VARCHAR2(200 BYTE), 
	"FIRMA_LCTSTAMP" DATE, 
	"MANDANT_ID" NUMBER(*,0), 
	"TYP_FIRMY_ID" NUMBER(*,0), 
	"ULICE" VARCHAR2(50 BYTE), 
	"NAZOV_MESTA" VARCHAR2(30 BYTE), 
	"PSC" VARCHAR2(10 BYTE), 
	"STAT" VARCHAR2(2 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 393216 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_SPIN_FIRMA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_SPIN_FIRMA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_SPIN_FIRMA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_SPIN_PEN_USTAV.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_S_SPIN_PEN_USTAV.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_S_SPIN_PEN_USTAV
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_S_SPIN_PEN_USTAV" 
   (	"BANKA_ID" NUMBER(*,0), 
	"NAZOV_BANKY" VARCHAR2(50 BYTE), 
	"KOD_BANKY" VARCHAR2(15 BYTE), 
	"CISLO_POBOCKY" VARCHAR2(10 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_SPIN_PEN_USTAV.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_SPIN_PEN_USTAV.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_SPIN_PEN_USTAV.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_TOPAS_DODODBER.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_S_TOPAS_DODODBER.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_S_TOPAS_DODODBER
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_S_TOPAS_DODODBER" 
   (	"S_DBLINK" VARCHAR2(30 BYTE), 
	"DOID" VARCHAR2(10 BYTE), 
	"ICO" VARCHAR2(35 BYTE), 
	"NAZEV" VARCHAR2(100 BYTE), 
	"DATOP" DATE, 
	"DIC" VARCHAR2(35 BYTE), 
	"ULICE" VARCHAR2(50 BYTE), 
	"PSC" VARCHAR2(20 BYTE), 
	"OBEC" VARCHAR2(50 BYTE), 
	"ZEME" VARCHAR2(3 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 262144 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_TOPAS_DODODBER.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_TOPAS_DODODBER.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_TOPAS_DODODBER.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_TOPAS_EMITENTI.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_S_TOPAS_EMITENTI.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_S_TOPAS_EMITENTI
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_S_TOPAS_EMITENTI" 
   (	"S_DBLINK" VARCHAR2(30 BYTE), 
	"EID" NUMBER(*,0), 
	"ICO" VARCHAR2(11 BYTE), 
	"NAZEV" VARCHAR2(56 BYTE), 
	"STAT" VARCHAR2(3 BYTE), 
	"ULICE" VARCHAR2(32 BYTE), 
	"PSC" VARCHAR2(6 BYTE), 
	"OBEC" VARCHAR2(40 BYTE), 
	"WWW" VARCHAR2(100 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 524288 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_TOPAS_EMITENTI.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_TOPAS_EMITENTI.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_TOPAS_EMITENTI.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_TOPAS_KLIENTI.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_S_TOPAS_KLIENTI.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_S_TOPAS_KLIENTI
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_S_TOPAS_KLIENTI" 
   (	"S_DBLINK" VARCHAR2(30 BYTE), 
	"KID" NUMBER(*,0), 
	"ICO" VARCHAR2(11 BYTE), 
	"NAZEV" VARCHAR2(56 BYTE), 
	"JMENO" VARCHAR2(30 BYTE), 
	"DATOP" DATE, 
	"TYP" CHAR(1 BYTE), 
	"ULICE" VARCHAR2(40 BYTE), 
	"PSC" VARCHAR2(20 BYTE), 
	"OBEC" VARCHAR2(40 BYTE), 
	"STAT" VARCHAR2(3 BYTE), 
	"UCETSCP" VARCHAR2(12 BYTE), 
	"REGRMS" VARCHAR2(9 BYTE), 
	"KODSTAT" VARCHAR2(2 BYTE), 
	"FINALICO" VARCHAR2(20 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_TOPAS_KLIENTI.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_TOPAS_KLIENTI.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_TOPAS_KLIENTI.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_TOPAS_PLATINSTITUCE.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_S_TOPAS_PLATINSTITUCE.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_S_TOPAS_PLATINSTITUCE
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_S_TOPAS_PLATINSTITUCE" 
   (	"S_DBLINK" VARCHAR2(30 BYTE), 
	"PLATINST" VARCHAR2(10 BYTE), 
	"NAZEV" VARCHAR2(100 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_TOPAS_PLATINSTITUCE.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_TOPAS_PLATINSTITUCE.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_TOPAS_PLATINSTITUCE.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_TMP_EPERSPEKTIVAOSOBYPB.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_TMP_EPERSPEKTIVAOSOBYPB.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_TMP_EPERSPEKTIVAOSOBYPB
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_TMP_EPERSPEKTIVAOSOBYPB" 
   (	"OSOBA_ID" NUMBER(38,0), 
	"PRIVATNI_BANKER" VARCHAR2(100 BYTE), 
	"ICO" VARCHAR2(20 BYTE), 
	"NAZEV" VARCHAR2(100 BYTE), 
	"SCP_KLIENT_ID" VARCHAR2(30 BYTE), 
	"ULICE" VARCHAR2(80 BYTE), 
	"MESTO" VARCHAR2(80 BYTE), 
	"PSC" VARCHAR2(10 BYTE), 
	"ICO_CIZINEC" VARCHAR2(20 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_TMP_EPERSPEKTIVAOSOBYPB.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_TMP_EPERSPEKTIVAOSOBYPB.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_TMP_EPERSPEKTIVAOSOBYPB.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_TMP_PBKLIENT.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_TMP_PBKLIENT.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_TMP_PBKLIENT
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_TMP_PBKLIENT" 
   (	"ID_DATABAZE" NUMBER(*,0), 
	"S_ICO" VARCHAR2(20 BYTE), 
	"S_NAZEV" VARCHAR2(100 BYTE), 
	"S_ULICE" VARCHAR2(80 BYTE), 
	"S_MESTO" VARCHAR2(80 BYTE), 
	"S_PSC" VARCHAR2(10 BYTE), 
	"S_STAT" VARCHAR2(5 BYTE), 
	"S_PRIVATNIBANKER" VARCHAR2(100 BYTE), 
	"C_PERSONCORP" CHAR(1 BYTE), 
	"NUMBER0" VARCHAR2(20 BYTE), 
	"KID" NUMBER(*,0), 
	"STREDISKO" VARCHAR2(20 BYTE), 
	"SHORT_NAME" VARCHAR2(20 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_TMP_PBKLIENT.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_TMP_PBKLIENT.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_TMP_PBKLIENT.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_TMP_ZDROJDAT.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying KP_TMP_ZDROJDAT.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table KP_TMP_ZDROJDAT
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."KP_TMP_ZDROJDAT" 
   (	"ID_EXTSYSTEM" VARCHAR2(20 BYTE), 
	"ID_EXTSYSTEM2" VARCHAR2(35 BYTE), 
	"C_EXTSYSTEM" CHAR(1 BYTE), 
	"S_MENA" VARCHAR2(3 BYTE), 
	"C_STRANA" CHAR(1 BYTE), 
	"S_UCET" VARCHAR2(10 BYTE), 
	"ND_CASTKAMENA" NUMBER(18,2), 
	"ND_CASTKALOCAL" NUMBER(18,2), 
	"S_EXTPROTISTRANA" VARCHAR2(30 BYTE), 
	"S_POPIS" VARCHAR2(128 BYTE), 
	"DATUM" DATE, 
	"S_EXTPROTISTRANA_EO" VARCHAR2(30 BYTE), 
	"S_EXTPROTISTRANA_BANKA" VARCHAR2(30 BYTE), 
	"S_EXTPROTISTRANAICO" VARCHAR2(20 BYTE), 
	"S_EXTPROTISTRANAICO_EO" VARCHAR2(20 BYTE), 
	"S_EXTPROTISTRANAICO_BANKA" VARCHAR2(20 BYTE), 
	"KID" NUMBER(*,0), 
	"DOID" VARCHAR2(10 BYTE), 
	"PLATINST" VARCHAR2(10 BYTE), 
	"BANKA_ID" NUMBER(*,0), 
	"FIRMA_ID" NUMBER(*,0), 
	"EO_ID" NUMBER(*,0), 
	"NUMBER0" NUMBER(*,0), 
	"ID_PROTI_1" NUMBER(*,0), 
	"ID_PROTI_2" NUMBER(*,0), 
	"ID_PROTI_3" NUMBER(*,0), 
	"S_EXTPROTISTRANA_EMITENT" VARCHAR2(15 BYTE), 
	"S_EXTPROTISTRANAICO_EMITENT" VARCHAR2(20 BYTE), 
	"EMITENT_ID" NUMBER(*,0), 
	"ID_PROTI_4" NUMBER(*,0), 
	"POCATECNISTAV" CHAR(1 BYTE), 
	"ID_KTGPROJEKT" NUMBER(*,0), 
	"ID_KTGODBOR" NUMBER(*,0), 
	"ID_FRONTA" NUMBER(*,0)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 3145728 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_TMP_ZDROJDAT.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_TMP_ZDROJDAT.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_TMP_ZDROJDAT.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PLSQL_PROFILER_DATA.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying PLSQL_PROFILER_DATA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table PLSQL_PROFILER_DATA
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."PLSQL_PROFILER_DATA" 
   (	"RUNID" NUMBER, 
	"UNIT_NUMBER" NUMBER, 
	"LINE#" NUMBER, 
	"TOTAL_OCCUR" NUMBER, 
	"TOTAL_TIME" NUMBER, 
	"MIN_TIME" NUMBER, 
	"MAX_TIME" NUMBER, 
	"SPARE1" NUMBER, 
	"SPARE2" NUMBER, 
	"SPARE3" NUMBER, 
	"SPARE4" NUMBER
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA" ;

   COMMENT ON TABLE "DB_DSA"."PLSQL_PROFILER_DATA"  IS 'Accumulated data from all profiler runs'
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PLSQL_PROFILER_DATA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PLSQL_PROFILER_DATA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PLSQL_PROFILER_DATA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PLSQL_PROFILER_RUNS.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying PLSQL_PROFILER_RUNS.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table PLSQL_PROFILER_RUNS
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."PLSQL_PROFILER_RUNS" 
   (	"RUNID" NUMBER, 
	"RELATED_RUN" NUMBER, 
	"RUN_OWNER" VARCHAR2(32 BYTE), 
	"RUN_DATE" DATE, 
	"RUN_COMMENT" VARCHAR2(2047 BYTE), 
	"RUN_TOTAL_TIME" NUMBER, 
	"RUN_SYSTEM_INFO" VARCHAR2(2047 BYTE), 
	"RUN_COMMENT1" VARCHAR2(2047 BYTE), 
	"SPARE1" VARCHAR2(256 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA" ;

   COMMENT ON TABLE "DB_DSA"."PLSQL_PROFILER_RUNS"  IS 'Run-specific information for the PL/SQL profiler'
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PLSQL_PROFILER_RUNS.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PLSQL_PROFILER_RUNS.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PLSQL_PROFILER_RUNS.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PLSQL_PROFILER_UNITS.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying PLSQL_PROFILER_UNITS.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table PLSQL_PROFILER_UNITS
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."PLSQL_PROFILER_UNITS" 
   (	"RUNID" NUMBER, 
	"UNIT_NUMBER" NUMBER, 
	"UNIT_TYPE" VARCHAR2(32 BYTE), 
	"UNIT_OWNER" VARCHAR2(32 BYTE), 
	"UNIT_NAME" VARCHAR2(32 BYTE), 
	"UNIT_TIMESTAMP" DATE, 
	"TOTAL_TIME" NUMBER DEFAULT 0, 
	"SPARE1" NUMBER, 
	"SPARE2" NUMBER
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA" ;

   COMMENT ON TABLE "DB_DSA"."PLSQL_PROFILER_UNITS"  IS 'Information about each library unit in a run'
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PLSQL_PROFILER_UNITS.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PLSQL_PROFILER_UNITS.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PLSQL_PROFILER_UNITS.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: Q_ALL_GENERATION.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying Q_ALL_GENERATION.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table Q_ALL_GENERATION
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."Q_ALL_GENERATION" 
   (	"KIND" CHAR(1 BYTE), 
	"GEN_DATE" DATE, 
	"TIME_GENERATED" DATE, 
	"FK_GEN_ID" CHAR(20 BYTE), 
	"GEN_DATE_FROM" DATE, 
	"Q_FROM" CHAR(3 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: Q_ALL_GENERATION.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: Q_ALL_GENERATION.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: Q_ALL_GENERATION.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: Q_REP_BOOKING.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying Q_REP_BOOKING.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table Q_REP_BOOKING
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."Q_REP_BOOKING" 
   (	"BOOKING_ID" CHAR(20 BYTE), 
	"PC_ID" CHAR(20 BYTE), 
	"PROD_NUMBER" CHAR(35 BYTE), 
	"DESCRIPTION" CHAR(50 BYTE), 
	"BOOK_CURRENCY" CHAR(3 BYTE), 
	"BOOK_DATE" DATE, 
	"EFFECTIVE_DATE" DATE, 
	"GL_SYNTETIC" VARCHAR2(35 BYTE), 
	"AMOUNT" NUMBER(15,2), 
	"LC_AMOUNT" NUMBER(15,2), 
	"OWNER_ID" CHAR(20 BYTE), 
	"IND_DEBIT_CREDIT" CHAR(1 BYTE), 
	"BOOK_NUMBER" VARCHAR2(30 BYTE), 
	"REFERENCE0" CHAR(20 BYTE), 
	"TOPAS_PROTISTRANA" VARCHAR2(10 BYTE), 
	"EKONOM_SEKTOR" VARCHAR2(20 BYTE), 
	"AVAIL_KONTOKORENT" NUMBER(15,2), 
	"POCATECNISTAV" CHAR(1 BYTE), 
	"TOPAS_KID" NUMBER(*,0), 
	"ID_KTGPROJEKT" NUMBER(*,0), 
	"ID_KTGODBOR" NUMBER(*,0), 
	"VYJIMKAPROTISTRANA" CHAR(1 BYTE), 
	"TOPSECUR_PROTISTRANA" VARCHAR2(10 BYTE), 
	"PC_PROD_KIND" CHAR(3 BYTE), 
	"PC_PROD_TYPE" CHAR(3 BYTE), 
	"PC_STATUS" CHAR(2 BYTE), 
	"INTEREST" NUMBER(8,5), 
	"PROD_NUMBER_2" CHAR(35 BYTE), 
	"XIDSL" NUMBER(*,0), 
	"ID_FRONTA" NUMBER(*,0), 
	"JOINPCID" CHAR(20 BYTE), 
	"C_TYPTRANSAKCE" CHAR(1 BYTE), 
	"ID_KTGCENNYPAPIR" NUMBER(*,0), 
	"TOPAS_EMITENT" NUMBER(*,0)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: Q_REP_BOOKING.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: Q_REP_BOOKING.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: Q_REP_BOOKING.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: Q_REP_BOOKING_QCZ.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying Q_REP_BOOKING_QCZ.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table Q_REP_BOOKING_QCZ
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."Q_REP_BOOKING_QCZ" 
   (	"BOOKING_ID" CHAR(20 BYTE), 
	"PC_ID" CHAR(20 BYTE), 
	"PROD_NUMBER" CHAR(35 BYTE), 
	"DESCRIPTION" CHAR(50 BYTE), 
	"BOOK_CURRENCY" CHAR(3 BYTE), 
	"BOOK_DATE" DATE, 
	"EFFECTIVE_DATE" DATE, 
	"GL_SYNTETIC" VARCHAR2(35 BYTE), 
	"AMOUNT" NUMBER(15,2), 
	"LC_AMOUNT" NUMBER(15,2), 
	"OWNER_ID" CHAR(20 BYTE), 
	"IND_DEBIT_CREDIT" CHAR(1 BYTE), 
	"BOOK_NUMBER" VARCHAR2(30 BYTE), 
	"REFERENCE0" CHAR(20 BYTE), 
	"TOPAS_PROTISTRANA" VARCHAR2(10 BYTE), 
	"EKONOM_SEKTOR" VARCHAR2(20 BYTE), 
	"AVAIL_KONTOKORENT" NUMBER(15,2), 
	"POCATECNISTAV" CHAR(1 BYTE), 
	"TOPAS_KID" NUMBER(*,0), 
	"ID_KTGPROJEKT" NUMBER(*,0), 
	"ID_KTGODBOR" NUMBER(*,0), 
	"VYJIMKAPROTISTRANA" CHAR(1 BYTE), 
	"TOPSECUR_PROTISTRANA" VARCHAR2(10 BYTE), 
	"PC_PROD_KIND" CHAR(3 BYTE), 
	"PC_PROD_TYPE" CHAR(3 BYTE), 
	"PC_STATUS" CHAR(2 BYTE), 
	"INTEREST" NUMBER(8,5), 
	"PROD_NUMBER_2" CHAR(35 BYTE), 
	"XIDSL" NUMBER(*,0), 
	"ID_FRONTA" NUMBER(*,0), 
	"JOINPCID" CHAR(20 BYTE), 
	"C_TYPTRANSAKCE" CHAR(1 BYTE), 
	"ID_KTGCENNYPAPIR" NUMBER(*,0), 
	"TOPAS_EMITENT" NUMBER(*,0), 
	"FK_GEN_ID" CHAR(20 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: Q_REP_BOOKING_QCZ.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: Q_REP_BOOKING_QCZ.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: Q_REP_BOOKING_QCZ.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: Q_REP_BOOKING_QSK.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying Q_REP_BOOKING_QSK.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table Q_REP_BOOKING_QSK
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."Q_REP_BOOKING_QSK" 
   (	"BOOKING_ID" CHAR(20 BYTE), 
	"PC_ID" CHAR(20 BYTE), 
	"PROD_NUMBER" CHAR(35 BYTE), 
	"DESCRIPTION" CHAR(50 BYTE), 
	"BOOK_CURRENCY" CHAR(3 BYTE), 
	"BOOK_DATE" DATE, 
	"EFFECTIVE_DATE" DATE, 
	"GL_SYNTETIC" VARCHAR2(35 BYTE), 
	"AMOUNT" NUMBER(15,2), 
	"LC_AMOUNT" NUMBER(15,2), 
	"OWNER_ID" CHAR(20 BYTE), 
	"IND_DEBIT_CREDIT" CHAR(1 BYTE), 
	"BOOK_NUMBER" VARCHAR2(30 BYTE), 
	"REFERENCE0" CHAR(20 BYTE), 
	"TOPAS_PROTISTRANA" VARCHAR2(10 BYTE), 
	"EKONOM_SEKTOR" VARCHAR2(20 BYTE), 
	"AVAIL_KONTOKORENT" NUMBER(15,2), 
	"POCATECNISTAV" CHAR(1 BYTE), 
	"TOPAS_KID" NUMBER(*,0), 
	"ID_KTGPROJEKT" NUMBER(*,0), 
	"ID_KTGODBOR" NUMBER(*,0), 
	"VYJIMKAPROTISTRANA" CHAR(1 BYTE), 
	"TOPSECUR_PROTISTRANA" VARCHAR2(10 BYTE), 
	"PC_PROD_KIND" CHAR(3 BYTE), 
	"PC_PROD_TYPE" CHAR(3 BYTE), 
	"PC_STATUS" CHAR(2 BYTE), 
	"INTEREST" NUMBER(8,5), 
	"PROD_NUMBER_2" CHAR(35 BYTE), 
	"XIDSL" NUMBER(*,0), 
	"ID_FRONTA" NUMBER(*,0), 
	"JOINPCID" CHAR(20 BYTE), 
	"C_TYPTRANSAKCE" CHAR(1 BYTE), 
	"ID_KTGCENNYPAPIR" NUMBER(*,0), 
	"TOPAS_EMITENT" NUMBER(*,0), 
	"FK_GEN_ID" CHAR(20 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: Q_REP_BOOKING_QSK.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: Q_REP_BOOKING_QSK.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: Q_REP_BOOKING_QSK.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: Q_REP_KONTROLA.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying Q_REP_KONTROLA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table Q_REP_KONTROLA
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."Q_REP_KONTROLA" 
   (	"ID" NUMBER, 
	"DATUM" DATE, 
	"KIND" CHAR(1 BYTE), 
	"GL_SYNTETIC" CHAR(35 BYTE), 
	"BOOK_CURRENCY" CHAR(3 BYTE), 
	"BIS_BALANCE" NUMBER(15,2), 
	"FEIS_BALANCE" NUMBER(15,2), 
	"DIFFERENCE" NUMBER(15,2), 
	"FK_GEN_ID" CHAR(20 BYTE), 
	"QSOURCE" CHAR(3 BYTE), 
	"DATE_INSERT" DATE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA" ;

   COMMENT ON COLUMN "DB_DSA"."Q_REP_KONTROLA"."DATUM" IS 'datum generovania na Quaestore';
   COMMENT ON COLUMN "DB_DSA"."Q_REP_KONTROLA"."KIND" IS 'B/PCB, E/PCE';
   COMMENT ON COLUMN "DB_DSA"."Q_REP_KONTROLA"."QSOURCE" IS 'QCZ / QSK';
   COMMENT ON COLUMN "DB_DSA"."Q_REP_KONTROLA"."DATE_INSERT" IS 'kedy bolo nacitane z Q do KIS'
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: Q_REP_KONTROLA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: Q_REP_KONTROLA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: Q_REP_KONTROLA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: TMP_ID.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying TMP_ID.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table TMP_ID
--------------------------------------------------------

  CREATE GLOBAL TEMPORARY TABLE "DB_DSA"."TMP_ID" 
   (	"ID" NUMBER(*,0)
   ) ON COMMIT PRESERVE ROWS
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: TMP_ID.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: TMP_ID.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: TMP_ID.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: TR_TABLE1.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying TR_TABLE1.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table TR_TABLE1
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."TR_TABLE1" 
   (	"ID" NUMBER(*,0), 
	"MSG" VARCHAR2(100 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: TR_TABLE1.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: TR_TABLE1.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: TR_TABLE1.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: T_AIX_POHYBY.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying T_AIX_POHYBY.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table T_AIX_POHYBY
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."T_AIX_POHYBY" 
   (	"UDID" NUMBER(20,0), 
	"UPID" NUMBER(20,0), 
	"M1D0" NUMBER(10,0), 
	"DATUM" DATE, 
	"UCET" NVARCHAR2(20), 
	"STREDISKO" NVARCHAR2(15), 
	"PROTIUCET" NVARCHAR2(20), 
	"PROTISTREDISKO" NVARCHAR2(15), 
	"TEXT" NVARCHAR2(100), 
	"CASTKA" NUMBER(32,16), 
	"MENA" NVARCHAR2(3), 
	"CASTKAMENY" NUMBER(32,16), 
	"KURZ" NUMBER(38,6), 
	"JEDNOTKA" NUMBER(10,0), 
	"DOID" NVARCHAR2(20), 
	"PARZNAK" NVARCHAR2(30), 
	"VYPZNAK" NUMBER(10,0), 
	"DATUMSPLAT" DATE, 
	"ZAKAZKA" NVARCHAR2(30), 
	"CINNOST" NVARCHAR2(30), 
	"DATUMDPH" DATE, 
	"RELACEDPH" NUMBER(10,0), 
	"SAZBADPH" NUMBER(32,16), 
	"EXPORTLOCK" NUMBER(10,0), 
	"OPID" NUMBER(10,0), 
	"DATOP" NUMBER(10,0), 
	"TOPZKRATKA" NUMBER(10,0), 
	"MANAZER_ID" NVARCHAR2(30), 
	"PROJEKT_ID" NVARCHAR2(30), 
	"XIDSL" NUMBER(10,0), 
	"DOKLAD" VARCHAR2(30 BYTE), 
	"ID_FRONTA" NUMBER(*,0), 
	"POCATECNISTAV" CHAR(1 BYTE), 
	"VYJIMKAPROTISTRANA" CHAR(1 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: T_AIX_POHYBY.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: T_AIX_POHYBY.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: T_AIX_POHYBY.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: T_AIX_POHYBY_MEZZANINE.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying T_AIX_POHYBY_MEZZANINE.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table T_AIX_POHYBY_MEZZANINE
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."T_AIX_POHYBY_MEZZANINE" 
   (	"UDID" NUMBER(20,0), 
	"UPID" NUMBER(20,0), 
	"M1D0" NUMBER(10,0), 
	"DATUM" DATE, 
	"UCET" NVARCHAR2(20), 
	"STREDISKO" NVARCHAR2(15), 
	"PROTIUCET" NVARCHAR2(20), 
	"PROTISTREDISKO" NVARCHAR2(15), 
	"TEXT" NVARCHAR2(100), 
	"CASTKA" NUMBER(32,16), 
	"MENA" NVARCHAR2(3), 
	"CASTKAMENY" NUMBER(32,16), 
	"KURZ" NUMBER(38,6), 
	"JEDNOTKA" NUMBER(10,0), 
	"DOID" NVARCHAR2(20), 
	"PARZNAK" NVARCHAR2(30), 
	"VYPZNAK" NUMBER(10,0), 
	"DATUMSPLAT" DATE, 
	"ZAKAZKA" NVARCHAR2(30), 
	"CINNOST" NVARCHAR2(30), 
	"DATUMDPH" DATE, 
	"RELACEDPH" NUMBER(10,0), 
	"SAZBADPH" NUMBER(32,16), 
	"EXPORTLOCK" NUMBER(10,0), 
	"OPID" NUMBER(10,0), 
	"DATOP" NUMBER(10,0), 
	"TOPZKRATKA" NUMBER(10,0), 
	"MANAZER_ID" NVARCHAR2(30), 
	"PROJEKT_ID" NVARCHAR2(30), 
	"XIDSL" NUMBER(10,0), 
	"DOKLAD" VARCHAR2(30 BYTE), 
	"ID_FRONTA" NUMBER(*,0), 
	"POCATECNISTAV" CHAR(1 BYTE), 
	"VYJIMKAPROTISTRANA" CHAR(1 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: T_AIX_POHYBY_MEZZANINE.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: T_AIX_POHYBY_MEZZANINE.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: T_AIX_POHYBY_MEZZANINE.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: T_UCET_POHYBY.sql
-- Category: tables
-- ============================================================================

PROMPT Deploying T_UCET_POHYBY.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Table T_UCET_POHYBY
--------------------------------------------------------

  CREATE TABLE "DB_DSA"."T_UCET_POHYBY" 
   (	"UPID" NUMBER(12,0), 
	"UDID" NUMBER(12,0), 
	"DATUM" DATE, 
	"M1D0" CHAR(1 BYTE), 
	"DATUMSPLAT" DATE, 
	"CASTKA" NUMBER(19,2), 
	"UCET" VARCHAR2(20 BYTE), 
	"STREDISKO" VARCHAR2(30 BYTE), 
	"PROTIUCET" VARCHAR2(20 BYTE), 
	"PROTISTREDISKO" VARCHAR2(30 BYTE), 
	"DOID" VARCHAR2(60 BYTE), 
	"PARZNAK" VARCHAR2(30 BYTE), 
	"ZAKAZKA" VARCHAR2(30 BYTE), 
	"CINNOST" VARCHAR2(30 BYTE), 
	"MENA" VARCHAR2(3 BYTE), 
	"CASTKAMENY" NUMBER(19,2), 
	"KURZ" NUMBER(21,9), 
	"JEDNOTKA" NUMBER(15,6), 
	"TEXT" VARCHAR2(50 BYTE), 
	"OPID" NUMBER(6,0), 
	"DATOP" DATE, 
	"CISLO" NUMBER(9,0), 
	"PORADI" NUMBER(5,0), 
	"PLID" NUMBER(11,0), 
	"TOPEXTDOKLAD" VARCHAR2(20 BYTE), 
	"TOPZKRATKA" VARCHAR2(30 BYTE), 
	"TYPDOKL" VARCHAR2(3 BYTE), 
	"PLATINST" VARCHAR2(10 BYTE), 
	"DOID_ICO" VARCHAR2(20 BYTE), 
	"KLIENT_ICO" VARCHAR2(20 BYTE), 
	"PLATINST_ICO" VARCHAR2(20 BYTE), 
	"KID" NUMBER(*,0), 
	"VYJIMKAPROTISTRANA" VARCHAR2(1 BYTE) DEFAULT '0', 
	"EMITENT_ID" NUMBER(*,0), 
	"EMITENT_ICO" VARCHAR2(10 BYTE), 
	"EMITENT_ZKRATKA" VARCHAR2(15 BYTE), 
	"POCATECNISTAV" CHAR(1 BYTE), 
	"ID_KTGPROJEKT" NUMBER(*,0), 
	"ID_KTGODBOR" NUMBER(*,0), 
	"XIDSL" NUMBER(*,0), 
	"DOKLAD" VARCHAR2(30 BYTE), 
	"ID_FRONTA" NUMBER(*,0), 
	"C_ZNAKUCET" CHAR(1 BYTE), 
	"UCTOVANODNE" DATE, 
	"X_ISIT_BUDGET_ID" VARCHAR2(20 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 25296896 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"  ENABLE ROW MOVEMENT ;

   COMMENT ON COLUMN "DB_DSA"."T_UCET_POHYBY"."C_ZNAKUCET" IS 'Dava tam prvni znak z uctu. Musi vykousnout prvni nenumericky znak pokud v uctu je. Pouziva se pak pro jednoduche promazani vysledovkovych uctu za obdobi.'
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: T_UCET_POHYBY.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: T_UCET_POHYBY.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: T_UCET_POHYBY.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


PROMPT Completed tables deployment
PROMPT


-- ============================================================================
-- PHASE: Deploy CONSTRAINTS (35 files)
-- ============================================================================

PROMPT
PROMPT ============================================================================
PROMPT Deploying CONSTRAINTS (35 files)
PROMPT ============================================================================
PROMPT


-- ============================================================================
-- Deploy: ADMIN_MAIL_NOTIFICATION.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying ADMIN_MAIL_NOTIFICATION.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table ADMIN_MAIL_NOTIFICATION
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."ADMIN_MAIL_NOTIFICATION" ADD CONSTRAINT "MAIL_NOTIFICATION_1_UNIQUE" UNIQUE ("USER_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"  ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: ADMIN_MAIL_NOTIFICATION.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: ADMIN_MAIL_NOTIFICATION.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: ADMIN_MAIL_NOTIFICATION.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_CIS_RKC_INVALID_ICO_QUA.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying KP_CIS_RKC_INVALID_ICO_QUA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table KP_CIS_RKC_INVALID_ICO_QUA
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."KP_CIS_RKC_INVALID_ICO_QUA" MODIFY ("FIKTIVNI_NUMBER0" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."KP_CIS_RKC_INVALID_ICO_QUA" MODIFY ("INVALID_RKC_ICO" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."KP_CIS_RKC_INVALID_ICO_QUA" MODIFY ("RKC_REQUESTED_NAME" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."KP_CIS_RKC_INVALID_ICO_QUA" ADD CONSTRAINT "PK_RKC_INVALID_ICO" PRIMARY KEY ("FIKTIVNI_NUMBER0")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"  ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_CIS_RKC_INVALID_ICO_QUA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_CIS_RKC_INVALID_ICO_QUA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_CIS_RKC_INVALID_ICO_QUA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_KTG_PROTISTRANAVYJIMKY.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying KP_KTG_PROTISTRANAVYJIMKY.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table KP_KTG_PROTISTRANAVYJIMKY
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."KP_KTG_PROTISTRANAVYJIMKY" MODIFY ("ID_DATABAZE" CONSTRAINT "NN_KPKTGPROTIVYJIMKY_IDDB" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."KP_KTG_PROTISTRANAVYJIMKY" MODIFY ("NL_POZICEOD" CONSTRAINT "NN_KPPROTISTRANAVYJIMKY" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."KP_KTG_PROTISTRANAVYJIMKY" ADD CONSTRAINT "PK_KPKTGPROTISTRANAVYJIMKY" PRIMARY KEY ("ID")
  USING INDEX "DB_DSA"."PK_KPKTGPROTISTRANAVYJIMKY"  ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_KTG_PROTISTRANAVYJIMKY.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_KTG_PROTISTRANAVYJIMKY.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_KTG_PROTISTRANAVYJIMKY.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_LOG_QUAESTOR_CUST_GROUP.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying KP_LOG_QUAESTOR_CUST_GROUP.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table KP_LOG_QUAESTOR_CUST_GROUP
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."KP_LOG_QUAESTOR_CUST_GROUP" ADD CONSTRAINT "PK_LOG_QUAESTOR_CUST_GROUP" PRIMARY KEY ("ID_LOG", "DT_LOG", "NUMBER0", "CUST_GROUP_CODE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"  ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_LOG_QUAESTOR_CUST_GROUP.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_LOG_QUAESTOR_CUST_GROUP.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_LOG_QUAESTOR_CUST_GROUP.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_LOG_QUAESTOR_RELATION.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying KP_LOG_QUAESTOR_RELATION.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table KP_LOG_QUAESTOR_RELATION
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."KP_LOG_QUAESTOR_RELATION" ADD CONSTRAINT "PK_LOG_QUAESTOR_RELATION" PRIMARY KEY ("ID_LOG", "DT_LOG", "NUMBER0")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"  ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_LOG_QUAESTOR_RELATION.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_LOG_QUAESTOR_RELATION.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_LOG_QUAESTOR_RELATION.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_RADICIMPORTU.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying KP_RADICIMPORTU.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table KP_RADICIMPORTU
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."KP_RADICIMPORTU" MODIFY ("S_DBLINK" CONSTRAINT "NN_KPRADICIMPORT_LINK" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."KP_RADICIMPORTU" ADD CONSTRAINT "PK_KPRADICIMPORT" PRIMARY KEY ("ID_FRONTA")
  USING INDEX "DB_DSA"."PK_KPRADICIMPORT"  ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_RADICIMPORTU.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_RADICIMPORTU.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_RADICIMPORTU.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_REL_PROTISTRANA.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying KP_REL_PROTISTRANA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table KP_REL_PROTISTRANA
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."KP_REL_PROTISTRANA" MODIFY ("ID_DATABAZE" CONSTRAINT "NN_KPRELPROTI_IDDB" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."KP_REL_PROTISTRANA" ADD CONSTRAINT "PK_KPREL_PROTISTRANA" PRIMARY KEY ("ID")
  USING INDEX "DB_DSA"."PK_KPREL_PROTISTRANA"  ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_REL_PROTISTRANA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_REL_PROTISTRANA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_REL_PROTISTRANA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_MANUAL.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying KP_S_MANUAL.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table KP_S_MANUAL
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."KP_S_MANUAL" ADD CONSTRAINT "PK_KPSMANUAL" PRIMARY KEY ("S_DBLINK", "S_EXTID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"  ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_MANUAL.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_MANUAL.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_MANUAL.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_MANUALTEXT.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying KP_S_MANUALTEXT.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table KP_S_MANUALTEXT
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."KP_S_MANUALTEXT" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."KP_S_MANUALTEXT" MODIFY ("S_DBLINK" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."KP_S_MANUALTEXT" MODIFY ("S_TEXT" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."KP_S_MANUALTEXT" ADD CONSTRAINT "PK_KPSMANUALTEXT" PRIMARY KEY ("ID")
  USING INDEX "DB_DSA"."PK_KPSMANUALTEXT"  ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_MANUALTEXT.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_MANUALTEXT.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_MANUALTEXT.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_SEP.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying KP_S_SEP.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table KP_S_SEP
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."KP_S_SEP" MODIFY ("SEP_ID" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."KP_S_SEP" ADD CONSTRAINT "PK_KPSSEP" PRIMARY KEY ("SEP_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"  ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_SEP.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_SEP.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_SEP.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_SEP_STALKER.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying KP_S_SEP_STALKER.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table KP_S_SEP_STALKER
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."KP_S_SEP_STALKER" MODIFY ("DBLINK" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."KP_S_SEP_STALKER" MODIFY ("STALKER_SEPID" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."KP_S_SEP_STALKER" MODIFY ("SEP_ID" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."KP_S_SEP_STALKER" ADD CONSTRAINT "PK_KP_S_SEP_STALKER" PRIMARY KEY ("STALKER_SEPID", "SEP_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"  ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_SEP_STALKER.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_SEP_STALKER.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_SEP_STALKER.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_SPIN_EK_OBJEKT.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying KP_S_SPIN_EK_OBJEKT.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table KP_S_SPIN_EK_OBJEKT
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."KP_S_SPIN_EK_OBJEKT" ADD CONSTRAINT "PK_KPSSPINEKOBJ" PRIMARY KEY ("EO_ID")
  USING INDEX "DB_DSA"."PK_KPSSPINEKOBJ"  ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_SPIN_EK_OBJEKT.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_SPIN_EK_OBJEKT.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_SPIN_EK_OBJEKT.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_SPIN_EO_FIRMA.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying KP_S_SPIN_EO_FIRMA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table KP_S_SPIN_EO_FIRMA
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."KP_S_SPIN_EO_FIRMA" ADD CONSTRAINT "PK_KPSSPINEOFIRMA" PRIMARY KEY ("EO_ID", "FIRMA_ID")
  USING INDEX "DB_DSA"."PK_KPSSPINEOFIRMA"  ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_SPIN_EO_FIRMA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_SPIN_EO_FIRMA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_SPIN_EO_FIRMA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_SPIN_FIRMA.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying KP_S_SPIN_FIRMA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table KP_S_SPIN_FIRMA
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."KP_S_SPIN_FIRMA" ADD CONSTRAINT "PK_KPSSPINFIRMA" PRIMARY KEY ("FIRMA_ID")
  USING INDEX "DB_DSA"."PK_KPSSPINFIRMA"  ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_SPIN_FIRMA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_SPIN_FIRMA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_SPIN_FIRMA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_SPIN_PEN_USTAV.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying KP_S_SPIN_PEN_USTAV.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table KP_S_SPIN_PEN_USTAV
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."KP_S_SPIN_PEN_USTAV" ADD CONSTRAINT "PK_KPSSPINPUSTAV" PRIMARY KEY ("BANKA_ID")
  USING INDEX "DB_DSA"."PK_KPSSPINPUSTAV"  ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_SPIN_PEN_USTAV.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_SPIN_PEN_USTAV.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_SPIN_PEN_USTAV.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_TOPAS_DODODBER.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying KP_S_TOPAS_DODODBER.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table KP_S_TOPAS_DODODBER
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."KP_S_TOPAS_DODODBER" ADD CONSTRAINT "PK_KPSTOPASDODODBER" PRIMARY KEY ("S_DBLINK", "DOID")
  USING INDEX "DB_DSA"."PK_KPSTOPASDODODBER"  ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_TOPAS_DODODBER.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_TOPAS_DODODBER.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_TOPAS_DODODBER.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_TOPAS_EMITENTI.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying KP_S_TOPAS_EMITENTI.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table KP_S_TOPAS_EMITENTI
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."KP_S_TOPAS_EMITENTI" ADD CONSTRAINT "PK_KPSTOPASEMITENTI" PRIMARY KEY ("S_DBLINK", "EID")
  USING INDEX "DB_DSA"."PK_KPSTOPASEMITENTI"  ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_TOPAS_EMITENTI.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_TOPAS_EMITENTI.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_TOPAS_EMITENTI.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_TOPAS_KLIENTI.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying KP_S_TOPAS_KLIENTI.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table KP_S_TOPAS_KLIENTI
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."KP_S_TOPAS_KLIENTI" ADD CONSTRAINT "PK_KPSTOPASKLIENT" PRIMARY KEY ("S_DBLINK", "KID")
  USING INDEX "DB_DSA"."PK_KPSTOPASKLIENT"  ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_TOPAS_KLIENTI.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_TOPAS_KLIENTI.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_TOPAS_KLIENTI.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_S_TOPAS_PLATINSTITUCE.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying KP_S_TOPAS_PLATINSTITUCE.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table KP_S_TOPAS_PLATINSTITUCE
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."KP_S_TOPAS_PLATINSTITUCE" ADD CONSTRAINT "PK_KPSTOPASPLATINST" PRIMARY KEY ("S_DBLINK", "PLATINST")
  USING INDEX "DB_DSA"."PK_KPSTOPASPLATINST"  ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_S_TOPAS_PLATINSTITUCE.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_S_TOPAS_PLATINSTITUCE.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_S_TOPAS_PLATINSTITUCE.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_TMP_EPERSPEKTIVAOSOBYPB.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying KP_TMP_EPERSPEKTIVAOSOBYPB.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table KP_TMP_EPERSPEKTIVAOSOBYPB
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."KP_TMP_EPERSPEKTIVAOSOBYPB" MODIFY ("OSOBA_ID" NOT NULL ENABLE)
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_TMP_EPERSPEKTIVAOSOBYPB.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_TMP_EPERSPEKTIVAOSOBYPB.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_TMP_EPERSPEKTIVAOSOBYPB.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_TMP_PBKLIENT.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying KP_TMP_PBKLIENT.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table KP_TMP_PBKLIENT
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."KP_TMP_PBKLIENT" MODIFY ("ID_DATABAZE" CONSTRAINT "NN_KTGPBKIENT_IDDB" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."KP_TMP_PBKLIENT" MODIFY ("S_ICO" CONSTRAINT "NN_KTGPBKIENT_ICO" NOT NULL ENABLE)
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_TMP_PBKLIENT.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_TMP_PBKLIENT.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_TMP_PBKLIENT.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KP_TMP_ZDROJDAT.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying KP_TMP_ZDROJDAT.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table KP_TMP_ZDROJDAT
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."KP_TMP_ZDROJDAT" MODIFY ("ID_FRONTA" CONSTRAINT "NN_TMPZDROJDAT_FRONTA" NOT NULL ENABLE)
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KP_TMP_ZDROJDAT.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KP_TMP_ZDROJDAT.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KP_TMP_ZDROJDAT.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PLSQL_PROFILER_DATA.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying PLSQL_PROFILER_DATA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table PLSQL_PROFILER_DATA
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."PLSQL_PROFILER_DATA" MODIFY ("LINE#" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."PLSQL_PROFILER_DATA" ADD PRIMARY KEY ("RUNID", "UNIT_NUMBER", "LINE#")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"  ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PLSQL_PROFILER_DATA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PLSQL_PROFILER_DATA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PLSQL_PROFILER_DATA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PLSQL_PROFILER_RUNS.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying PLSQL_PROFILER_RUNS.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table PLSQL_PROFILER_RUNS
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."PLSQL_PROFILER_RUNS" ADD PRIMARY KEY ("RUNID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"  ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PLSQL_PROFILER_RUNS.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PLSQL_PROFILER_RUNS.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PLSQL_PROFILER_RUNS.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PLSQL_PROFILER_UNITS.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying PLSQL_PROFILER_UNITS.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table PLSQL_PROFILER_UNITS
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."PLSQL_PROFILER_UNITS" MODIFY ("TOTAL_TIME" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."PLSQL_PROFILER_UNITS" ADD PRIMARY KEY ("RUNID", "UNIT_NUMBER")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"  ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PLSQL_PROFILER_UNITS.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PLSQL_PROFILER_UNITS.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PLSQL_PROFILER_UNITS.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: Q_REP_BOOKING.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying Q_REP_BOOKING.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table Q_REP_BOOKING
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."Q_REP_BOOKING" MODIFY ("BOOKING_ID" CONSTRAINT "NN_QREPBOOKING_ID" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING" MODIFY ("PC_ID" CONSTRAINT "NN_QREPBOOKING_PC_ID" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING" MODIFY ("PROD_NUMBER" CONSTRAINT "NN_QREPBOOKING_PROD_NUM" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING" MODIFY ("BOOK_CURRENCY" CONSTRAINT "NN_QREPBOOKING_BOOK_CURR" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING" MODIFY ("BOOK_DATE" CONSTRAINT "NN_QREPBOOKING_BOOKDATE" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING" MODIFY ("EFFECTIVE_DATE" CONSTRAINT "NN_QREPBOOKING_EFFDATE" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING" MODIFY ("GL_SYNTETIC" CONSTRAINT "NN_QREPBOOKING_GLSYNT" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING" MODIFY ("AMOUNT" CONSTRAINT "NN_QREPBOOKING_AMOUNT" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING" MODIFY ("LC_AMOUNT" CONSTRAINT "NN_QREPBOOKING_AMOUNTLC" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING" MODIFY ("OWNER_ID" CONSTRAINT "NN_QREPBOOKING_OWNER" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING" MODIFY ("IND_DEBIT_CREDIT" CONSTRAINT "NN_QREPBOOKING_DEBCRED" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING" MODIFY ("ID_FRONTA" CONSTRAINT "NN_REPBOOKING_FRONTA" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING" ADD CONSTRAINT "PK_QREPBOOKING" PRIMARY KEY ("ID_FRONTA", "BOOKING_ID")
  USING INDEX "DB_DSA"."PK_QREPBOOKING"  ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: Q_REP_BOOKING.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: Q_REP_BOOKING.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: Q_REP_BOOKING.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: Q_REP_BOOKING_BCK_2011.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying Q_REP_BOOKING_BCK_2011.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table Q_REP_BOOKING_BCK_2011
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_BCK_2011" MODIFY ("BOOKING_ID" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_BCK_2011" MODIFY ("PC_ID" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_BCK_2011" MODIFY ("PROD_NUMBER" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_BCK_2011" MODIFY ("BOOK_CURRENCY" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_BCK_2011" MODIFY ("BOOK_DATE" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_BCK_2011" MODIFY ("EFFECTIVE_DATE" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_BCK_2011" MODIFY ("GL_SYNTETIC" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_BCK_2011" MODIFY ("AMOUNT" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_BCK_2011" MODIFY ("LC_AMOUNT" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_BCK_2011" MODIFY ("OWNER_ID" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_BCK_2011" MODIFY ("IND_DEBIT_CREDIT" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_BCK_2011" MODIFY ("ID_FRONTA" NOT NULL ENABLE)
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: Q_REP_BOOKING_BCK_2011.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: Q_REP_BOOKING_BCK_2011.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: Q_REP_BOOKING_BCK_2011.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: Q_REP_BOOKING_QCZ.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying Q_REP_BOOKING_QCZ.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table Q_REP_BOOKING_QCZ
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_QCZ" MODIFY ("BOOKING_ID" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_QCZ" MODIFY ("PC_ID" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_QCZ" MODIFY ("PROD_NUMBER" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_QCZ" MODIFY ("BOOK_CURRENCY" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_QCZ" MODIFY ("BOOK_DATE" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_QCZ" MODIFY ("EFFECTIVE_DATE" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_QCZ" MODIFY ("GL_SYNTETIC" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_QCZ" MODIFY ("AMOUNT" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_QCZ" MODIFY ("LC_AMOUNT" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_QCZ" MODIFY ("OWNER_ID" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_QCZ" MODIFY ("IND_DEBIT_CREDIT" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_QCZ" MODIFY ("ID_FRONTA" NOT NULL ENABLE)
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: Q_REP_BOOKING_QCZ.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: Q_REP_BOOKING_QCZ.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: Q_REP_BOOKING_QCZ.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: Q_REP_BOOKING_QSK.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying Q_REP_BOOKING_QSK.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table Q_REP_BOOKING_QSK
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_QSK" MODIFY ("BOOKING_ID" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_QSK" MODIFY ("PC_ID" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_QSK" MODIFY ("PROD_NUMBER" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_QSK" MODIFY ("BOOK_CURRENCY" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_QSK" MODIFY ("BOOK_DATE" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_QSK" MODIFY ("EFFECTIVE_DATE" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_QSK" MODIFY ("GL_SYNTETIC" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_QSK" MODIFY ("AMOUNT" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_QSK" MODIFY ("LC_AMOUNT" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_QSK" MODIFY ("OWNER_ID" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_QSK" MODIFY ("IND_DEBIT_CREDIT" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."Q_REP_BOOKING_QSK" MODIFY ("ID_FRONTA" NOT NULL ENABLE)
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: Q_REP_BOOKING_QSK.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: Q_REP_BOOKING_QSK.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: Q_REP_BOOKING_QSK.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: Q_REP_KONTROLA.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying Q_REP_KONTROLA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table Q_REP_KONTROLA
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."Q_REP_KONTROLA" ADD PRIMARY KEY ("ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"  ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: Q_REP_KONTROLA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: Q_REP_KONTROLA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: Q_REP_KONTROLA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: TMP_ID.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying TMP_ID.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table TMP_ID
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."TMP_ID" ADD PRIMARY KEY ("ID") ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: TMP_ID.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: TMP_ID.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: TMP_ID.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: TR_TABLE1.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying TR_TABLE1.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table TR_TABLE1
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."TR_TABLE1" ADD PRIMARY KEY ("ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"  ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: TR_TABLE1.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: TR_TABLE1.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: TR_TABLE1.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: T_AIX_POHYBY.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying T_AIX_POHYBY.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table T_AIX_POHYBY
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."T_AIX_POHYBY" MODIFY ("UDID" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."T_AIX_POHYBY" MODIFY ("UPID" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."T_AIX_POHYBY" MODIFY ("DATUM" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."T_AIX_POHYBY" MODIFY ("UCET" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."T_AIX_POHYBY" MODIFY ("PROTIUCET" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."T_AIX_POHYBY" MODIFY ("CASTKA" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."T_AIX_POHYBY" MODIFY ("MENA" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."T_AIX_POHYBY" MODIFY ("CASTKAMENY" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."T_AIX_POHYBY" MODIFY ("JEDNOTKA" NOT NULL ENABLE)
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: T_AIX_POHYBY.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: T_AIX_POHYBY.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: T_AIX_POHYBY.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: T_AIX_POHYBY_MEZZANINE.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying T_AIX_POHYBY_MEZZANINE.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table T_AIX_POHYBY_MEZZANINE
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."T_AIX_POHYBY_MEZZANINE" MODIFY ("UDID" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."T_AIX_POHYBY_MEZZANINE" MODIFY ("UPID" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."T_AIX_POHYBY_MEZZANINE" MODIFY ("DATUM" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."T_AIX_POHYBY_MEZZANINE" MODIFY ("UCET" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."T_AIX_POHYBY_MEZZANINE" MODIFY ("PROTIUCET" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."T_AIX_POHYBY_MEZZANINE" MODIFY ("CASTKA" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."T_AIX_POHYBY_MEZZANINE" MODIFY ("MENA" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."T_AIX_POHYBY_MEZZANINE" MODIFY ("CASTKAMENY" NOT NULL ENABLE);
  ALTER TABLE "DB_DSA"."T_AIX_POHYBY_MEZZANINE" MODIFY ("JEDNOTKA" NOT NULL ENABLE)
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: T_AIX_POHYBY_MEZZANINE.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: T_AIX_POHYBY_MEZZANINE.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: T_AIX_POHYBY_MEZZANINE.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: T_UCET_POHYBY.sql
-- Category: constraints
-- ============================================================================

PROMPT Deploying T_UCET_POHYBY.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Constraints for Table T_UCET_POHYBY
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."T_UCET_POHYBY" MODIFY ("ID_FRONTA" CONSTRAINT "NN_REPPOHYBY_FRONTA" NOT NULL ENABLE)
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: T_UCET_POHYBY.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: T_UCET_POHYBY.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: T_UCET_POHYBY.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


PROMPT Completed constraints deployment
PROMPT


-- ============================================================================
-- PHASE: Deploy FOREIGN KEYS (2 files)
-- ============================================================================

PROMPT
PROMPT ============================================================================
PROMPT Deploying FOREIGN KEYS (2 files)
PROMPT ============================================================================
PROMPT


-- ============================================================================
-- Deploy: PLSQL_PROFILER_DATA.sql
-- Category: foreign keys
-- ============================================================================

PROMPT Deploying PLSQL_PROFILER_DATA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Ref Constraints for Table PLSQL_PROFILER_DATA
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."PLSQL_PROFILER_DATA" ADD FOREIGN KEY ("RUNID", "UNIT_NUMBER")
	  REFERENCES "DB_DSA"."PLSQL_PROFILER_UNITS" ("RUNID", "UNIT_NUMBER") ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PLSQL_PROFILER_DATA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PLSQL_PROFILER_DATA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PLSQL_PROFILER_DATA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PLSQL_PROFILER_UNITS.sql
-- Category: foreign keys
-- ============================================================================

PROMPT Deploying PLSQL_PROFILER_UNITS.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  Ref Constraints for Table PLSQL_PROFILER_UNITS
--------------------------------------------------------

  ALTER TABLE "DB_DSA"."PLSQL_PROFILER_UNITS" ADD FOREIGN KEY ("RUNID")
	  REFERENCES "DB_DSA"."PLSQL_PROFILER_RUNS" ("RUNID") ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PLSQL_PROFILER_UNITS.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PLSQL_PROFILER_UNITS.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PLSQL_PROFILER_UNITS.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


PROMPT Completed foreign keys deployment
PROMPT


-- ============================================================================
-- PHASE: Deploy INDEXES (53 files)
-- ============================================================================

PROMPT
PROMPT ============================================================================
PROMPT Deploying INDEXES (53 files)
PROMPT ============================================================================
PROMPT


-- ============================================================================
-- Deploy: AAA111.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying AAA111.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index AAA111
--------------------------------------------------------

  CREATE INDEX "DB_DSA"."AAA111" ON "DB_DSA"."KP_S_SEP" ("RC") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: AAA111.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: AAA111.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: AAA111.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: BBB222.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying BBB222.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index BBB222
--------------------------------------------------------

  CREATE INDEX "DB_DSA"."BBB222" ON "DB_DSA"."KP_S_SEP" ("ICO") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: BBB222.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: BBB222.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: BBB222.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: IU_KPMANUALTEXT.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying IU_KPMANUALTEXT.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index IU_KPMANUALTEXT
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."IU_KPMANUALTEXT" ON "DB_DSA"."KP_S_MANUALTEXT" ("S_DBLINK", "S_TEXT") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: IU_KPMANUALTEXT.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: IU_KPMANUALTEXT.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: IU_KPMANUALTEXT.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: IU_KPRADICIMPORT.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying IU_KPRADICIMPORT.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index IU_KPRADICIMPORT
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."IU_KPRADICIMPORT" ON "DB_DSA"."KP_RADICIMPORTU" ("S_DBLINK") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: IU_KPRADICIMPORT.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: IU_KPRADICIMPORT.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: IU_KPRADICIMPORT.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: IX_INVALID_RKC_ICO.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying IX_INVALID_RKC_ICO.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index IX_INVALID_RKC_ICO
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."IX_INVALID_RKC_ICO" ON "DB_DSA"."KP_CIS_RKC_INVALID_ICO_QUA" ("INVALID_RKC_ICO") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: IX_INVALID_RKC_ICO.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: IX_INVALID_RKC_ICO.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: IX_INVALID_RKC_ICO.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: IX_KPSPINFIRMA_MANDANT.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying IX_KPSPINFIRMA_MANDANT.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index IX_KPSPINFIRMA_MANDANT
--------------------------------------------------------

  CREATE INDEX "DB_DSA"."IX_KPSPINFIRMA_MANDANT" ON "DB_DSA"."KP_S_SPIN_FIRMA" ("MANDANT_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 262144 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: IX_KPSPINFIRMA_MANDANT.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: IX_KPSPINFIRMA_MANDANT.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: IX_KPSPINFIRMA_MANDANT.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: IX_KP_REL_PROTISTRANA.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying IX_KP_REL_PROTISTRANA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index IX_KP_REL_PROTISTRANA
--------------------------------------------------------

  CREATE INDEX "DB_DSA"."IX_KP_REL_PROTISTRANA" ON "DB_DSA"."KP_REL_PROTISTRANA" ("ID_SPOLECNOST", "ID_DATABAZE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: IX_KP_REL_PROTISTRANA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: IX_KP_REL_PROTISTRANA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: IX_KP_REL_PROTISTRANA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: IX_KP_REL_PROTISTRANA_BANKA.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying IX_KP_REL_PROTISTRANA_BANKA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index IX_KP_REL_PROTISTRANA_BANKA
--------------------------------------------------------

  CREATE INDEX "DB_DSA"."IX_KP_REL_PROTISTRANA_BANKA" ON "DB_DSA"."KP_REL_PROTISTRANA" ("BANKA_ID", "ID_DATABAZE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: IX_KP_REL_PROTISTRANA_BANKA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: IX_KP_REL_PROTISTRANA_BANKA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: IX_KP_REL_PROTISTRANA_BANKA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: IX_KP_REL_PROTISTRANA_DOID.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying IX_KP_REL_PROTISTRANA_DOID.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index IX_KP_REL_PROTISTRANA_DOID
--------------------------------------------------------

  CREATE INDEX "DB_DSA"."IX_KP_REL_PROTISTRANA_DOID" ON "DB_DSA"."KP_REL_PROTISTRANA" ("DOID", "ID_DATABAZE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: IX_KP_REL_PROTISTRANA_DOID.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: IX_KP_REL_PROTISTRANA_DOID.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: IX_KP_REL_PROTISTRANA_DOID.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: IX_KP_REL_PROTISTRANA_EID.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying IX_KP_REL_PROTISTRANA_EID.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index IX_KP_REL_PROTISTRANA_EID
--------------------------------------------------------

  CREATE INDEX "DB_DSA"."IX_KP_REL_PROTISTRANA_EID" ON "DB_DSA"."KP_REL_PROTISTRANA" ("EID", "ID_DATABAZE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: IX_KP_REL_PROTISTRANA_EID.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: IX_KP_REL_PROTISTRANA_EID.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: IX_KP_REL_PROTISTRANA_EID.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: IX_KP_REL_PROTISTRANA_EO.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying IX_KP_REL_PROTISTRANA_EO.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index IX_KP_REL_PROTISTRANA_EO
--------------------------------------------------------

  CREATE INDEX "DB_DSA"."IX_KP_REL_PROTISTRANA_EO" ON "DB_DSA"."KP_REL_PROTISTRANA" ("EO_ID", "ID_DATABAZE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: IX_KP_REL_PROTISTRANA_EO.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: IX_KP_REL_PROTISTRANA_EO.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: IX_KP_REL_PROTISTRANA_EO.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: IX_KP_REL_PROTISTRANA_FIRMA.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying IX_KP_REL_PROTISTRANA_FIRMA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index IX_KP_REL_PROTISTRANA_FIRMA
--------------------------------------------------------

  CREATE INDEX "DB_DSA"."IX_KP_REL_PROTISTRANA_FIRMA" ON "DB_DSA"."KP_REL_PROTISTRANA" ("FIRMA_ID", "ID_DATABAZE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: IX_KP_REL_PROTISTRANA_FIRMA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: IX_KP_REL_PROTISTRANA_FIRMA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: IX_KP_REL_PROTISTRANA_FIRMA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: IX_KP_REL_PROTISTRANA_ICO.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying IX_KP_REL_PROTISTRANA_ICO.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index IX_KP_REL_PROTISTRANA_ICO
--------------------------------------------------------

  CREATE INDEX "DB_DSA"."IX_KP_REL_PROTISTRANA_ICO" ON "DB_DSA"."KP_REL_PROTISTRANA" ("S_ICO", "ID_DATABAZE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: IX_KP_REL_PROTISTRANA_ICO.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: IX_KP_REL_PROTISTRANA_ICO.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: IX_KP_REL_PROTISTRANA_ICO.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: IX_KP_REL_PROTISTRANA_KID.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying IX_KP_REL_PROTISTRANA_KID.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index IX_KP_REL_PROTISTRANA_KID
--------------------------------------------------------

  CREATE INDEX "DB_DSA"."IX_KP_REL_PROTISTRANA_KID" ON "DB_DSA"."KP_REL_PROTISTRANA" ("KID", "ID_DATABAZE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: IX_KP_REL_PROTISTRANA_KID.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: IX_KP_REL_PROTISTRANA_KID.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: IX_KP_REL_PROTISTRANA_KID.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: IX_KP_REL_PROTISTRANA_NUMBER0.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying IX_KP_REL_PROTISTRANA_NUMBER0.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index IX_KP_REL_PROTISTRANA_NUMBER0
--------------------------------------------------------

  CREATE INDEX "DB_DSA"."IX_KP_REL_PROTISTRANA_NUMBER0" ON "DB_DSA"."KP_REL_PROTISTRANA" ("S_NUMBER0") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: IX_KP_REL_PROTISTRANA_NUMBER0.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: IX_KP_REL_PROTISTRANA_NUMBER0.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: IX_KP_REL_PROTISTRANA_NUMBER0.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: IX_KP_REL_PROTISTRANA_PLATINST.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying IX_KP_REL_PROTISTRANA_PLATINST.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index IX_KP_REL_PROTISTRANA_PLATINST
--------------------------------------------------------

  CREATE INDEX "DB_DSA"."IX_KP_REL_PROTISTRANA_PLATINST" ON "DB_DSA"."KP_REL_PROTISTRANA" ("PLATINST", "ID_DATABAZE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: IX_KP_REL_PROTISTRANA_PLATINST.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: IX_KP_REL_PROTISTRANA_PLATINST.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: IX_KP_REL_PROTISTRANA_PLATINST.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: IX_QREPBOOKING_BOOKNUMBER.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying IX_QREPBOOKING_BOOKNUMBER.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index IX_QREPBOOKING_BOOKNUMBER
--------------------------------------------------------

  CREATE INDEX "DB_DSA"."IX_QREPBOOKING_BOOKNUMBER" ON "DB_DSA"."Q_REP_BOOKING" ("BOOK_NUMBER") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: IX_QREPBOOKING_BOOKNUMBER.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: IX_QREPBOOKING_BOOKNUMBER.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: IX_QREPBOOKING_BOOKNUMBER.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: IX_QREPBOOKING_BOOKNUMBER_2.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying IX_QREPBOOKING_BOOKNUMBER_2.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index IX_QREPBOOKING_BOOKNUMBER_2
--------------------------------------------------------

  CREATE INDEX "DB_DSA"."IX_QREPBOOKING_BOOKNUMBER_2" ON "DB_DSA"."Q_REP_BOOKING" ("EFFECTIVE_DATE", "BOOK_NUMBER") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: IX_QREPBOOKING_BOOKNUMBER_2.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: IX_QREPBOOKING_BOOKNUMBER_2.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: IX_QREPBOOKING_BOOKNUMBER_2.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: IX_QREPBOOKING_ID_BUDGET.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying IX_QREPBOOKING_ID_BUDGET.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index IX_QREPBOOKING_ID_BUDGET
--------------------------------------------------------

  CREATE INDEX "DB_DSA"."IX_QREPBOOKING_ID_BUDGET" ON "DB_DSA"."Q_REP_BOOKING" ("ID_KTGODBOR", "GL_SYNTETIC", "EFFECTIVE_DATE", "POCATECNISTAV") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: IX_QREPBOOKING_ID_BUDGET.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: IX_QREPBOOKING_ID_BUDGET.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: IX_QREPBOOKING_ID_BUDGET.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: IX_QREPBOOKING_ID_ODBOR.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying IX_QREPBOOKING_ID_ODBOR.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index IX_QREPBOOKING_ID_ODBOR
--------------------------------------------------------

  CREATE INDEX "DB_DSA"."IX_QREPBOOKING_ID_ODBOR" ON "DB_DSA"."Q_REP_BOOKING" ("ID_KTGODBOR") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: IX_QREPBOOKING_ID_ODBOR.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: IX_QREPBOOKING_ID_ODBOR.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: IX_QREPBOOKING_ID_ODBOR.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: IX_QREPBOOKING_UCET.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying IX_QREPBOOKING_UCET.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index IX_QREPBOOKING_UCET
--------------------------------------------------------

  CREATE INDEX "DB_DSA"."IX_QREPBOOKING_UCET" ON "DB_DSA"."Q_REP_BOOKING" ("GL_SYNTETIC") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: IX_QREPBOOKING_UCET.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: IX_QREPBOOKING_UCET.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: IX_QREPBOOKING_UCET.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: IX_REPBOOKING.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying IX_REPBOOKING.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index IX_REPBOOKING
--------------------------------------------------------

  CREATE INDEX "DB_DSA"."IX_REPBOOKING" ON "DB_DSA"."Q_REP_BOOKING" ("ID_FRONTA") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: IX_REPBOOKING.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: IX_REPBOOKING.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: IX_REPBOOKING.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: IX_TMPZDROJDAT.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying IX_TMPZDROJDAT.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index IX_TMPZDROJDAT
--------------------------------------------------------

  CREATE INDEX "DB_DSA"."IX_TMPZDROJDAT" ON "DB_DSA"."KP_TMP_ZDROJDAT" ("ID_FRONTA") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: IX_TMPZDROJDAT.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: IX_TMPZDROJDAT.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: IX_TMPZDROJDAT.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: IX_TUCETPOHYBYSTREDISKO.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying IX_TUCETPOHYBYSTREDISKO.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index IX_TUCETPOHYBYSTREDISKO
--------------------------------------------------------

  CREATE INDEX "DB_DSA"."IX_TUCETPOHYBYSTREDISKO" ON "DB_DSA"."T_UCET_POHYBY" ("STREDISKO") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 12976128 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: IX_TUCETPOHYBYSTREDISKO.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: IX_TUCETPOHYBYSTREDISKO.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: IX_TUCETPOHYBYSTREDISKO.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: IX_TUCETPOHYBYSTREDISKO_U.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying IX_TUCETPOHYBYSTREDISKO_U.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index IX_TUCETPOHYBYSTREDISKO_U
--------------------------------------------------------

  CREATE INDEX "DB_DSA"."IX_TUCETPOHYBYSTREDISKO_U" ON "DB_DSA"."T_UCET_POHYBY" ("UCET") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: IX_TUCETPOHYBYSTREDISKO_U.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: IX_TUCETPOHYBYSTREDISKO_U.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: IX_TUCETPOHYBYSTREDISKO_U.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: IX_UCETPOHYBY.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying IX_UCETPOHYBY.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index IX_UCETPOHYBY
--------------------------------------------------------

  CREATE INDEX "DB_DSA"."IX_UCETPOHYBY" ON "DB_DSA"."T_UCET_POHYBY" ("ID_FRONTA") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: IX_UCETPOHYBY.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: IX_UCETPOHYBY.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: IX_UCETPOHYBY.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: MAIL_NOTIFICATION_1_UNIQUE.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying MAIL_NOTIFICATION_1_UNIQUE.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index MAIL_NOTIFICATION_1_UNIQUE
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."MAIL_NOTIFICATION_1_UNIQUE" ON "DB_DSA"."ADMIN_MAIL_NOTIFICATION" ("USER_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: MAIL_NOTIFICATION_1_UNIQUE.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: MAIL_NOTIFICATION_1_UNIQUE.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: MAIL_NOTIFICATION_1_UNIQUE.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PK_KPKTGPROTISTRANAVYJIMKY.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying PK_KPKTGPROTISTRANAVYJIMKY.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index PK_KPKTGPROTISTRANAVYJIMKY
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."PK_KPKTGPROTISTRANAVYJIMKY" ON "DB_DSA"."KP_KTG_PROTISTRANAVYJIMKY" ("ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PK_KPKTGPROTISTRANAVYJIMKY.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PK_KPKTGPROTISTRANAVYJIMKY.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PK_KPKTGPROTISTRANAVYJIMKY.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PK_KPRADICIMPORT.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying PK_KPRADICIMPORT.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index PK_KPRADICIMPORT
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."PK_KPRADICIMPORT" ON "DB_DSA"."KP_RADICIMPORTU" ("ID_FRONTA") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PK_KPRADICIMPORT.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PK_KPRADICIMPORT.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PK_KPRADICIMPORT.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PK_KPREL_PROTISTRANA.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying PK_KPREL_PROTISTRANA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index PK_KPREL_PROTISTRANA
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."PK_KPREL_PROTISTRANA" ON "DB_DSA"."KP_REL_PROTISTRANA" ("ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 393216 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PK_KPREL_PROTISTRANA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PK_KPREL_PROTISTRANA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PK_KPREL_PROTISTRANA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PK_KPSMANUAL.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying PK_KPSMANUAL.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index PK_KPSMANUAL
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."PK_KPSMANUAL" ON "DB_DSA"."KP_S_MANUAL" ("S_DBLINK", "S_EXTID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PK_KPSMANUAL.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PK_KPSMANUAL.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PK_KPSMANUAL.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PK_KPSMANUALTEXT.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying PK_KPSMANUALTEXT.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index PK_KPSMANUALTEXT
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."PK_KPSMANUALTEXT" ON "DB_DSA"."KP_S_MANUALTEXT" ("ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PK_KPSMANUALTEXT.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PK_KPSMANUALTEXT.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PK_KPSMANUALTEXT.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PK_KPSSEP.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying PK_KPSSEP.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index PK_KPSSEP
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."PK_KPSSEP" ON "DB_DSA"."KP_S_SEP" ("SEP_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PK_KPSSEP.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PK_KPSSEP.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PK_KPSSEP.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PK_KPSSPINEKOBJ.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying PK_KPSSPINEKOBJ.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index PK_KPSSPINEKOBJ
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."PK_KPSSPINEKOBJ" ON "DB_DSA"."KP_S_SPIN_EK_OBJEKT" ("EO_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 262144 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PK_KPSSPINEKOBJ.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PK_KPSSPINEKOBJ.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PK_KPSSPINEKOBJ.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PK_KPSSPINEOFIRMA.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying PK_KPSSPINEOFIRMA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index PK_KPSSPINEOFIRMA
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."PK_KPSSPINEOFIRMA" ON "DB_DSA"."KP_S_SPIN_EO_FIRMA" ("EO_ID", "FIRMA_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PK_KPSSPINEOFIRMA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PK_KPSSPINEOFIRMA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PK_KPSSPINEOFIRMA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PK_KPSSPINFIRMA.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying PK_KPSSPINFIRMA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index PK_KPSSPINFIRMA
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."PK_KPSSPINFIRMA" ON "DB_DSA"."KP_S_SPIN_FIRMA" ("FIRMA_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PK_KPSSPINFIRMA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PK_KPSSPINFIRMA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PK_KPSSPINFIRMA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PK_KPSSPINPUSTAV.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying PK_KPSSPINPUSTAV.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index PK_KPSSPINPUSTAV
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."PK_KPSSPINPUSTAV" ON "DB_DSA"."KP_S_SPIN_PEN_USTAV" ("BANKA_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PK_KPSSPINPUSTAV.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PK_KPSSPINPUSTAV.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PK_KPSSPINPUSTAV.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PK_KPSTOPASDODODBER.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying PK_KPSTOPASDODODBER.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index PK_KPSTOPASDODODBER
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."PK_KPSTOPASDODODBER" ON "DB_DSA"."KP_S_TOPAS_DODODBER" ("S_DBLINK", "DOID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PK_KPSTOPASDODODBER.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PK_KPSTOPASDODODBER.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PK_KPSTOPASDODODBER.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PK_KPSTOPASEMITENTI.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying PK_KPSTOPASEMITENTI.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index PK_KPSTOPASEMITENTI
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."PK_KPSTOPASEMITENTI" ON "DB_DSA"."KP_S_TOPAS_EMITENTI" ("S_DBLINK", "EID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 393216 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PK_KPSTOPASEMITENTI.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PK_KPSTOPASEMITENTI.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PK_KPSTOPASEMITENTI.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PK_KPSTOPASKLIENT.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying PK_KPSTOPASKLIENT.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index PK_KPSTOPASKLIENT
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."PK_KPSTOPASKLIENT" ON "DB_DSA"."KP_S_TOPAS_KLIENTI" ("S_DBLINK", "KID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PK_KPSTOPASKLIENT.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PK_KPSTOPASKLIENT.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PK_KPSTOPASKLIENT.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PK_KPSTOPASPLATINST.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying PK_KPSTOPASPLATINST.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index PK_KPSTOPASPLATINST
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."PK_KPSTOPASPLATINST" ON "DB_DSA"."KP_S_TOPAS_PLATINSTITUCE" ("S_DBLINK", "PLATINST") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PK_KPSTOPASPLATINST.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PK_KPSTOPASPLATINST.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PK_KPSTOPASPLATINST.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PK_KP_S_SEP_STALKER.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying PK_KP_S_SEP_STALKER.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index PK_KP_S_SEP_STALKER
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."PK_KP_S_SEP_STALKER" ON "DB_DSA"."KP_S_SEP_STALKER" ("STALKER_SEPID", "SEP_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PK_KP_S_SEP_STALKER.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PK_KP_S_SEP_STALKER.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PK_KP_S_SEP_STALKER.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PK_LOG_QUAESTOR_CUST_GROUP.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying PK_LOG_QUAESTOR_CUST_GROUP.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index PK_LOG_QUAESTOR_CUST_GROUP
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."PK_LOG_QUAESTOR_CUST_GROUP" ON "DB_DSA"."KP_LOG_QUAESTOR_CUST_GROUP" ("ID_LOG", "DT_LOG", "NUMBER0", "CUST_GROUP_CODE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PK_LOG_QUAESTOR_CUST_GROUP.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PK_LOG_QUAESTOR_CUST_GROUP.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PK_LOG_QUAESTOR_CUST_GROUP.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PK_LOG_QUAESTOR_RELATION.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying PK_LOG_QUAESTOR_RELATION.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index PK_LOG_QUAESTOR_RELATION
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."PK_LOG_QUAESTOR_RELATION" ON "DB_DSA"."KP_LOG_QUAESTOR_RELATION" ("ID_LOG", "DT_LOG", "NUMBER0") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PK_LOG_QUAESTOR_RELATION.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PK_LOG_QUAESTOR_RELATION.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PK_LOG_QUAESTOR_RELATION.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PK_QREPBOOKING.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying PK_QREPBOOKING.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index PK_QREPBOOKING
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."PK_QREPBOOKING" ON "DB_DSA"."Q_REP_BOOKING" ("ID_FRONTA", "BOOKING_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PK_QREPBOOKING.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PK_QREPBOOKING.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PK_QREPBOOKING.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PK_RKC_INVALID_ICO.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying PK_RKC_INVALID_ICO.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index PK_RKC_INVALID_ICO
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."PK_RKC_INVALID_ICO" ON "DB_DSA"."KP_CIS_RKC_INVALID_ICO_QUA" ("FIKTIVNI_NUMBER0") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PK_RKC_INVALID_ICO.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PK_RKC_INVALID_ICO.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PK_RKC_INVALID_ICO.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: SYS_C0014747.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying SYS_C0014747.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index SYS_C0014747
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."SYS_C0014747" ON "DB_DSA"."PLSQL_PROFILER_RUNS" ("RUNID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: SYS_C0014747.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: SYS_C0014747.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: SYS_C0014747.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: SYS_C0014749.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying SYS_C0014749.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index SYS_C0014749
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."SYS_C0014749" ON "DB_DSA"."PLSQL_PROFILER_UNITS" ("RUNID", "UNIT_NUMBER") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: SYS_C0014749.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: SYS_C0014749.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: SYS_C0014749.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: SYS_C0014752.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying SYS_C0014752.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index SYS_C0014752
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."SYS_C0014752" ON "DB_DSA"."PLSQL_PROFILER_DATA" ("RUNID", "UNIT_NUMBER", "LINE#") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: SYS_C0014752.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: SYS_C0014752.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: SYS_C0014752.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: SYS_C0048929.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying SYS_C0048929.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index SYS_C0048929
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."SYS_C0048929" ON "DB_DSA"."TR_TABLE1" ("ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: SYS_C0048929.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: SYS_C0048929.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: SYS_C0048929.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: SYS_C005349.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying SYS_C005349.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index SYS_C005349
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."SYS_C005349" ON "DB_DSA"."Q_REP_KONTROLA" ("ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: SYS_C005349.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: SYS_C005349.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: SYS_C005349.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: SYS_C005351.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying SYS_C005351.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index SYS_C005351
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."SYS_C005351" ON "DB_DSA"."TMP_ID" ("ID")
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: SYS_C005351.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: SYS_C005351.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: SYS_C005351.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: UX_KPKTGPROTIVYJIMKY.sql
-- Category: indexes
-- ============================================================================

PROMPT Deploying UX_KPKTGPROTIVYJIMKY.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Index UX_KPKTGPROTIVYJIMKY
--------------------------------------------------------

  CREATE UNIQUE INDEX "DB_DSA"."UX_KPKTGPROTIVYJIMKY" ON "DB_DSA"."KP_KTG_PROTISTRANAVYJIMKY" ("ID_DATABAZE", "S_UCETMASKA") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 131072 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "DSA"
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: UX_KPKTGPROTIVYJIMKY.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: UX_KPKTGPROTIVYJIMKY.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: UX_KPKTGPROTIVYJIMKY.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


PROMPT Completed indexes deployment
PROMPT


-- ============================================================================
-- PHASE: Deploy VIEWS (2 files)
-- ============================================================================

PROMPT
PROMPT ============================================================================
PROMPT Deploying VIEWS (2 files)
PROMPT ============================================================================
PROMPT


-- ============================================================================
-- Deploy: DWT_KTG_PROTISTRANAVYJIMKY.sql
-- Category: views
-- ============================================================================

PROMPT Deploying DWT_KTG_PROTISTRANAVYJIMKY.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for View DWT_KTG_PROTISTRANAVYJIMKY
--------------------------------------------------------

  CREATE OR REPLACE FORCE EDITIONABLE VIEW "DB_DSA"."DWT_KTG_PROTISTRANAVYJIMKY" ("ID", "ID_DATABAZE", "S_UCETMASKA", "S_PROTISTRANA", "NL_POZICEOD") AS 
  select "ID","ID_DATABAZE","S_UCETMASKA","S_PROTISTRANA","NL_POZICEOD" from DB_DSA.KP_KTG_PROTISTRANAVYJIMKY
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: DWT_KTG_PROTISTRANAVYJIMKY.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: DWT_KTG_PROTISTRANAVYJIMKY.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: DWT_KTG_PROTISTRANAVYJIMKY.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: DWT_REL_PROTISTRANA.sql
-- Category: views
-- ============================================================================

PROMPT Deploying DWT_REL_PROTISTRANA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for View DWT_REL_PROTISTRANA
--------------------------------------------------------

  CREATE OR REPLACE FORCE EDITIONABLE VIEW "DB_DSA"."DWT_REL_PROTISTRANA" ("ID", "ID_DATABAZE", "ID_SPOLECNOST", "S_ICO", "S_NAZEV", "S_NUMBER0", "FIRMA_ID", "EO_ID", "BANKA_ID", "DOID", "KID", "PLATINST", "AKCE", "DT_ZMENA", "S_POM_ICO", "EID", "DATUMZALOZENI", "ULICE", "PSC", "MESTO", "STAT", "DIC", "UCETSCP", "REGRMS", "EKONOM_SEKTOR", "TYPOSOBY") AS 
  select "ID","ID_DATABAZE","ID_SPOLECNOST","S_ICO","S_NAZEV","S_NUMBER0","FIRMA_ID","EO_ID","BANKA_ID","DOID","KID","PLATINST","AKCE","DT_ZMENA","S_POM_ICO","EID","DATUMZALOZENI","ULICE","PSC","MESTO","STAT","DIC","UCETSCP","REGRMS","EKONOM_SEKTOR","TYPOSOBY" from DB_DSA.KP_REL_PROTISTRANA           
  where 
    id_databaze in (  
    --100,105,300,320, 325
    select id from db_jt.kp_cis_databaze where c_aktivni = 1
                                                and id != 900 --esc 13.09 2019 z 987452  na 418890 zazn.
    )
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: DWT_REL_PROTISTRANA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: DWT_REL_PROTISTRANA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: DWT_REL_PROTISTRANA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


PROMPT Completed views deployment
PROMPT


-- ============================================================================
-- PHASE: Deploy FUNCTIONS (7 files)
-- ============================================================================

PROMPT
PROMPT ============================================================================
PROMPT Deploying FUNCTIONS (7 files)
PROMPT ============================================================================
PROMPT


-- ============================================================================
-- Deploy: F_FORMATICO.sql
-- Category: functions
-- ============================================================================

PROMPT Deploying F_FORMATICO.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Function F_FORMATICO
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE FUNCTION "DB_DSA"."F_FORMATICO" ( sIco varchar2 ) return varchar2 DETERMINISTIC as
	ret varchar2(20) := '???';
begin
	if ( sIco is null ) then
		return ( ret );
	end if;

--	if ( length ( sIco ) > 9 ) then
-- smrM 04.06.2020
	if ( length ( trim(sIco) ) > 9 ) then
--		return ( sIco );
-- esc 19.01.2011
      return trim(sIco)  ;
	end if;

	--return ( substr ( '000000000' || sIco, -10 ) );
  return ( substr ( '000000000' || trim(sIco), -10 ) );
end
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: F_FORMATICO.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: F_FORMATICO.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: F_FORMATICO.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: F_FORMATNAZEV.sql
-- Category: functions
-- ============================================================================

PROMPT Deploying F_FORMATNAZEV.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Function F_FORMATNAZEV
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE FUNCTION "DB_DSA"."F_FORMATNAZEV" ( sNazev varchar2 ) return varchar2 as
    ret varchar2(200);
begin

    ret := sNazev;

    -- ENTER nahradim s SPACE (Chr(10) \n New Line	; Chr(13) \r Carriage Return); Chr(9) Horizontal tab
    ret := REPLACE(REPLACE(REPLACE(ret, Chr(9), ' '), Chr(10), ' '), Chr(13), ' ');
    -- duplikovane SPACE odstranim
    ret := REGEXP_REPLACE(ret, ' {2,}', ' ');
    -- zrusim medzeru na zaciatku a na konci
    ret := TRIM(ret);

    return ( ret );
end
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: F_FORMATNAZEV.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: F_FORMATNAZEV.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: F_FORMATNAZEV.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: F_GETLASTCLOSEDATE.sql
-- Category: functions
-- ============================================================================

PROMPT Deploying F_GETLASTCLOSEDATE.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Function F_GETLASTCLOSEDATE
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE FUNCTION "DB_DSA"."F_GETLASTCLOSEDATE" ( aIdSpolecnost int ) return date as
	row_kpKtgUcetSpolecnost db_jt.kp_ktg_UcetniSpolecnost%ROWTYPE;
	l_code int;
begin
	select * into row_kpKtgUcetSpolecnost
	from db_jt.kp_ktg_UcetniSpolecnost
	where id = aIdSpolecnost;

	if ( row_kpKtgUcetSpolecnost.c_extSystem = 'T' ) then
		return ( f_getLastCloseDateTopas ( row_kpKtgUcetSpolecnost.topas_id, row_kpKtgUcetSpolecnost.s_dbLink ) );
	elsif ( row_kpKtgUcetSpolecnost.c_extSystem = 'S' ) then
		return ( row_kpKtgUcetSpolecnost.dt_lastCloseDate );	
--		return ( f_getLastCloseDate@spin.world ( row_kpKtgUcetSpolecnost.spin_id ) );
	elsif ( row_kpKtgUcetSpolecnost.c_extSystem = 'Q' ) then
		return ( f_getLastCloseDateQuaestor ( row_kpKtgUcetSpolecnost.s_dbLink ) );
	elsif ( row_kpKtgUcetSpolecnost.c_extSystem in ( 'M', 'I') ) then
		return ( null );
		--return ( to_date ( '1.1.1999', 'dd.mm.yyyy' ) );
	end if;

	raise_application_error ( -20000, 'Tak tady neni kod ' || aIdSpolecnost );

	exception when NO_DATA_FOUND then
		--raise_application_error ( -20001, 'Close date: not found : idSpolecnost ' || aIdSpolecnost );
		return ( null );
	when others then
		l_code := SQLCODE;
		if ( l_code not in ( -2068, -12500, -12541 ) ) then
			-- neco jineho nez nedostupna databaze
			raise;
		end if;

		return ( to_date ( '1.1.1999', 'dd.mm.yyyy') );
end
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: F_GETLASTCLOSEDATE.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: F_GETLASTCLOSEDATE.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: F_GETLASTCLOSEDATE.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: F_GETLASTCLOSEDATEQUAESTOR.sql
-- Category: functions
-- ============================================================================

PROMPT Deploying F_GETLASTCLOSEDATEQUAESTOR.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Function F_GETLASTCLOSEDATEQUAESTOR
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE FUNCTION "DB_DSA"."F_GETLASTCLOSEDATEQUAESTOR" ( aDBLink varchar2 ) return date as
	ret date;
	sqlStmt varchar2(512);
begin
	sqlStmt := 'select max(date_upto) from quaestor.gl_accnt_period@' || aDBLink  ||' where status_gl = ''C''';

	execute immediate sqlStmt into ret;

	return ret;
end
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: F_GETLASTCLOSEDATEQUAESTOR.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: F_GETLASTCLOSEDATEQUAESTOR.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: F_GETLASTCLOSEDATEQUAESTOR.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: F_GETLASTCLOSEDATETOPAS.sql
-- Category: functions
-- ============================================================================

PROMPT Deploying F_GETLASTCLOSEDATETOPAS.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Function F_GETLASTCLOSEDATETOPAS
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE FUNCTION "DB_DSA"."F_GETLASTCLOSEDATETOPAS" ( aIdStredisko varchar2, aDBLink varchar2 ) return date as
	ret date;
	sqlStmt varchar2(512);
begin
		sqlStmt :=
			'select Max(datumdo1) from sysadm.datauzaverek@' || aDBLink  ||
			' where stredisko = ''' || aIdStredisko || '''';

		execute immediate sqlStmt into ret;

		return ret;
end
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: F_GETLASTCLOSEDATETOPAS.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: F_GETLASTCLOSEDATETOPAS.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: F_GETLASTCLOSEDATETOPAS.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/

-- ERROR: Cannot read /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/FUNCTIONS/F_GETRELPROTISTRANA.sql: 'utf-8' codec can't decode byte 0xc8 in position 570: invalid continuation byte

-- ERROR: Cannot read /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/FUNCTIONS/F_GET_DB_NAME.sql: 'utf-8' codec can't decode byte 0x9e in position 315: invalid start byte


PROMPT Completed functions deployment
PROMPT


-- ============================================================================
-- PHASE: Deploy PROCEDURES (12 files)
-- ============================================================================

PROMPT
PROMPT ============================================================================
PROMPT Deploying PROCEDURES (12 files)
PROMPT ============================================================================
PROMPT


-- ============================================================================
-- Deploy: PPP.sql
-- Category: procedures
-- ============================================================================

PROMPT Deploying PPP.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Procedure PPP
--------------------------------------------------------
set define off;

  CREATE OR REPLACE EDITIONABLE PROCEDURE "DB_DSA"."PPP" AS
 i integer;
 x integer;
begin
--	insert into t_ucet_pohyby ( udid, upid, m1d0, datum, ucet, stredisko, protiucet, protistredisko, text, castka, mena, castkameny, kurz, jednotka, doid, parznak, datumsplat, zakazka, cinnost, xidsl, topzkratka,  emitent_id, emitent_ICO, emitent_zkratka, pocatecniStav, id_ktgProjekt, id_ktgOdbor, id_Fronta  )select u.udid, u.upid, u.m1d0, trunc(u.datum), u.ucet, u.stredisko, u.protiucet, u.protistredisko, substr(u.Text,1,50), u.castka, u.mena, u.castkameny, u.kurz, u.jednotka, u.doid, u.parznak, u.datumsplat, u.zakazka, u.cinnost, u.xidsl, u.topzkratka,  case when ( u.zakazka is null ) then null else read.f_zakazkaEmitent@TopAsset ( 1, u.zakazka ) end,case when ( u.zakazka is null ) then null else read.f_zakazkaEmitent@TopAsset ( 2, u.zakazka ) end,case when ( u.zakazka is null ) then null else read.f_zakazkaEmitent@TopAsset ( 3, u.zakazka ) end,'0', u.projekt_id, u.manazer_id, 324944 from ucetpohyby@TopAsset u  where stredisko = 'sss'  and ucet is not null and udid != 0;

/*
	insert into db_dsa.t_ucet_pohyby ( udid, upid, m1d0, datum, ucet, stredisko, protiucet, protistredisko, text, castka, 
   mena, castkameny, kurz, jednotka, doid, parznak, datumsplat, zakazka, cinnost, xidsl, topzkratka,  emitent_id,  
   emitent_ICO, emitent_zkratka, pocatecniStav, id_ktgProjekt, id_ktgOdbor, id_Fronta  )select u.udid, u.upid,   
   u.m1d0, trunc(u.datum), u.ucet, u.stredisko, u.protiucet, u.protistredisko, substr(u.Text,1,50), u.castka,   
   u.mena, u.castkameny, u.kurz, u.jednotka, u.doid, u.parznak, u.datumsplat, u.zakazka, u.cinnost, u.xidsl,  
   u.topzkratka,  case when ( u.zakazka is null ) then null else read.f_zakazkaEmitent@TopAsset ( 1, u.zakazka ) end,   
   case when ( u.zakazka is null ) then null else read.f_zakazkaEmitent@TopAsset ( 2, u.zakazka ) end,   
   case when ( u.zakazka is null ) then null else read.f_zakazkaEmitent@TopAsset ( 3, u.zakazka ) end,'0', 
   u.projekt_id, u.manazer_id, 335163 from gaapucetpohyby@TopAsset u  where stredisko = 'JTPBD'  and   
   ucet is not null and udid != 0 and datum between '1.1.2006' and '31.1.2006'; 
*/

/*
	insert into db_dsa.t_ucet_pohyby ( udid, upid, m1d0, datum, ucet, stredisko, protiucet, protistredisko, text, castka, 
   mena, castkameny, kurz, jednotka, doid, parznak, datumsplat, zakazka, cinnost, xidsl, topzkratka,  
	--emitent_id,   emitent_ICO, emitent_zkratka, 
	pocatecniStav, id_ktgProjekt, id_ktgOdbor, id_Fronta  )
	select u.udid, u.upid,   
   u.m1d0, trunc(u.datum), u.ucet, u.stredisko, u.protiucet, u.protistredisko, substr(u.Text,1,50), u.castka,   
   u.mena, u.castkameny, u.kurz, u.jednotka, u.doid, u.parznak, u.datumsplat, u.zakazka, u.cinnost, u.xidsl,  
   u.topzkratka,  
	
--	  case when ( u.zakazka is null ) then null else read.f_zakazkaEmitent@TopAsset ( 1, u.zakazka ) end,   
--   case when ( u.zakazka is null ) then null else read.f_zakazkaEmitent@TopAsset ( 2, u.zakazka ) end,   
--   case when ( u.zakazka is null ) then null else read.f_zakazkaEmitent@TopAsset ( 3, u.zakazka ) end,
	
	'0', 
   u.projekt_id, u.manazer_id, 335163 
	from gaapucetpohyby@TopAsset u  where stredisko = 'JTPBD'  and   
   ucet is not null and udid != 0 and datum between '1.1.2006' and '31.1.2006'; 
*/

	insert into db_dsa.t_ucet_pohyby ( udid, upid, id_Fronta  )
	select -1, -2, -999 
	from dual@topasset; 


commit;
END
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PPP.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PPP.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PPP.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PPP2.sql
-- Category: procedures
-- ============================================================================

PROMPT Deploying PPP2.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Procedure PPP2
--------------------------------------------------------
set define off;

  CREATE OR REPLACE EDITIONABLE PROCEDURE "DB_DSA"."PPP2" AS
 i integer;
 x integer;
 h varchar2(4000);
begin

--return;


execute immediate 
	'insert into t_ucet_pohyby ( udid, upid, m1d0, datum, ucet, stredisko, protiucet, protistredisko, text, castka, mena, castkameny, kurz, jednotka, doid, parznak, datumsplat, zakazka, cinnost, xidsl, topzkratka,  emitent_id, emitent_ICO, emitent_zkratka, pocatecniStav, id_ktgProjekt, id_ktgOdbor, id_Fronta  )select u.udid, u.upid, u.m1d0, trunc(u.datum), u.ucet, u.stredisko, u.protiucet, u.protistredisko, substr(u.Text,1,50), u.castka, u.mena, u.castkameny, u.kurz, u.jednotka, u.doid, u.parznak, u.datumsplat, u.zakazka, u.cinnost, u.xidsl, u.topzkratka,  case when ( u.zakazka is null ) then null else read.f_zakazkaEmitent@TopAsset ( 1, u.zakazka ) end,case when ( u.zakazka is null ) then null else read.f_zakazkaEmitent@TopAsset ( 2, u.zakazka ) end,case when ( u.zakazka is null ) then null else read.f_zakazkaEmitent@TopAsset ( 3, u.zakazka ) end, ''0'', u.projekt_id, u.manazer_id, 324944 from ucetpohyby@TopAsset u  where stredisko = ''sss''  and ucet is not null and udid != 0';
insert into db_jt.yyy ( hlaska ) values ( 'OK byl tady' );
commit;	

--raise_application_error ( -20111, 'xxxx simulace');
/*
exception when others then
			 h:= SQLERRM;
	insert into db_jt.yyy ( hlaska ) values ( h );		 
commit;
*/
END
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PPP2.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PPP2.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PPP2.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: PPP3.sql
-- Category: procedures
-- ============================================================================

PROMPT Deploying PPP3.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Procedure PPP3
--------------------------------------------------------
set define off;

  CREATE OR REPLACE EDITIONABLE PROCEDURE "DB_DSA"."PPP3" AS
 i integer;
 x integer;
begin
--	insert into t_ucet_pohyby ( udid, upid, m1d0, datum, ucet, stredisko, protiucet, protistredisko, text, castka, mena, castkameny, kurz, jednotka, doid, parznak, datumsplat, zakazka, cinnost, xidsl, topzkratka,  emitent_id, emitent_ICO, emitent_zkratka, pocatecniStav, id_ktgProjekt, id_ktgOdbor, id_Fronta  )select u.udid, u.upid, u.m1d0, trunc(u.datum), u.ucet, u.stredisko, u.protiucet, u.protistredisko, substr(u.Text,1,50), u.castka, u.mena, u.castkameny, u.kurz, u.jednotka, u.doid, u.parznak, u.datumsplat, u.zakazka, u.cinnost, u.xidsl, u.topzkratka,  case when ( u.zakazka is null ) then null else read.f_zakazkaEmitent@TopAsset ( 1, u.zakazka ) end,case when ( u.zakazka is null ) then null else read.f_zakazkaEmitent@TopAsset ( 2, u.zakazka ) end,case when ( u.zakazka is null ) then null else read.f_zakazkaEmitent@TopAsset ( 3, u.zakazka ) end,'0', u.projekt_id, u.manazer_id, 324944 from ucetpohyby@TopAsset u  where stredisko = 'sss'  and ucet is not null and udid != 0;

/*
	insert into db_dsa.t_ucet_pohyby ( udid, upid, m1d0, datum, ucet, stredisko, protiucet, protistredisko, text, castka, 
   mena, castkameny, kurz, jednotka, doid, parznak, datumsplat, zakazka, cinnost, xidsl, topzkratka,  emitent_id,  
   emitent_ICO, emitent_zkratka, pocatecniStav, id_ktgProjekt, id_ktgOdbor, id_Fronta  )select u.udid, u.upid,   
   u.m1d0, trunc(u.datum), u.ucet, u.stredisko, u.protiucet, u.protistredisko, substr(u.Text,1,50), u.castka,   
   u.mena, u.castkameny, u.kurz, u.jednotka, u.doid, u.parznak, u.datumsplat, u.zakazka, u.cinnost, u.xidsl,  
   u.topzkratka,  case when ( u.zakazka is null ) then null else read.f_zakazkaEmitent@TopAsset ( 1, u.zakazka ) end,   
   case when ( u.zakazka is null ) then null else read.f_zakazkaEmitent@TopAsset ( 2, u.zakazka ) end,   
   case when ( u.zakazka is null ) then null else read.f_zakazkaEmitent@TopAsset ( 3, u.zakazka ) end,'0', 
   u.projekt_id, u.manazer_id, 335163 from gaapucetpohyby@TopAsset u  where stredisko = 'JTPBD'  and   
   ucet is not null and udid != 0 and datum between '1.1.2006' and '31.1.2006'; 
*/

/*
	insert into db_dsa.t_ucet_pohyby ( udid, upid, m1d0, datum, ucet, stredisko, protiucet, protistredisko, text, castka, 
   mena, castkameny, kurz, jednotka, doid, parznak, datumsplat, zakazka, cinnost, xidsl, topzkratka,  
	--emitent_id,   emitent_ICO, emitent_zkratka, 
	pocatecniStav, id_ktgProjekt, id_ktgOdbor, id_Fronta  )
	select u.udid, u.upid,   
   u.m1d0, trunc(u.datum), u.ucet, u.stredisko, u.protiucet, u.protistredisko, substr(u.Text,1,50), u.castka,   
   u.mena, u.castkameny, u.kurz, u.jednotka, u.doid, u.parznak, u.datumsplat, u.zakazka, u.cinnost, u.xidsl,  
   u.topzkratka,  
	
--	  case when ( u.zakazka is null ) then null else read.f_zakazkaEmitent@TopAsset ( 1, u.zakazka ) end,   
--   case when ( u.zakazka is null ) then null else read.f_zakazkaEmitent@TopAsset ( 2, u.zakazka ) end,   
--   case when ( u.zakazka is null ) then null else read.f_zakazkaEmitent@TopAsset ( 3, u.zakazka ) end,
	
	'0', 
   u.projekt_id, u.manazer_id, 335163 
	from gaapucetpohyby@TopAsset u  where stredisko = 'JTPBD'  and   
   ucet is not null and udid != 0 and datum between '1.1.2006' and '31.1.2006'; 
*/

	insert into db_dsa.t_ucet_pohyby ( udid, upid, id_Fronta  )
	select -3, -3, -999 
	from dual@topczk; 


commit;
END
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: PPP3.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: PPP3.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: PPP3.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: P_EXECUTESQL.sql
-- Category: procedures
-- ============================================================================

PROMPT Deploying P_EXECUTESQL.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Procedure P_EXECUTESQL
--------------------------------------------------------
set define off;

  CREATE OR REPLACE EDITIONABLE PROCEDURE "DB_DSA"."P_EXECUTESQL" ( aPrikaz varchar2 ) AS
 i integer;
 x integer;
begin
	i:=dbms_sql.open_cursor;

	dbms_sql.parse(i, aPrikaz,dbms_sql.v7);
	x:= dbms_sql.execute(i);
	dbms_sql.close_cursor(i);

END
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: P_EXECUTESQL.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: P_EXECUTESQL.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: P_EXECUTESQL.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/

-- ERROR: Cannot read /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/PROCEDURES/P_EXEC_FINISH_AG.sql: 'utf-8' codec can't decode byte 0xed in position 1146: invalid continuation byte


-- ============================================================================
-- Deploy: P_RADICZADOSTIMPORT.sql
-- Category: procedures
-- ============================================================================

PROMPT Deploying P_RADICZADOSTIMPORT.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Procedure P_RADICZADOSTIMPORT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE EDITIONABLE PROCEDURE "DB_DSA"."P_RADICZADOSTIMPORT" ( aIdFronta int, aSDBLink varchar2, aStav out char ) as
	pom int;
	aktZadost kp_radicImportu%ROWTYPE;
begin

	while ( 1 = 1 ) loop

		lock table kp_radicImportu in exclusive mode;

		select count(*) into pom from kp_radicImportu where s_dbLink = aSDBLink;

		-- dbLink je unique takze bud jeden nebo zadny
		if ( pom = 1 ) then
			select * into aktZadost from kp_radicImportu where s_dbLink = aSDBLink;
			if ( aktZadost.id_fronta = aIdFronta ) then
				if ( aktZadost.c_stav = 'K' ) then
					aStav := aktZadost.c_stav;
					goto konec; -- data uz jsou pripravena
				end if;
			end if;
			-- v tabulkach jsou nejaka data ze stejne DB, ktera si nekdo pripravil a jeste nevyzvedl
			-- => musim cekat az si je odebere
		else
			-- OK muze generovat, zakladam pozadavek
			insert into kp_radicImportu ( id_fronta, s_dbLink, c_stav ) values ( aIdFronta, aSDBLink, 'I' );
			aStav := 'I';
			goto konec;
		end if;

		-- data se pripravuji musi se cekat
		commit; -- uvolni zamek
		dbms_lock.sleep( 1 );

	end loop;

	<<konec>> commit; -- uvolni zamek

end
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: P_RADICZADOSTIMPORT.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: P_RADICZADOSTIMPORT.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: P_RADICZADOSTIMPORT.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: P_RECOMPILEPACKAGE.sql
-- Category: procedures
-- ============================================================================

PROMPT Deploying P_RECOMPILEPACKAGE.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Procedure P_RECOMPILEPACKAGE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE EDITIONABLE PROCEDURE "DB_DSA"."P_RECOMPILEPACKAGE" as
begin
	for i in 1 .. 5 loop
		for cObject in (
			select object_name, owner
			from all_objects
			where status = 'INVALID' and object_type = 'PACKAGE BODY'
				and owner = 'DB_JT' and instr(object_name, '2') = 0 )
		loop
			begin
dbms_output.put_line ( 'alter package ' || cObject.owner || '.' || cObject.object_name || ' compile' );
				p_executesql ( 'alter package ' || cObject.owner || '.' || cObject.object_name || ' compile body' );
			exception when others then
				dbms_output.put_line ( sqlerrm );
			end;
		end loop;
	end loop;

end
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: P_RECOMPILEPACKAGE.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: P_RECOMPILEPACKAGE.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: P_RECOMPILEPACKAGE.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/

-- ERROR: Cannot read /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/PROCEDURES/P_SEND_EMAIL_ESS_CRM.sql: 'utf-8' codec can't decode byte 0x9d in position 1127: invalid start byte

-- ERROR: Cannot read /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/PROCEDURES/P_SEND_EMAIL_ESS_QCZ.sql: 'utf-8' codec can't decode byte 0x9d in position 1116: invalid start byte


-- ============================================================================
-- Deploy: P_SEND_MAIL.sql
-- Category: procedures
-- ============================================================================

PROMPT Deploying P_SEND_MAIL.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Procedure P_SEND_MAIL
--------------------------------------------------------
set define off;

  CREATE OR REPLACE EDITIONABLE PROCEDURE "DB_DSA"."P_SEND_MAIL" (p_to          IN VARCHAR2
                                          ,p_from        IN VARCHAR2
                                          ,p_subject     IN VARCHAR2
                                          ,p_text_msg    IN VARCHAR2 DEFAULT NULL
                                          ,p_attach_name IN VARCHAR2 DEFAULT NULL
                                          ,p_attach_mime IN VARCHAR2 DEFAULT NULL
                                          ,p_attach_clob IN CLOB DEFAULT NULL
                                          ,p_smtp_host   IN VARCHAR2 DEFAULT 'smtp-pa.jtfg.com'
                                          ,p_smtp_port   IN NUMBER DEFAULT 25)
AS
  l_mail_conn   UTL_SMTP.connection;
  l_boundary    VARCHAR2(50) := '----=*#JT1175Bankprg#*=';
  l_step        PLS_INTEGER  := 12000; -- make sure you set a multiple of 3 not higher than 24573
BEGIN
  l_mail_conn := UTL_SMTP.open_connection(p_smtp_host, p_smtp_port);
  UTL_SMTP.helo(l_mail_conn, p_smtp_host);
  UTL_SMTP.mail(l_mail_conn, p_from);
  UTL_SMTP.rcpt(l_mail_conn, p_to);

  UTL_SMTP.open_data(l_mail_conn);

  UTL_SMTP.write_data(l_mail_conn, 'Date: ' || TO_CHAR(SYSDATE, 'DD-MON-YYYY HH24:MI:SS') || UTL_TCP.crlf);
  UTL_SMTP.write_data(l_mail_conn, 'To: ' || p_to || UTL_TCP.crlf);
  UTL_SMTP.write_data(l_mail_conn, 'From: ' || p_from || UTL_TCP.crlf);
  UTL_SMTP.write_data(l_mail_conn, 'Subject: ' || p_subject || UTL_TCP.crlf);
  UTL_SMTP.write_data(l_mail_conn, 'Reply-To: ' || p_from || UTL_TCP.crlf);
  UTL_SMTP.write_data(l_mail_conn, 'MIME-Version: 1.0' || UTL_TCP.crlf);
  UTL_SMTP.write_data(l_mail_conn, 'Content-Type: multipart/mixed; boundary="' || l_boundary || '"' || UTL_TCP.crlf || UTL_TCP.crlf);

  IF p_text_msg IS NOT NULL THEN
    UTL_SMTP.write_data(l_mail_conn, '--' || l_boundary || UTL_TCP.crlf);
--    UTL_SMTP.write_data(l_mail_conn, 'Content-Type: text/plain; charset="iso-8859-1"' || UTL_TCP.crlf || UTL_TCP.crlf);
    UTL_SMTP.write_data(l_mail_conn, 'Content-Type: text/plain; charset="UTF-8"' || UTL_TCP.crlf || UTL_TCP.crlf);

    UTL_SMTP.write_data(l_mail_conn, p_text_msg);
    UTL_SMTP.write_data(l_mail_conn, UTL_TCP.crlf || UTL_TCP.crlf);
  END IF;

  IF p_attach_name IS NOT NULL THEN
    UTL_SMTP.write_data(l_mail_conn, '--' || l_boundary || UTL_TCP.crlf);
    UTL_SMTP.write_data(l_mail_conn, 'Content-Type: ' || p_attach_mime || '; name="' || p_attach_name || '"' || UTL_TCP.crlf);
    UTL_SMTP.write_data(l_mail_conn, 'Content-Disposition: attachment; filename="' || p_attach_name || '"' || UTL_TCP.crlf || UTL_TCP.crlf);

    FOR i IN 0 .. TRUNC((DBMS_LOB.getlength(p_attach_clob) - 1 )/l_step) LOOP
      UTL_SMTP.write_data(l_mail_conn, DBMS_LOB.substr(p_attach_clob, l_step, i * l_step + 1));
    END LOOP;

    UTL_SMTP.write_data(l_mail_conn, UTL_TCP.crlf || UTL_TCP.crlf);
  END IF;

  UTL_SMTP.write_data(l_mail_conn, '--' || l_boundary || '--' || UTL_TCP.crlf);
  UTL_SMTP.close_data(l_mail_conn);

  UTL_SMTP.quit(l_mail_conn);

  EXCEPTION
  WHEN UTL_SMTP.transient_error OR UTL_SMTP.permanent_error THEN
    BEGIN
      UTL_SMTP.quit(l_mail_conn);
    EXCEPTION
    WHEN UTL_SMTP.transient_error OR UTL_SMTP.permanent_error THEN
      NULL; -- When the SMTP server is down or unavailable, we don't
      -- have a connection to the server. The quit call will
      -- raise an exception that we can ignore.
    END;

END
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: P_SEND_MAIL.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: P_SEND_MAIL.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: P_SEND_MAIL.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: P_SEP_PROBLEM_BILANCE.sql
-- Category: procedures
-- ============================================================================

PROMPT Deploying P_SEP_PROBLEM_BILANCE.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Procedure P_SEP_PROBLEM_BILANCE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE EDITIONABLE PROCEDURE "DB_DSA"."P_SEP_PROBLEM_BILANCE" (p_id_databaze	NUMBER
                                                  ,p_sep_id       NUMBER
                                                  ,p_id_doklad    NUMBER
                                                  ) AS
    PRAGMA autonomous_transaction;

/* =======================================

Autor:      Miroslav Smrecek
Pouziti:    sep problem v dokladoch 
Funkce:     ked funkcia db_jt.f_getUnifAIXDoid nenajde sep_id v rel_protistrana ulozi sa zobrazi cez view v CP
Vytvoreno:  12.03.2021

===========================================*/    

BEGIN

    INSERT INTO Kp_S_Sep_Problem_Bilance (id
                                         ,id_databaze
                                         ,sep_id
                                         ,id_doklad
                                         ,valid
                                         ,datum
                                         )   
    VALUES (SQ_sep_problem_bilance.NEXTVAL
           ,p_id_databaze
           ,p_sep_id
           ,p_id_doklad
           ,'1'
           ,sysdate 
           );

    COMMIT;
END;

--------------------------------------------------------------------------------

--GRANT EXECUTE ON p_sep_problem_bilance TO DB_JT
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: P_SEP_PROBLEM_BILANCE.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: P_SEP_PROBLEM_BILANCE.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: P_SEP_PROBLEM_BILANCE.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/

-- ERROR: Cannot read /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/PROCEDURES/P_UA_RKC_ATRIBUTY.sql: 'utf-8' codec can't decode byte 0xe8 in position 1161: invalid continuation byte


PROMPT Completed procedures deployment
PROMPT


-- ============================================================================
-- PHASE: Deploy PACKAGE SPECIFICATIONS (14 files)
-- ============================================================================

PROMPT
PROMPT ============================================================================
PROMPT Deploying PACKAGE SPECIFICATIONS (14 files)
PROMPT ============================================================================
PROMPT


-- ============================================================================
-- Deploy: BAK_LOADPROTISTRANY.sql
-- Category: package specifications
-- ============================================================================

PROMPT Deploying BAK_LOADPROTISTRANY.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Package BAK_LOADPROTISTRANY
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE PACKAGE "DB_DSA"."BAK_LOADPROTISTRANY" as
	procedure p_updateProtistrany ( aUzivatel varchar2 );
end
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: BAK_LOADPROTISTRANY.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: BAK_LOADPROTISTRANY.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: BAK_LOADPROTISTRANY.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KAP_GUI.sql
-- Category: package specifications
-- ============================================================================

PROMPT Deploying KAP_GUI.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Package KAP_GUI
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE PACKAGE "DB_DSA"."KAP_GUI" as

	procedure p_refreshDBLink;

end
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KAP_GUI.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KAP_GUI.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KAP_GUI.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KAP_PRIVATEBANKING.sql
-- Category: package specifications
-- ============================================================================

PROMPT Deploying KAP_PRIVATEBANKING.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Package KAP_PRIVATEBANKING
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE PACKAGE "DB_DSA"."KAP_PRIVATEBANKING" as
	procedure p_loadKlienti( aUzivatel varchar2 );
end
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KAP_PRIVATEBANKING.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KAP_PRIVATEBANKING.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KAP_PRIVATEBANKING.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: LOADAIX.sql
-- Category: package specifications
-- ============================================================================

PROMPT Deploying LOADAIX.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Package LOADAIX
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE PACKAGE "DB_DSA"."LOADAIX" as
    procedure p_loadUcetniPohyby ( aUzivatel varchar2, aSpolecnost varchar2, aLink varchar2, aDatumOd date, aDatumDo date, aDatumStartUctovani date, aJenomPS int, aIdFronta int, aTyp char );
end
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: LOADAIX.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: LOADAIX.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: LOADAIX.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: LOADCRMESS.sql
-- Category: package specifications
-- ============================================================================

PROMPT Deploying LOADCRMESS.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Package LOADCRMESS
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE PACKAGE "DB_DSA"."LOADCRMESS" as
	procedure p_updateCRMESS ( aUzivatel varchar2 );
end
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: LOADCRMESS.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: LOADCRMESS.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: LOADCRMESS.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: LOADMANUAL.sql
-- Category: package specifications
-- ============================================================================

PROMPT Deploying LOADMANUAL.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Package LOADMANUAL
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE PACKAGE "DB_DSA"."LOADMANUAL" as

  -- import protistran u externe uctovanych spolecnosti

  -- %param aTyp M - menova,  U - uverova pozice
  procedure p_startImport ( aIdKtgUcetniSpolecnost int, aDatum date, aTyp char );

  -- Zkontroluje(pripadne vlozi) zda existuje jiz zaznam pro dvojici aDBLink, aExtId
   -- pokud ano tak vrati id protistrany z KIS. Pokud neexistuje tak automaticky tuto novou protistranu v KIS zalozi.
   -- %param aUzivatel kdo akci provadi 
  procedure p_testInsertUverova ( 
    aUzivatel varchar2, aExtId varchar2, aNazev varchar2, 
    aUcet varchar2, aText varchar2, aCNBCastka number, aCastka number, aCastkaLocal number, aMena varchar2
    , aSkupina varchar2
    , aTypObezretnost varchar2, aKrajina varchar2, aEsa varchar2, aNace varchar2, aLei varchar2
    , aShadowBank varchar2, aTypeExposure varchar2, aCollRegCode varchar2 
    , aDefaultFlag char, vUnif varchar2, vImpDataId number );  

-- esc 22.09 2009
  procedure p_testInsertUverovaR ( 
    aUzivatel varchar2, aExtId varchar2, aNazev varchar2, 
    aUcet varchar2, aText varchar2, aCNBCastka number, aCastka number, aCastkaLocal number, aMena varchar2 
    , aRizikovost number , aSkupina varchar2, aBanka  varchar2, aCistaAngazovanost number
    , aTypObezretnost varchar2, aKrajina varchar2, aEsa varchar2, aNace varchar2, aLei varchar2
    , aShadowBank varchar2, aTypeExposure varchar2, aCollRegCode varchar2
    , aDefaultFlag char, vUnif varchar2, vImpDataId number );  
    
  procedure p_testInsertMenova ( 
    aUzivatel varchar2,  
    aUcet varchar2, aText varchar2, aCNBCastka number, aCastka number, aCastkaLocal number, aMena varchar2 );  


  -- provede preparovani z umele zalozene protistrany na 'korektne' zadanou protistranu.
   -- %param aIdKtgUcetniSpolecnost ucetni spolecnost jejiz protistrana se paruje ( napr. IBI bank, Treti Rim )
   -- %param aExtId unikatni identifikace protistrany v ucetnim systemu spolecnosti aIdKtgUcetniSpolecnost
   -- %param aIdKtgSpolecnost spolecnost na kterou se bude ta protistrana parovat
  procedure p_parovani ( 
    aUzivatel varchar2, aIdKtgUcetniSpolecnost int, aExtId varchar2, aIdKtgSpolecnost int );  

  

end
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: LOADMANUAL.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: LOADMANUAL.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: LOADMANUAL.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: LOADPROTISTRANY.sql
-- Category: package specifications
-- ============================================================================

PROMPT Deploying LOADPROTISTRANY.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Package LOADPROTISTRANY
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE PACKAGE "DB_DSA"."LOADPROTISTRANY" as
	procedure p_updateProtistrany ( aUzivatel varchar2 );
end
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: LOADPROTISTRANY.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: LOADPROTISTRANY.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: LOADPROTISTRANY.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: LOADPROTISTRANY2.sql
-- Category: package specifications
-- ============================================================================

PROMPT Deploying LOADPROTISTRANY2.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Package LOADPROTISTRANY2
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE PACKAGE "DB_DSA"."LOADPROTISTRANY2" as
	procedure p_updateProtistrany ( aUzivatel varchar2 );
    procedure db_jt_app_p_log_autonomous ( aText varchar2 );
end
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: LOADPROTISTRANY2.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: LOADPROTISTRANY2.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: LOADPROTISTRANY2.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: LOADPROTISTRANY_NEW.sql
-- Category: package specifications
-- ============================================================================

PROMPT Deploying LOADPROTISTRANY_NEW.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Package LOADPROTISTRANY_NEW
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE PACKAGE "DB_DSA"."LOADPROTISTRANY_NEW" as
	procedure p_updateProtistrany ( aUzivatel varchar2 );
end
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: LOADPROTISTRANY_NEW.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: LOADPROTISTRANY_NEW.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: LOADPROTISTRANY_NEW.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: LOADQUAESTOR.sql
-- Category: package specifications
-- ============================================================================

PROMPT Deploying LOADQUAESTOR.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Package LOADQUAESTOR
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE PACKAGE "DB_DSA"."LOADQUAESTOR" as
	procedure p_updateProtistrany ( aUzivatel varchar2 );
	procedure p_updateProtistranyDoplnek  ( aUzivatel varchar2 );
	procedure p_afterLoad ( aUzivatel varchar2, aTypDatum char, aLink varchar2, aDatumDo date, aIdDoklad int );	
	procedure p_loadRepBooking ( aUzivatel varchar2, aTypDatum char, aLink varchar2,
		aDatumOd date, aDatumDo date, aJenomPS int, aIdFronta int, aIdDoklad int );
--	procedure p_searchTopasFakturaProti ( aUzivatel varchar2 );
end
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: LOADQUAESTOR.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: LOADQUAESTOR.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: LOADQUAESTOR.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: LOADSEP.sql
-- Category: package specifications
-- ============================================================================

PROMPT Deploying LOADSEP.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Package LOADSEP
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE PACKAGE "DB_DSA"."LOADSEP" as
	procedure p_updateProtistrany ( aUzivatel varchar2 );
end
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: LOADSEP.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: LOADSEP.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: LOADSEP.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: LOADSEP_NEW.sql
-- Category: package specifications
-- ============================================================================

PROMPT Deploying LOADSEP_NEW.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Package LOADSEP_NEW
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE PACKAGE "DB_DSA"."LOADSEP_NEW" as
	procedure p_updateProtistrany ( aUzivatel varchar2 );
end
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: LOADSEP_NEW.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: LOADSEP_NEW.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: LOADSEP_NEW.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: LOADSPIN.sql
-- Category: package specifications
-- ============================================================================

PROMPT Deploying LOADSPIN.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Package LOADSPIN
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE PACKAGE "DB_DSA"."LOADSPIN" as
	procedure p_updateProtistrany ( aUzivatel varchar2 );
	procedure p_loadSpinUcetniPohyby ( aUzivatel varchar2, aMandant int, aDatumOd date, aDatumDo date, aJenomPS int, aIdFronta int );
end
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: LOADSPIN.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: LOADSPIN.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: LOADSPIN.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: LOADTOPAS.sql
-- Category: package specifications
-- ============================================================================

PROMPT Deploying LOADTOPAS.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Package LOADTOPAS
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE PACKAGE "DB_DSA"."LOADTOPAS" as
	procedure p_updateProtistrany ( aUzivatel varchar2 );
	procedure p_loadTopasUcetniPohyby ( aUzivatel varchar2, aSpolecnost varchar2, aLink varchar2, aDatumOd date, aDatumDo date, aDatumStartUctovani date, aJenomPS int, aIdFronta int, aTyp char );
end
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: LOADTOPAS.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: LOADTOPAS.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: LOADTOPAS.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


PROMPT Completed package specifications deployment
PROMPT


-- ============================================================================
-- PHASE: Deploy PACKAGE BODIES (14 files)
-- ============================================================================

PROMPT
PROMPT ============================================================================
PROMPT Deploying PACKAGE BODIES (14 files)
PROMPT ============================================================================
PROMPT

-- ERROR: Cannot read /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/PACKAGE_BODIES/BAK_LOADPROTISTRANY.sql: 'utf-8' codec can't decode byte 0xe9 in position 39370: invalid continuation byte


-- ============================================================================
-- Deploy: KAP_GUI.sql
-- Category: package bodies
-- ============================================================================

PROMPT Deploying KAP_GUI.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Package Body KAP_GUI
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE PACKAGE BODY "DB_DSA"."KAP_GUI" as

procedure p_refreshDBLink as
   s_cod int;
begin
	--esc 12/20111 - novy TopSecur , s ATLantikom
  begin
		dbms_session.close_database_link('topa11oo');
		exception when others then
			s_cod := SQLCode;
			if ( s_cod != -2081 ) then
				raise;
			end if;
	end;
  
	begin
		dbms_session.close_database_link('quaestor');
		exception when others then
			s_cod := SQLCode;
			if ( s_cod != -2081 ) then
				raise;
			end if;
	end;
	begin
		dbms_session.close_database_link('quaestor_sk');
		exception when others then
			s_cod := SQLCode;
			if ( s_cod != -2081 ) then
				raise;
			end if;
	end;	
	begin
		dbms_session.close_database_link('spin');
		exception when others then
			s_cod := SQLCode;
			if ( s_cod != -2081 ) then
				raise;
			end if;
	end;
	begin
		dbms_session.close_database_link('topasset');
		exception when others then
			s_cod := SQLCode;
			if ( s_cod != -2081 ) then
				raise;
			end if;
	end;
	begin
		dbms_session.close_database_link('topczk');
		exception when others then
			s_cod := SQLCode;
			if ( s_cod != -2081 ) then
				raise;
			end if;
	end;
	begin
		dbms_session.close_database_link('topeur');
		exception when others then
			s_cod := SQLCode;
			if ( s_cod != -2081 ) then
				raise;
			end if;
	end;
	begin
		dbms_session.close_database_link('topgbp');
		exception when others then
			if ( s_cod != -2081 ) then
				s_cod := SQLCode;
				raise;
			end if;
	end;
	begin
		dbms_session.close_database_link('topsecur');
		exception when others then
			s_cod := SQLCode;
			if ( s_cod != -2081 ) then
				raise;
			end if;
	end;
	begin
		dbms_session.close_database_link('topskk');
		exception when others then
			s_cod := SQLCode;
			if ( s_cod != -2081 ) then
				raise;
			end if;
	end;
	begin
		dbms_session.close_database_link('topusd');
		exception when others then
			s_cod := SQLCode;
			if ( s_cod != -2081 ) then
				raise;
			end if;
	end;

end;


end
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KAP_GUI.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KAP_GUI.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KAP_GUI.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: KAP_PRIVATEBANKING.sql
-- Category: package bodies
-- ============================================================================

PROMPT Deploying KAP_PRIVATEBANKING.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Package Body KAP_PRIVATEBANKING
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE PACKAGE BODY "DB_DSA"."KAP_PRIVATEBANKING" as
	currentUser varchar2(30);
	
	function stdIco(ico varchar2) return varchar2 as
		noveIco varchar2(20) := ico;
	begin
		while(length(noveIco)<10) loop
			noveIco := '0'||noveIco;
		end loop;
		
		return noveIco;
	end;
	
	procedure loadQuaestor as
		mySql varchar2(1000);
		cnt int;
	begin
		for r in (select id, s_dbLink
			  	  from db_jt.kp_cis_databaze
				  where c_pbTyp = 'Q'
          and C_AKTIVNI = 1 --08.11.2012          
				  order by id) loop
				  
			delete kp_tmp_pbKlient where id_databaze = r.id;
				  
			mySql := 'insert into kp_tmp_pbKlient '||
				  	 '(id_databaze, s_ico, s_nazev, s_ulice, s_mesto, s_psc, s_stat, s_privatniBanker, c_personCorp, number0) '||
					 'select '||r.id||', trim(rel.search_identificat), trim(rel.search_name), trim(rel.street)||'' ''||trim(rel.house_number_compl), trim(rel.city), trim(rel.postcode), trim(rel.country_iso_code), trim(st.name), trim(rel.indicator_pc), trim(rel.number0) '||
					 'from relation@'||r.s_dbLink||' rel, '||
					 	  'rel_status_member@'||r.s_dbLink||' rm, '||
						  'relation_status@'||r.s_dbLink||' st '||
					 'where rel.NUMBER0 = rm.FK_RELATION (+) '|| 
					   'and st.CODE = rm.FK_RELATION_STATUS '||
					   'and (st.code like ''P%'' or st.code like ''Admin%'')'||
					   'and st.code not like ''PF%''';
					   
			execute immediate mySql;	
		
			commit;
			
			for rKlient in (select number0 
			                from kp_tmp_pbKlient
							where id_databaze = r.id) loop 

				--dotahneme jeste skupinu z relation_alias
				mySql := 'select count(*) from relation_alias@'||r.s_dbLink||' al where '||rKlient.number0||' = trim(al.fk_relation)';
				
				execute immediate mySql into cnt;
				
				if(cnt = 1) then
					mySql := 'update kp_tmp_pbKlient set short_name = (select short_name from relation_alias@'||r.s_dbLink||' al where '||rKlient.number0||' = trim(al.fk_relation)) where number0 = '||rKlient.number0;
					
					execute immediate mySql;
				elsif(cnt > 1) then
				    mySql := 'select count(*) from relation_alias@'||r.s_dbLink||' al where '||rKlient.number0||' = trim(al.fk_relation) and al.short_name like ''%\_PB\_%'' escape ''\''';
					
					execute immediate mySql into cnt;
					
					if(cnt = 1) then
						mySql := 'update kp_tmp_pbKlient set short_name = (select short_name from relation_alias@'||r.s_dbLink||' al where '||rKlient.number0||' = trim(al.fk_relation) and al.short_name like ''%\_PB\_%'' escape ''\'') where number0 = '||rKlient.number0;
						
						execute immediate mySql;
					elsif(cnt>0) then
						mySql := 'update kp_tmp_pbKlient set short_name = (select short_name from relation_alias@'||r.s_dbLink||' al where '||rKlient.number0||' = trim(al.fk_relation) and al.short_name like ''%\_PB\_%'' escape ''\'' and rownum = 1) where number0 = '||rKlient.number0;
						
						execute immediate mySql;
					else
						mySql := 'update kp_tmp_pbKlient set short_name = (select short_name from relation_alias@'||r.s_dbLink||' al where '||rKlient.number0||' = trim(al.fk_relation) and rownum = 1) where number0 = '||rKlient.number0;
						
						execute immediate mySql;
					end if;
				end if;   
				
			end loop;
			
			commit;
				  
		end loop;
	end;

	procedure loadTopas as
		mySql varchar2(1000);
	begin
		for r in (select id, s_dbLink
			  	  from db_jt.kp_cis_databaze
				  where c_pbTyp = 'T'
          and C_AKTIVNI = 1 --08.11.2012
				  order by id) loop
				  
			delete kp_tmp_pbKlient where id_databaze = r.id;
				  
			mySql := 'insert into kp_tmp_pbKlient '||
				  	 '(id_databaze, s_ico, s_nazev, s_ulice, s_mesto, s_psc, s_stat, s_privatniBanker, c_personCorp, kid) '||
					 'select '||r.id||', trim(ico), trim(trim(titul)||'' ''||trim(jmeno)||'' ''||trim(nazev)), trim(ulice), trim(obec), trim(psc), null, trim(maskarady), null, kid '||
					 'from klienti@'||r.s_dbLink||' k '||
					 'where k.MASKARADY LIKE ''PB%'''||
					   'and k.MASKARADY NOT LIKE ''PB\_S\_%'' escape ''\''';
					   
			execute immediate mySql;			
				  
			commit;
		
		end loop;
	end;
	
	procedure repairIco as
		noveIco varchar2(20);
		idSpol int;
		renoveIco varchar2(20);
		cnt int;
	begin
		for r in ( select pb.*
			  	   from kp_tmp_pbKlient pb
				   where pb.s_ico like '900%') loop
				   
			noveIco := f_formatIco(r.s_ico);
			
			update kp_tmp_pbKlient
			set s_ico = nvl((select s_ico from db_jt.kp_ktg_spolecnost where s_pom_ico = noveIco),noveIco)
			where s_ico = r.s_ico;
			
		end loop;

		for r in ( select pb.*
			  	   from kp_tmp_ePerspektivaOsobyPB pb
				   where pb.ico like '900%') loop
				   
			noveIco := f_formatIco(r.ico);
			
			update kp_tmp_ePerspektivaOsobyPB
			set ico = nvl((select s_ico from db_jt.kp_ktg_spolecnost where s_pom_ico = noveIco),noveIco)
			where ico = r.ico;
				
		end loop;
		
		commit;

	--esc 10/2010
  EXCEPTION when OTHERS 
      then
      --dbms_output.put_line(SQLCODE||' -ERROR- '||SQLERRM );
        db_jt.app.p_log_autonomous('::repairIco:: noveIco:'||noveIco);
        db_jt.app.p_log_autonomous(SQLCODE||' -ERROR- '||SQLERRM);
        raise;      

	end;
	
	procedure processData as
		noveIco varchar2(20);
		cnt int;
	begin
		for r in (select *
			  	  from kp_tmp_pbKlient
				  order by id_databaze) loop
				  
			noveIco := stdIco(r.s_ico);
				  
			select count(*)
			into cnt
			from db_jt.kp_ktg_pbKlient k
			where k.s_ico = noveIco;
			
			if(cnt>0) then
				if(r.number0 is not null) then
					if(r.id_databaze = 100) then
						update db_jt.kp_ktg_pbKlient k
						set k.number0 = r.number0
						where k.number0 is null
						  and k.s_ico = noveIco;
						  
						update db_jt.kp_ktg_pbKlient k
						set c_aktivni = '1',
							s_nazev = r.s_nazev,
							s_ulice = r.s_ulice,
							s_mesto = r.s_mesto,
							s_psc = r.s_psc,
							s_stat = r.s_stat,
							s_privatniBanker = r.s_privatnibanker,
							c_personCorp = r.c_personCorp,						
							k.s_uzivatel = currentUser,
						    k.dt_datumZmeny = sysdate,
						    k.s_icoBezNul = r.s_ico
						where k.s_ico = noveIco;
					elsif(r.id_databaze = 105) then
						update db_jt.kp_ktg_pbKlient k
						set k.number0_sk = r.number0
						where k.number0_sk is null
						  and k.s_ico = noveIco;
						  
						update db_jt.kp_ktg_pbKlient k
						set c_aktivni = '1',
							s_nazev = r.s_nazev,
							s_ulice = r.s_ulice,
							s_mesto = r.s_mesto,
							s_psc = r.s_psc,
							s_stat = r.s_stat,
							s_privatniBanker = r.s_privatnibanker,
							c_personCorp = r.c_personCorp,						
							k.s_uzivatel = currentUser,
						    k.dt_datumZmeny = sysdate,
						    k.s_icoBezNul = r.s_ico
						where k.number0 is null
						  and k.s_ico = noveIco;
					end if;
				end if;
				if(r.short_name is not null) then
					update db_jt.kp_ktg_pbKlient k
					set k.short_name = r.short_name
					where k.short_name is null
					  and k.s_ico = noveIco;
				end if;
				if(r.kid is not null) then
					update db_jt.kp_ktg_pbKlient k
					set k.kid = r.kid,
						k.s_uzivatel = currentUser,
					    k.dt_datumZmeny = sysdate
					where k.kid is null
					  and k.s_ico = noveIco;

					update db_jt.kp_ktg_pbKlient k
					set c_aktivni = '1',
						s_nazev = r.s_nazev,
						s_ulice = r.s_ulice,
						s_mesto = r.s_mesto,
						s_psc = r.s_psc,
						s_stat = r.s_stat,
						s_privatniBanker = r.s_privatnibanker,
						c_personCorp = r.c_personCorp,
						k.s_uzivatel = currentUser,
					    k.dt_datumZmeny = sysdate,
						k.s_icoBezNul = r.s_ico
					where k.number0 is null
					  and k.number0_sk is null
					  and k.s_ico = noveIco;
				end if;
			else
				insert into db_jt.kp_ktg_pbKlient (
					c_aktivni,
					s_ico,
					s_nazev,
					s_ulice,
					s_mesto,
					s_psc,
					s_stat,
					s_privatniBanker,
					c_personCorp,
					kid,
					stredisko,
					short_name,
					s_uzivatel,
					s_icoBezNul)
				values (
					'1',
					noveIco,
					r.s_nazev,
					r.s_ulice,
					r.s_mesto,
					r.s_psc,
					r.s_stat,
					r.s_privatniBanker,
					r.c_personCorp,
					r.kid,
					r.stredisko,
					r.short_name,
					currentUser,
					r.s_ico);
				
				if(r.id_databaze = 100) then
					update db_jt.kp_ktg_pbKlient k
					set k.number0 = r.number0
					where k.s_ico = noveIco;
				elsif(r.id_databaze = 105) then
					update db_jt.kp_ktg_pbKlient k
					set k.number0_sk = r.number0
					where k.s_ico = noveIco;
				end if;
			end if;
				  
		end loop;
		
		commit;
		
		for r in (select *
			  	  from kp_tmp_eperspektivaOsobyPb) loop
		
			if(r.ico_cizinec is null) then
				noveIco := stdIco(r.ico);
			else
				noveIco := stdIco(r.ico_cizinec);
			end if;

			select count(*)
			into cnt
			from db_jt.kp_ktg_pbKlient k
			where k.s_ico = noveIco;

			if(cnt>0) then
				update db_jt.kp_ktg_pbKlient k
				set k.id_eperspektiva = r.osoba_id,
					k.s_uzivatel = currentUser,
				    k.dt_datumZmeny = sysdate
				where k.id_eperspektiva is null
				  and k.s_ico = noveIco;

				update db_jt.kp_ktg_pbKlient k
				set k.s_privatniBanker = r.privatni_banker,
					k.s_uzivatel = currentUser,
				    k.dt_datumZmeny = sysdate
				where k.number0 is null
				  and k.kid is not null
				  and k.s_ico = noveIco;

				update db_jt.kp_ktg_pbKlient k
				set c_aktivni = '1',
					--s_nazev = r.nazev,
					--s_ulice = r.ulice,
					--s_mesto = r.mesto,
					--s_psc = r.psc,
					s_privatniBanker = r.privatni_banker,
					k.s_uzivatel = currentUser,
				    k.dt_datumZmeny = sysdate,
					k.s_icoBezNul = nvl(r.ico_cizinec,r.ico)
				where k.number0 is null
				  and k.kid is null
				  and k.s_ico = noveIco;
			else
				insert into db_jt.kp_ktg_pbKlient (
					c_aktivni,
					s_ico,
					s_nazev,
					s_ulice,
					s_mesto,
					s_psc,
					s_privatniBanker,
					id_eperspektiva,
					s_uzivatel,
					s_icoBezNul)
				values (
					'1',
					noveIco,
					r.nazev,
					r.ulice,
					r.mesto,
					r.psc,
					r.privatni_banker,
					r.osoba_id,
					currentUser,
					nvl(r.ico_cizinec,r.ico));
			end if;
		
		end loop;
		
		commit;
	end;
	
	procedure p_loadKlienti( aUzivatel varchar2 ) as
	begin
		currentUser := aUzivatel; 
	
		loadQuaestor;
		loadTopas;
		--loadFeis;
		
		repairIco;
		
		processData;
	end; 
end
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: KAP_PRIVATEBANKING.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: KAP_PRIVATEBANKING.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: KAP_PRIVATEBANKING.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: LOADAIX.sql
-- Category: package bodies
-- ============================================================================

PROMPT Deploying LOADAIX.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Package Body LOADAIX
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE PACKAGE BODY "DB_DSA"."LOADAIX" as

procedure loadAIXUcetniPohyby ( aUzivatel varchar2, aSpolecnost varchar2, bLink varchar2, aDatumOd date, aDatumDo date, aDatumStart date, aJenomPS int, aIdFronta int, aDatumOdPS date ) as
    sqlStmt varchar2(5000);
begin

    db_jt.app.p_log_autonomous ( 'DSA : loadAIXUcetniPohyby START' );

    db_jt.app.p_log_autonomous ( 'DSA : loadAIXUcetniPohyby aUzivatel:'||aUzivatel||' aSpolecnost:'||aSpolecnost||' bLink:'||bLink||' aDatumOd:'||aDatumOd||' aDatumDo:'||aDatumDo||' aDatumStart:'||aDatumStart||' aJenomPS:'||aJenomPS||' aIdFronta:'||aIdFronta||' aDatumOdPS:'||aDatumOdPS );

    P_ExecuteSQL ( 'truncate table T_AIX_pohyby' );

--loadprotistrany2.db_jt_app_p_log_autonomous ( 'DSA : loadAIXUcetniPohyby 1' );
    if ( aJenomPS in ( 0, 2 ) ) then
        -- 0- normalni data + PS
        -- 1- jenom PS  / bez normalnich dat
        -- 2- normalni data ( bez PS ) 
        sqlStmt := 
          'insert into T_AIX_pohyby  ( ' ||
              'udid, upid, m1d0, datum, ucet, stredisko, protiucet, protistredisko, ' ||
              'text, castka, mena, castkameny, kurz, jednotka, doid, parznak, vypznak, datumsplat, ' ||
              'zakazka, cinnost, datumdph, relacedph, sazbadph, exportlock, opid, datop, ' ||
              'topzkratka, manazer_id, projekt_id, xidsl, id_fronta, pocatecniStav) ' ||
          'select ' ||
              'udid, upid, m1d0, datum, ucet, stredisko, protiucet, protistredisko, ' ||
--              'text, castka, mena, castkameny, kurz, jednotka, doid, parznak, vypznak, datumsplat, ' ||
--              'zakazka, cinnost, datumdph, relacedph, sazbadph, exportlock, opid, datop, ' ||
              'text, castka, mena, castkameny, kurz, jednotka, doid, parznak, vypznak, NULL, ' ||
              'zakazka, cinnost, NULL, relacedph, sazbadph, exportlock, opid, NULL, ' ||

              'topzkratka, manazer_id, projekt_id, xidsl, ' || aIdFronta || ', ''0'' ' ||
           'from ' ||
               'kis.UCETPOHYBY@' || bLink || ' ' ||
           'where ' ||
              'stredisko = ''' || aSpolecnost || '''  and  ' ||
--              'datum between ''' || aDatumOd || ''' and ''' || aDatumDo || ''' ';
              'datum between ''' || aDatumOdPS || ''' and ''' || aDatumDo || ''' ';

        db_jt.app.p_log_autonomous ( 'DSA : loadAIXUcetniPohyby - sqlStmt:' || sqlStmt );
        execute immediate sqlStmt;

        db_jt.app.p_log_autonomous ( 'DSA : loadAIXUcetniPohyby - after insert transakce' );

    end if;

    -- pokud poslu do aJenomPS = 2 tak nechci pocatecni stavy
    if ( aJenomPS != 2 ) then
        -- pocatecni stavy
        -- 0- normalni data + PS
        -- 1- jenom PS  / bez normalnich dat
        -- 2- normalni data ( bez PS ) 
        sqlStmt :=
          'insert into T_AIX_pohyby  ( ' ||
              'udid, upid, m1d0, datum, ucet, stredisko, protiucet, protistredisko, ' ||
              'text, castka, mena, castkameny, kurz, jednotka, doid, parznak, vypznak, datumsplat, ' ||
              'zakazka, cinnost, exportlock, ' ||
              'topzkratka, manazer_id, projekt_id, xidsl, id_fronta, pocatecniStav) ' ||
          'select ' ||
              'psid, 1, m1d0, datum, ucet, stredisko, protiucet, protistredisko, ' ||
--              'text, castka, mena, castkameny, kurz, jednotek, doid, parznak, vypznak, datumsplat, ' ||
              'text, castka, mena, castkameny, kurz, jednotek, doid, parznak, vypznak, NULL, ' ||
              'zakazka, cinnost, exportlock, ' ||
              'topzkratka, manazer_id, projekt_id, xidsl, ' || aIdFronta || ', ''1'' ' ||
           'from ' ||
               'kis.POCSTAVYUCTU@' || bLink || ' ' ||
           'where ' ||
              'stredisko = ''' || aSpolecnost || '''  and  ' ||
--              'datum between ''' || aDatumOd || ''' and ''' || aDatumDo || ''' ';
              'datum between ''' || aDatumOdPS || ''' and ''' || aDatumDo || ''' ';

        db_jt.app.p_log_autonomous ( 'DSA : loadAIXUcetniPohyby Pocatecni STAVY sqlStmt:' || sqlStmt );
        execute immediate sqlStmt;

        db_jt.app.p_log_autonomous ( 'DSA : loadAIXUcetniPohyby Pocatecni STAVY  aJenomPS:'||aJenomPS );

    end if;

    exception
		when others then
			db_jt.app.p_log_autonomous ( 'DSA : ERROR : loadAIXUcetniPohyby Pocatecni STAVY '||bLink|| ' uzivatel: ' || aUzivatel );
			db_jt.app.p_log_autonomous ( 'DSA : ERROR : '|| sqlStmt );

            raise_application_error ( -20001, 'Original chyba=' || SQLCODE || ' Parametr DBLink='||bLink||' => ' ||SQLERRM);

end;


procedure loadAIXUcetniPohybyIFRS ( aUzivatel varchar2, aSpolecnost varchar2, bLink varchar2, aDatumOd date, aDatumDo date, aDatumStart date, aJenomPS int, aIdFronta int, aDatumOdPS date ) as
    sqlStmt varchar2(5000);
begin

    db_jt.app.p_log_autonomous ( 'DSA : loadAIXUcetniPohybyIFRS START' );

    db_jt.app.p_log_autonomous ( 'DSA : loadAIXUcetniPohybyIFRS aUzivatel:'||aUzivatel||' aSpolecnost:'||aSpolecnost||' bLink:'||bLink||' aDatumOd:'||aDatumOd||' aDatumDo:'||aDatumDo||' aDatumStart:'||aDatumStart||' aJenomPS:'||aJenomPS||' aIdFronta:'||aIdFronta||' aDatumOdPS:'||aDatumOdPS );

    P_ExecuteSQL ( 'truncate table T_AIX_pohyby' );

--loadprotistrany2.db_jt_app_p_log_autonomous ( 'DSA : loadAIXUcetniPohyby 1' );
    if ( aJenomPS in ( 0, 2 ) ) then
        -- IFRS data
        -- 0- normalni data + PS
        -- 1- jenom PS  / bez normalnich dat
        -- 2- normalni data ( bez PS ) 
        sqlStmt :=
          'insert into T_AIX_pohyby  ( ' ||
              'udid, upid, m1d0, datum, ucet, stredisko, protiucet, protistredisko, ' ||
              'text, castka, mena, castkameny, kurz, jednotka, doid, parznak, vypznak, datumsplat, ' ||
              'zakazka, cinnost, datumdph, relacedph, sazbadph, exportlock, opid, datop, ' ||
              'topzkratka, manazer_id, projekt_id, xidsl, id_fronta, pocatecnistav ) ' ||
          'select ' ||
              'udid, upid, m1d0, datum, ucet, stredisko, protiucet, protistredisko, ' ||
--              'text, castka, mena, castkameny, kurz, jednotka, doid, parznak, vypznak, datumsplat, ' ||
--              'zakazka, cinnost, datumdph, relacedph, sazbadph, exportlock, opid, datop, ' ||
              'text, castka, mena, castkameny, kurz, jednotka, doid, parznak, vypznak, NULL, ' ||
              'zakazka, cinnost, NULL, relacedph, sazbadph, exportlock, opid, NULL, ' ||
              'topzkratka, manazer_id, projekt_id, xidsl, ' || aIdFronta || ', ''0'' ' ||
           'from ' ||
               'kis.GAAPUCETPOHYBY@' || bLink || ' ' ||
           'where ' ||
              'stredisko = ''' || aSpolecnost || '''  and  ' ||
--              'datum between ''' || aDatumOd || ''' and ''' || aDatumDo || ''' ';
              'datum between ''' || aDatumOdPS || ''' and ''' || aDatumDo || ''' ';

        db_jt.app.p_log_autonomous ( 'DSA : loadAIXUcetniPohybyIFRS - sqlStmt:' || sqlStmt  );
        execute immediate sqlStmt;

        db_jt.app.p_log_autonomous ( 'DSA : loadAIXUcetniPohybyIFRS - after insert' );

    end if;

    -- pokud poslu do aJenomPS = 2 tak nechci pocatecni stavy
    if ( aJenomPS != 2 ) then
        -- IFRS pocatecni stavy
        -- 0- normalni data + PS
        -- 1- jenom PS  / bez normalnich dat
        -- 2- normalni data ( bez PS ) 
        sqlStmt :=
          'insert into T_AIX_pohyby  ( ' ||
              'udid, upid, m1d0, datum, ucet, stredisko, protiucet, protistredisko, ' ||
              'text, castka, mena, castkameny, kurz, jednotka, doid,  vypznak, datumsplat, ' ||
              'zakazka, cinnost, exportlock, ' ||
              'topzkratka, manazer_id, projekt_id, xidsl, id_fronta, pocatecniStav) ' ||
          'select ' ||
              'psid, 1, m1d0, datum, ucet, stredisko, protiucet, protistredisko, ' ||
--              'text, castka, mena, castkameny, kurz, jednotek, doid,  vypznak, datumsplat, ' ||
              'text, castka, mena, castkameny, kurz, jednotek, doid,  vypznak, NULL, ' ||
              'zakazka, cinnost, exportlock, ' ||
              'topzkratka, manazer_id, projekt_id, xidsl, ' || aIdFronta || ', ''1'' ' ||
           'from ' ||
               'kis.GAAPPOCSTAVYUCTU@' || bLink || ' ' ||
           'where ' ||
              'stredisko = ''' || aSpolecnost || '''  and  ' ||
--              'datum between ''' || aDatumOd || ''' and ''' || aDatumDo || ''' ';
              'datum between ''' || aDatumOdPS || ''' and ''' || aDatumDo || ''' ';

        db_jt.app.p_log_autonomous ( 'DSA : loadAIXUcetniPohybyIFRS Pocatecni STAVY  sqlStmt:' || sqlStmt );
        execute immediate sqlStmt;

        db_jt.app.p_log_autonomous ( 'DSA : loadAIXUcetniPohybyIFRS Pocatecni STAVY  aJenomPS:'||aJenomPS );

    end if;

    exception
		when others then
			db_jt.app.p_log_autonomous ( 'DSA : ERROR : loadAIXUcetniPohybyIFRS Pocatecni STAVY '||bLink|| ' uzivatel: ' || aUzivatel );
			db_jt.app.p_log_autonomous ( 'DSA : ERROR : '|| sqlStmt );

            raise_application_error ( -20001, 'Original chyba=' || SQLCODE || ' Parametr DBLink='||bLink||' => ' ||SQLERRM);

end;


procedure p_loadUcetniPohyby ( aUzivatel varchar2, aSpolecnost varchar2, aLink varchar2, aDatumOd date, aDatumDo date, aDatumStartUctovani date, aJenomPS int,
                         aIdFronta int, aTyp char ) is
	aStav char;
    bLink VARCHAR2(8 BYTE);
    aDatumOdPS DATE;
begin
    db_jt.app.p_log_autonomous ( '  v db_dsa.loadAIX.p_loadAIXUcetniPohyby START' );

    if ( aTyp not in (  'N', 'I' ) ) then
        raise_application_error ( -20001, 'Spatny parametr ' || aTyp );
    end if;

    begin
-- pro DB link je potreba pouzit s_instance z db_jt.kp_cis_databaze, z kp_ktg_ucetnispolecnost dostaneme aLink = 'AX'
        SELECT s_instance INTO bLink
        FROM db_jt.kp_cis_databaze
        WHERE s_dblink = aLink;
-- pre generovanie vytvaram aDatumOdPS co je rok podla generovaneho dokladu a den/mesiac podla ucetni obdobi start
        aDatumOdPS := to_date ( to_char(aDatumStartUctovani,'dd.mm') || '.'  || EXTRACT(YEAR FROM aDatumOd),'dd.mm.yyyy' );
-- moze nastat pripad ked aDatumOdPS bude vacsie ako datum generovania do, vtedy berieme data od 1.1.
        IF aDatumOdPS >= aDatumDo THEN aDatumOdPS := aDatumOd; END IF;

        p_radicZadostImport ( aIdFronta, 'A', aStav );

        if ( aStav = 'K' ) then
            -- data jsou pripravena
            return;
        end if;

--        P_ExecuteSQL ( 'truncate table t_ucet_pohyby' );
--        delete t_ucet_pohyby;

        if ( aTyp = 'N' ) then
            loadAIXUcetniPohyby ( aUzivatel, aSpolecnost, bLink, aDatumOd, aDatumDo, aDatumStartUctovani, aJenomPS, aIdFronta, aDatumOdPS );
        elsif ( aTyp = 'I' ) then
            loadAIXUcetniPohybyIFRS ( aUzivatel, aSpolecnost, bLink, aDatumOd, aDatumDo, aDatumStartUctovani, aJenomPS, aIdFronta, aDatumOdPS );
        else
            raise_application_error ( -20001, 'Spatny parametr' );
        end if;

        db_jt.app.p_log_autonomous ( 'DSA : LOADAIX.p_loadUcetniPohyby - vyjimky protitstran' );
        for cVyjimka in (
            select v.s_ucetMaska, v.s_protistrana, v.nl_poziceOd
            from KP_ktg_ProtistranaVyjimky v
            where v.id_databaze = 500  )
        loop

            update t_aix_pohyby set
                vyjimkaProtistrana = '3',
                doid = cVyjimka.s_protistrana
            where
                substr ( ucet, cVyjimka.nl_poziceOd ) like cVyjimka.s_ucetMaska;

        end loop;

        db_jt.app.p_log_autonomous ( 'DSA : LOADAIX.p_loadUcetniPohyby - vyjimky protitstran konec' );


        update kp_radicImportu set c_stav = 'K' where id_fronta = aIdFronta;

	exception
		when others then
			rollback;
			delete kp_radicImportu where id_fronta = aIdFronta;
			commit;
			raise;

	end;
end;

end
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: LOADAIX.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: LOADAIX.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: LOADAIX.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/

-- ERROR: Cannot read /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/PACKAGE_BODIES/LOADCRMESS.sql: 'utf-8' codec can't decode byte 0xe1 in position 7412: invalid continuation byte

-- ERROR: Cannot read /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/PACKAGE_BODIES/LOADMANUAL.sql: 'utf-8' codec can't decode byte 0xc8 in position 5593: invalid continuation byte

-- ERROR: Cannot read /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/PACKAGE_BODIES/LOADPROTISTRANY.sql: 'utf-8' codec can't decode byte 0xe9 in position 56708: invalid continuation byte

-- ERROR: Cannot read /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/PACKAGE_BODIES/LOADPROTISTRANY2.sql: 'utf-8' codec can't decode byte 0xe9 in position 39383: invalid continuation byte

-- ERROR: Cannot read /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/PACKAGE_BODIES/LOADPROTISTRANY_NEW.sql: 'utf-8' codec can't decode byte 0xe9 in position 40718: invalid continuation byte

-- ERROR: Cannot read /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/PACKAGE_BODIES/LOADQUAESTOR.sql: 'utf-8' codec can't decode byte 0xc8 in position 4649: invalid continuation byte

-- ERROR: Cannot read /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/PACKAGE_BODIES/LOADSEP.sql: 'utf-8' codec can't decode byte 0xe1 in position 5352: invalid continuation byte

-- ERROR: Cannot read /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/PACKAGE_BODIES/LOADSEP_NEW.sql: 'utf-8' codec can't decode byte 0x9a in position 1595: invalid start byte

-- ERROR: Cannot read /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/PACKAGE_BODIES/LOADSPIN.sql: 'utf-8' codec can't decode byte 0xe1 in position 3542: invalid continuation byte

-- ERROR: Cannot read /Users/radektuma/DEV/KIS/sources/DB/DB_DSA/PACKAGE_BODIES/LOADTOPAS.sql: 'utf-8' codec can't decode byte 0xed in position 4252: invalid continuation byte


PROMPT Completed package bodies deployment
PROMPT


-- ============================================================================
-- PHASE: Deploy TRIGGERS (15 files)
-- ============================================================================

PROMPT
PROMPT ============================================================================
PROMPT Deploying TRIGGERS (15 files)
PROMPT ============================================================================
PROMPT


-- ============================================================================
-- Deploy: TAI_Q_ALL_GENERATION.sql
-- Category: triggers
-- ============================================================================

PROMPT Deploying TAI_Q_ALL_GENERATION.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Trigger TAI_Q_ALL_GENERATION
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "DB_DSA"."TAI_Q_ALL_GENERATION" 
after INSERT on Q_ALL_GENERATION for each row

declare from_db varchar2(3) ;

begin
if ( :new.Q_FROM = 'QCZ' )  then
        execute immediate 'delete from DB_DSA.Q_REP_BOOKING_QCZ' ;
        kp_etl.inserttokis@QUAESTOR(1,'B');  
elsif ( :new.Q_FROM = 'QSK' )  then  
        execute immediate 'delete from  DB_DSA.Q_REP_BOOKING_QSK' ;
        kp_etl.inserttokis@QUAESTOR_SK(5000,'B');  
end if;  
    
EXCEPTION WHEN OTHERS
then
  rollback;
  db_jt.app.p_log_autonomous('DB_DSA.TAI_Q_ALL_GENERATION- citam data z '||:new.Q_FROM||' - ERROR:'||SQLERRM);			  
end;
/
ALTER TRIGGER "DB_DSA"."TAI_Q_ALL_GENERATION" ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: TAI_Q_ALL_GENERATION.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: TAI_Q_ALL_GENERATION.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: TAI_Q_ALL_GENERATION.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: TAI_Q_REP_BOOKING_QCZ.sql
-- Category: triggers
-- ============================================================================

PROMPT Deploying TAI_Q_REP_BOOKING_QCZ.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Trigger TAI_Q_REP_BOOKING_QCZ
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "DB_DSA"."TAI_Q_REP_BOOKING_QCZ" 
after INSERT on Q_REP_BOOKING_QCZ

declare
pragma autonomous_transaction;

BEGIN
DB_JT.app.p_log_autonomous('TAI_Q_REP_BOOKING_QCZ - START BUDGETY : DAT_BUDGETTRANSAKCE__ALL_QCZ ');

begin  ---odbory QCZ  
  DELETE DB_JT.DAT_BUDGETTRANSAKCE__ALL_QCZ WHERE  fk_gen_id  = (SELECT MAX(fk_gen_id) FROM DB_DSA.Q_REP_BOOKING_QCZ WHERE rownum <2 ) ;

  insert into DB_JT.DAT_BUDGETTRANSAKCE__ALL_QCZ   --10357 QCZ , 6224x 01.06 2012, 8977  19.07
  (ID	,S_POPIS	,ND_CASTKAMENA,S_MENA	,ND_CASTKALOCAL	,UCSPOL_MENA	,DATUM	,SPOLECNOST	,S_UCET	,S_UNIFSPOL	,ID_BUDGET	,ID_CISTYPTRANSAKCE	
  ,ID_KTGPROJEKT	,SPOL_DOC	,XIDSL	,BUDGET_MENA,FK_GEN_ID	)
  SELECT 
    z.BOOKING_ID id,
    z.DESCRIPTION s_popis,  --??? nie je to popis transakce ...    
    DECODE ( z.IND_DEBIT_CREDIT, 'D', -1, 1 )*z.AMOUNT nd_castkamena,    
    z.BOOK_CURRENCY S_MENA,    
    DECODE ( z.IND_DEBIT_CREDIT, 'D', -1, 1 )*z.LC_AMOUNT nd_castkalocal,    
    s.s_mena ucspol_mena,    
    z.EFFECTIVE_DATE DATUM, --- ???    
    s.s_nazev spolecnost,    
    z.GL_SYNTETIC s_ucet,    
    ' ' s_unifSpol,
    B.ID id_budget,
    M.ID_CISTYPTRANSAKCE,    
    z.ID_KTGPROJEKT,        
    '0' spol_doc,    
    z.xidsl,
    bp.s_mena budget_mena   ,
    z.FK_GEN_ID
  FROM 
    DB_DSA.Q_REP_BOOKING_QCZ z, 
    DB_JT.KP_ktg_UcetniSpolecnost s,
    DB_JT.KP_DEF_BUDGETMUSTEK M,
    DB_JT.KP_DAT_BUDGET B,
    DB_JT.kp_dat_budgetpolozka bp
  WHERE 
      --z.fk_gen_id  = --'20120801015754195416'--
      --                   (SELECT MAX(fk_gen_id) FROM DB_DSA.q_all_generation WHERE kind = 'B' and  Q_FROM='QCZ' )  AND
      s.id = 1 --BANKA  
      AND (z.pocatecnistav <> '1'  )
      AND z.ID_KTGODBOR     = B.ID_KTGODBOR
      AND s.ID_CISSUBJECT   = M.ID_CISSUBJECT
      AND z.GL_SYNTETIC LIKE M.S_UCET    ||'%'  
      AND z.EFFECTIVE_DATE BETWEEN B.DT_PLATNOSTOD AND B.DT_PLATNOSTDO    
      AND bp.ID_BUDGET     = b.ID
      AND bp.ID_CISTYPTRAN = M.ID_CISTYPTRANSAKCE  
      AND z.EFFECTIVE_DATE BETWEEN M.DT_PLATNOSTOD AND M.DT_PLATNOSTDO    ;

commit;

DB_JT.app.p_log_autonomous('TAI_Q_REP_BOOKING_QCZ - naplnene budgety ODBOROV DAT_BUDGETTRANSAKCE__ALL_QCZ '||SQLERRM);

      
EXCEPTION
WHEN OTHERS THEN
      rollback;
      DB_JT.app.p_log_autonomous('TAI_Q_REP_BOOKING_QCZ - chyba pri plneni ODBORY DAT_BUDGETTRANSAKCE__ALL_QCZ '||SQLERRM);
end;

begin --PROJEKTY

DELETE DB_JT.DAT_BUDGETPROJEKTTRAN_ALL_QCZ WHERE  fk_gen_id  = (SELECT MAX(fk_gen_id) FROM DB_DSA.Q_REP_BOOKING_QCZ WHERE rownum <2 ) ;
  
insert into DB_JT.DAT_BUDGETPROJEKTTRAN_ALL_QCZ  
(ID	,S_POPIS	,ND_CASTKAMENA,S_MENA	,ND_CASTKALOCAL	,UCSPOL_MENA	,DATUM	,SPOLECNOST	
,S_UCET	,S_UNIFSPOL	,ID_BUDGET	,ID_CISTYPTRANSAKCE	,ID_KTGODBOR	,SPOL_DOC	,XIDSL	,BUDGET_MENA , FK_GEN_ID	)
  SELECT distinct
    z.BOOKING_ID id,
    z.DESCRIPTION s_popis,  --??? nie je to popis transakce ...    
    DECODE ( z.IND_DEBIT_CREDIT, 'D', -1, 1 )*z.AMOUNT nd_castkamena,    
    z.BOOK_CURRENCY S_MENA,    
    DECODE ( z.IND_DEBIT_CREDIT, 'D', -1, 1 )*z.LC_AMOUNT nd_castkalocal,    
    s.s_mena ucspol_mena,    
    z.EFFECTIVE_DATE DATUM, --- ???    
    s.s_nazev spolecnost,    
    z.GL_SYNTETIC s_ucet,    
    ' ' s_unifSpol,
    B.ID id_budget,
    M.ID_CISTYPTRANSAKCE,    
    z.ID_KTGODBOR,        
    '0' spol_doc,    
    z.xidsl,
    bp.s_mena budget_mena
    ,    z.FK_GEN_ID
  FROM 
    DB_DSA.Q_REP_BOOKING_QCZ z, 
    DB_JT.KP_ktg_UcetniSpolecnost s,
    DB_JT.KP_DEF_BUDGETMUSTEK M,
    DB_JT.KP_DAT_BUDGET B,
    DB_JT.kp_dat_budgetpolozka bp
  WHERE 
  s.id=1 --BANKA
  AND (z.pocatecnistav <> '1'  )
  AND z.ID_KTGPROJEKT                                                      = B.ID_KTGPROJEKT
  AND s.ID_CISSUBJECT                                                    = M.ID_CISSUBJECT
  AND z.GL_SYNTETIC LIKE M.S_UCET    ||'%'  
  AND z.EFFECTIVE_DATE BETWEEN B.DT_PLATNOSTOD AND B.DT_PLATNOSTDO    
  AND bp.ID_BUDGET     = b.ID
  AND bp.ID_CISTYPTRAN = M.ID_CISTYPTRANSAKCE 
  and z.EFFECTIVE_DATE  BETWEEN m.dt_platnostod and m.dt_platnostdo;

commit;
DB_JT.app.p_log_autonomous('TAI_Q_REP_BOOKING_QCZ - naplnene budgety PROJEKTOV: DAT_BUDGETTRANSAKCE__ALL_QCZ '||SQLERRM);  

EXCEPTION
WHEN OTHERS THEN
      rollback;
      DB_JT.app.p_log_autonomous('TAI_Q_REP_BOOKING_QCZ - chyba pri plneni PROJEKTU DAT_BUDGETTRANSAKCE__ALL_QCZ '||SQLERRM);  
end;
END;
/
ALTER TRIGGER "DB_DSA"."TAI_Q_REP_BOOKING_QCZ" ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: TAI_Q_REP_BOOKING_QCZ.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: TAI_Q_REP_BOOKING_QCZ.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: TAI_Q_REP_BOOKING_QCZ.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: TAI_Q_REP_BOOKING_QSK.sql
-- Category: triggers
-- ============================================================================

PROMPT Deploying TAI_Q_REP_BOOKING_QSK.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Trigger TAI_Q_REP_BOOKING_QSK
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "DB_DSA"."TAI_Q_REP_BOOKING_QSK" 
after INSERT on Q_REP_BOOKING_QSK

declare
pragma autonomous_transaction;

BEGIN
DB_JT.app.p_log_autonomous('TAI_Q_REP_BOOKING_QSK - START BUDGETY: DAT_BUDGETTRANSAKCE__ALL_QSK ');

begin --odbory
    DELETE DB_JT.DAT_BUDGETTRANSAKCE__ALL_QSK WHERE  fk_gen_id  = (SELECT MAX(fk_gen_id) FROM DB_DSA.Q_REP_BOOKING_QSK WHERE rownum <2 ) ;

    INSERT into DB_JT.DAT_BUDGETTRANSAKCE__ALL_QSK   --4400x 23.05.2012, 6552 19.07 2012
      (ID	,S_POPIS	,ND_CASTKAMENA  ,S_MENA	,ND_CASTKALOCAL	,UCSPOL_MENA	,DATUM	,SPOLECNOST	,S_UCET	,S_UNIFSPOL	,ID_BUDGET	,ID_CISTYPTRANSAKCE	
      ,ID_KTGPROJEKT	,SPOL_DOC	,XIDSL	,BUDGET_MENA	, FK_GEN_ID)
    SELECT 
    z.BOOKING_ID id,
    z.DESCRIPTION s_popis,  --??? nie je to popis transakce ...    
    DECODE ( z.IND_DEBIT_CREDIT, 'D', -1, 1 )*z.AMOUNT nd_castkamena,    
    z.BOOK_CURRENCY S_MENA,    
    DECODE ( z.IND_DEBIT_CREDIT, 'D', -1, 1 )*z.LC_AMOUNT nd_castkalocal,    
    s.s_mena ucspol_mena,    
    z.EFFECTIVE_DATE DATUM, --- ???    
    s.s_nazev spolecnost,    
    z.GL_SYNTETIC s_ucet,    
    ' ' s_unifSpol,
    B.ID id_budget,
    M.ID_CISTYPTRANSAKCE,    
    z.ID_KTGPROJEKT,        
    '0' spol_doc,    
    z.xidsl,
    bp.s_mena budget_mena ,
    z.FK_GEN_ID
  FROM 
    DB_DSA.Q_REP_BOOKING_QSK z, 
    DB_JT.KP_ktg_UcetniSpolecnost s,
    DB_JT.KP_DEF_BUDGETMUSTEK M,
    DB_JT.KP_DAT_BUDGET B,
    DB_JT.kp_dat_budgetpolozka bp
    WHERE 
          --z.fk_gen_id  = --'20120801015754195416'--
          --                         (SELECT MAX(fk_gen_id) FROM DB_DSA.q_all_generation WHERE kind = 'B' and  Q_FROM='QSK'  ) AND
        s.id = 5000 --BANKA pobocka
        AND (z.pocatecnistav <> '1'  )
        AND z.ID_KTGODBOR     = B.ID_KTGODBOR
        AND s.ID_CISSUBJECT   = M.ID_CISSUBJECT
        AND z.GL_SYNTETIC LIKE M.S_UCET    ||'%'  
        AND z.EFFECTIVE_DATE BETWEEN B.DT_PLATNOSTOD AND B.DT_PLATNOSTDO  
        AND bp.ID_BUDGET     = b.ID
        AND bp.ID_CISTYPTRAN = M.ID_CISTYPTRANSAKCE 
        AND z.EFFECTIVE_DATE BETWEEN M.DT_PLATNOSTOD AND M.DT_PLATNOSTDO      ;

commit;
      DB_JT.app.p_log_autonomous('TAI_Q_REP_BOOKING_QSK - naplnene budgety ODBOROV: DAT_BUDGETTRANSAKCE__ALL_QSK '||SQLERRM);
EXCEPTION
WHEN OTHERS THEN
      rollback;
      DB_JT.app.p_log_autonomous('TAI_Q_REP_BOOKING_QSK - chyba pri plneni ODBOROV DAT_BUDGETTRANSAKCE__ALL_QSK '||SQLERRM);
end;

begin --projekty

DELETE DB_JT.DAT_BUDGETPROJEKTTRAN_ALL_QSK WHERE  fk_gen_id  = (SELECT MAX(fk_gen_id) FROM DB_DSA.Q_REP_BOOKING_QSK WHERE rownum <2 ) ;

insert into DB_JT.DAT_BUDGETPROJEKTTRAN_ALL_QSK  
(ID	,S_POPIS	,ND_CASTKAMENA,S_MENA	,ND_CASTKALOCAL	,UCSPOL_MENA	,DATUM	,SPOLECNOST	
,S_UCET	,S_UNIFSPOL	,ID_BUDGET	,ID_CISTYPTRANSAKCE	,ID_KTGODBOR	,SPOL_DOC	,XIDSL	,BUDGET_MENA , FK_GEN_ID	)
SELECT distinct --QSK
    z.BOOKING_ID id,
    z.DESCRIPTION s_popis,  --??? nie je to popis transakce ...    
    DECODE ( z.IND_DEBIT_CREDIT, 'D', -1, 1 )*z.AMOUNT nd_castkamena,    
    z.BOOK_CURRENCY S_MENA,    
    DECODE ( z.IND_DEBIT_CREDIT, 'D', -1, 1 )*z.LC_AMOUNT nd_castkalocal,    
    s.s_mena ucspol_mena,    
    z.EFFECTIVE_DATE DATUM, --- ???    
    s.s_nazev spolecnost,    
    z.GL_SYNTETIC s_ucet,    
    ' ' s_unifSpol,
    B.ID id_budget,
    M.ID_CISTYPTRANSAKCE,    
    z.ID_KTGODBOR,        
    '0' spol_doc,    
    z.xidsl,
    bp.s_mena budget_mena
    ,FK_GEN_ID
  FROM 
    DB_DSA.Q_REP_BOOKING_QSK z, --- v.2011
    DB_JT.KP_ktg_UcetniSpolecnost s,
    DB_JT.KP_DEF_BUDGETMUSTEK M,
    DB_JT.KP_DAT_BUDGET B,
    DB_JT.kp_dat_budgetpolozka bp
  WHERE 
  s.id=5000 --BANKA pobocka
  AND (z.pocatecnistav <> '1'  )
  AND z.ID_KTGPROJEKT                                                      = B.ID_KTGPROJEKT  
  AND s.ID_CISSUBJECT                                                    = M.ID_CISSUBJECT
  AND z.GL_SYNTETIC LIKE M.S_UCET    ||'%'  
  AND z.EFFECTIVE_DATE BETWEEN B.DT_PLATNOSTOD AND B.DT_PLATNOSTDO    
  AND bp.ID_BUDGET     = b.ID
  AND bp.ID_CISTYPTRAN = M.ID_CISTYPTRANSAKCE 
  and z.EFFECTIVE_DATE  BETWEEN m.dt_platnostod and m.dt_platnostdo;

commit;
DB_JT.app.p_log_autonomous('TAI_Q_REP_BOOKING_QSK - Naplnene budgety PROJEKTOV : DAT_BUDGETPROJEKTTRAN_ALL_QSK '||SQLERRM);

EXCEPTION
WHEN OTHERS THEN
      rollback;
      DB_JT.app.p_log_autonomous('TAI_Q_REP_BOOKING_QSK - chyba pri plneni PROJEKTOV - DAT_BUDGETPROJEKTTRAN_ALL_QSK '||SQLERRM);
end;

END;
/
ALTER TRIGGER "DB_DSA"."TAI_Q_REP_BOOKING_QSK" ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: TAI_Q_REP_BOOKING_QSK.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: TAI_Q_REP_BOOKING_QSK.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: TAI_Q_REP_BOOKING_QSK.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: TAU_KP_REL_PROTI_ZMENA.sql
-- Category: triggers
-- ============================================================================

PROMPT Deploying TAU_KP_REL_PROTI_ZMENA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Trigger TAU_KP_REL_PROTI_ZMENA
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "DB_DSA"."TAU_KP_REL_PROTI_ZMENA" 
after update of s_ico, s_nazev
on KP_rel_Protistrana
for each row



begin
  if((:old.s_nazev <> :new.s_nazev OR :old.s_ico <> :new.s_ico) and :new.id_spolecnost>0) then
	  insert into KP_LOG_PROTISTRANA (
	    ID,
		ID_DATABAZE,
		ID_SPOLECNOST,
		S_ICO,
		S_NAZEV,
		S_NUMBER0,
		FIRMA_ID,
		EO_ID,
		BANKA_ID,
		DOID,
		KID,
		PLATINST,
		AKCE,
		DT_ZMENA,
		S_POM_ICO,
		EID,
		DATUMZALOZENI,
		ULICE,
		PSC,
		MESTO,
		STAT,
		DIC,
		UCETSCP,
		REGRMS,
		EKONOM_SEKTOR,
		TYPOSOBY,
	    S_AKCE
	  )
	  values (
	    :old.ID,
		:old.ID_DATABAZE,
		:old.ID_SPOLECNOST,
		:old.S_ICO,
		:old.S_NAZEV,
		:old.S_NUMBER0,
		:old.FIRMA_ID,
		:old.EO_ID,
		:old.BANKA_ID,
		:old.DOID,
		:old.KID,
		:old.PLATINST,
		:old.AKCE,
		:old.DT_ZMENA,
		:old.S_POM_ICO,
		:old.EID,
		:old.DATUMZALOZENI,
		:old.ULICE,
		:old.PSC,
		:old.MESTO,
		:old.STAT,
		:old.DIC,
		:old.UCETSCP,
		:old.REGRMS,
		:old.EKONOM_SEKTOR,
		:old.TYPOSOBY,
		'O'
	  );

	  insert into KP_LOG_PROTISTRANA (
	    ID,
		ID_DATABAZE,
		ID_SPOLECNOST,
		S_ICO,
		S_NAZEV,
		S_NUMBER0,
		FIRMA_ID,
		EO_ID,
		BANKA_ID,
		DOID,
		KID,
		PLATINST,
		AKCE,
		DT_ZMENA,
		S_POM_ICO,
		EID,
		DATUMZALOZENI,
		ULICE,
		PSC,
		MESTO,
		STAT,
		DIC,
		UCETSCP,
		REGRMS,
		EKONOM_SEKTOR,
		TYPOSOBY,
	    S_AKCE
	  )
	  values (
	    :new.ID,
		:new.ID_DATABAZE,
		:new.ID_SPOLECNOST,
		:new.S_ICO,
		:new.S_NAZEV,
		:new.S_NUMBER0,
		:new.FIRMA_ID,
		:new.EO_ID,
		:new.BANKA_ID,
		:new.DOID,
		:new.KID,
		:new.PLATINST,
		:new.AKCE,
		:new.DT_ZMENA,
		:new.S_POM_ICO,
		:new.EID,
		:new.DATUMZALOZENI,
		:new.ULICE,
		:new.PSC,
		:new.MESTO,
		:new.STAT,
		:new.DIC,
		:new.UCETSCP,
		:new.REGRMS,
		:new.EKONOM_SEKTOR,
		:new.TYPOSOBY,
		'U'
	  );
  end if;
end;


/
ALTER TRIGGER "DB_DSA"."TAU_KP_REL_PROTI_ZMENA" ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: TAU_KP_REL_PROTI_ZMENA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: TAU_KP_REL_PROTI_ZMENA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: TAU_KP_REL_PROTI_ZMENA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: TBD_KP_REL_PROTI_ZMENA.sql
-- Category: triggers
-- ============================================================================

PROMPT Deploying TBD_KP_REL_PROTI_ZMENA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Trigger TBD_KP_REL_PROTI_ZMENA
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "DB_DSA"."TBD_KP_REL_PROTI_ZMENA" 
before delete
on KP_rel_Protistrana
for each row



begin
  if(:old.ID_SPOLECNOST > 0) then
	  insert into KP_LOG_PROTISTRANA (
	    ID,
		ID_DATABAZE,
		ID_SPOLECNOST,
		S_ICO,
		S_NAZEV,
		S_NUMBER0,
		FIRMA_ID,
		EO_ID,
		BANKA_ID,
		DOID,
		KID,
		PLATINST,
		AKCE,
		DT_ZMENA,
		S_POM_ICO,
		EID,
		DATUMZALOZENI,
		ULICE,
		PSC,
		MESTO,
		STAT,
		DIC,
		UCETSCP,
		REGRMS,
		EKONOM_SEKTOR,
		TYPOSOBY,
	    S_AKCE
	  )
	  values (
	    :old.ID,
		:old.ID_DATABAZE,
		:old.ID_SPOLECNOST,
		:old.S_ICO,
		:old.S_NAZEV,
		:old.S_NUMBER0,
		:old.FIRMA_ID,
		:old.EO_ID,
		:old.BANKA_ID,
		:old.DOID,
		:old.KID,
		:old.PLATINST,
		:old.AKCE,
		:old.DT_ZMENA,
		:old.S_POM_ICO,
		:old.EID,
		:old.DATUMZALOZENI,
		:old.ULICE,
		:old.PSC,
		:old.MESTO,
		:old.STAT,
		:old.DIC,
		:old.UCETSCP,
		:old.REGRMS,
		:old.EKONOM_SEKTOR,
		:old.TYPOSOBY,
		'D'
	  );
  end if;
end;


/
ALTER TRIGGER "DB_DSA"."TBD_KP_REL_PROTI_ZMENA" ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: TBD_KP_REL_PROTI_ZMENA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: TBD_KP_REL_PROTI_ZMENA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: TBD_KP_REL_PROTI_ZMENA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: TBI_KP_KTG_PROTISTRANAVYJIMKY.sql
-- Category: triggers
-- ============================================================================

PROMPT Deploying TBI_KP_KTG_PROTISTRANAVYJIMKY.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Trigger TBI_KP_KTG_PROTISTRANAVYJIMKY
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "DB_DSA"."TBI_KP_KTG_PROTISTRANAVYJIMKY" 
before insert on KP_ktg_ProtistranaVyjimky
for each row


declare
    l_number number(10,0);


begin
	if ( NVL ( :new.id, 0 )  = 0 ) then
		select sq_KP_ktg_ProtistranaVyjimky.nextVal into :new.id from dual;
	end if;
    
    -- 22.12.2021 pre AX musi s_protistrana obsahovat cislo
    if :new.id_databaze = 500 then
        l_number := to_number(:new.s_protistrana);
    end if;
    
end;
/
ALTER TRIGGER "DB_DSA"."TBI_KP_KTG_PROTISTRANAVYJIMKY" ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: TBI_KP_KTG_PROTISTRANAVYJIMKY.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: TBI_KP_KTG_PROTISTRANAVYJIMKY.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: TBI_KP_KTG_PROTISTRANAVYJIMKY.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: TBI_KP_REL_PROTISTRANA.sql
-- Category: triggers
-- ============================================================================

PROMPT Deploying TBI_KP_REL_PROTISTRANA.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Trigger TBI_KP_REL_PROTISTRANA
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "DB_DSA"."TBI_KP_REL_PROTISTRANA" 
before insert on KP_rel_Protistrana
for each row



declare
  newId int;
begin
  if ( NVL ( :new.id, 0 )  = 0 ) then
	select sq_kp_rel_protistrana.nextVal into :new.id from dual;
  end if;
  
  newId := :new.id ;

  if(:new.ID_SPOLECNOST > 0) then
	  insert into KP_LOG_PROTISTRANA (
	    ID,
		ID_DATABAZE,
		ID_SPOLECNOST,
		S_ICO,
		S_NAZEV,
		S_NUMBER0,
		FIRMA_ID,
		EO_ID,
		BANKA_ID,
		DOID,
		KID,
		PLATINST,
		AKCE,
		DT_ZMENA,
		S_POM_ICO,
		EID,
		DATUMZALOZENI,
		ULICE,
		PSC,
		MESTO,
		STAT,
		DIC,
		UCETSCP,
		REGRMS,
		EKONOM_SEKTOR,
		TYPOSOBY,
	    S_AKCE
	  )
	  values (
	    newId,
		:new.ID_DATABAZE,
		:new.ID_SPOLECNOST,
		:new.S_ICO,
		:new.S_NAZEV,
		:new.S_NUMBER0,
		:new.FIRMA_ID,
		:new.EO_ID,
		:new.BANKA_ID,
		:new.DOID,
		:new.KID,
		:new.PLATINST,
		:new.AKCE,
		:new.DT_ZMENA,
		:new.S_POM_ICO,
		:new.EID,
		:new.DATUMZALOZENI,
		:new.ULICE,
		:new.PSC,
		:new.MESTO,
		:new.STAT,
		:new.DIC,
		:new.UCETSCP,
		:new.REGRMS,
		:new.EKONOM_SEKTOR,
		:new.TYPOSOBY,
		'I'
	  );
  end if;
end;


/
ALTER TRIGGER "DB_DSA"."TBI_KP_REL_PROTISTRANA" ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: TBI_KP_REL_PROTISTRANA.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: TBI_KP_REL_PROTISTRANA.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: TBI_KP_REL_PROTISTRANA.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: TBI_KP_REL_PROTI_SPATNEIC.sql
-- Category: triggers
-- ============================================================================

PROMPT Deploying TBI_KP_REL_PROTI_SPATNEIC.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Trigger TBI_KP_REL_PROTI_SPATNEIC
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "DB_DSA"."TBI_KP_REL_PROTI_SPATNEIC" 
before insert or update of id_spolecnost
on KP_rel_Protistrana
for each row



begin
	if ( :new.s_ico in ( '???', '0000000???', '0000000000' ) ) then
		:new.id_spolecnost := -2;
	end if;
end;

/
ALTER TRIGGER "DB_DSA"."TBI_KP_REL_PROTI_SPATNEIC" ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: TBI_KP_REL_PROTI_SPATNEIC.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: TBI_KP_REL_PROTI_SPATNEIC.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: TBI_KP_REL_PROTI_SPATNEIC.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: TBI_KP_S_MANUALTEXT.sql
-- Category: triggers
-- ============================================================================

PROMPT Deploying TBI_KP_S_MANUALTEXT.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Trigger TBI_KP_S_MANUALTEXT
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "DB_DSA"."TBI_KP_S_MANUALTEXT" 
before insert on KP_s_ManualText
for each row
begin
	if ( NVL ( :new.id, 0 )  = 0 ) then
		select SQ_KP_ManualText.nextVal into :new.id from dual;
	end if;
end;



/
ALTER TRIGGER "DB_DSA"."TBI_KP_S_MANUALTEXT" ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: TBI_KP_S_MANUALTEXT.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: TBI_KP_S_MANUALTEXT.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: TBI_KP_S_MANUALTEXT.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: TBI_QREPKONTROLAPK.sql
-- Category: triggers
-- ============================================================================

PROMPT Deploying TBI_QREPKONTROLAPK.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Trigger TBI_QREPKONTROLAPK
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "DB_DSA"."TBI_QREPKONTROLAPK" before
  INSERT ON "DB_DSA"."Q_REP_KONTROLA" FOR EACH row BEGIN IF inserting THEN IF :NEW."ID" IS NULL THEN
  SELECT SQ_Q_REP_KONTROLA.nextval INTO :NEW."ID" FROM dual;
END IF;
END IF;
END;

/
ALTER TRIGGER "DB_DSA"."TBI_QREPKONTROLAPK" ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: TBI_QREPKONTROLAPK.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: TBI_QREPKONTROLAPK.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: TBI_QREPKONTROLAPK.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: TBI_QREPKONTROLATM.sql
-- Category: triggers
-- ============================================================================

PROMPT Deploying TBI_QREPKONTROLATM.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Trigger TBI_QREPKONTROLATM
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "DB_DSA"."TBI_QREPKONTROLATM" before
  INSERT ON "DB_DSA"."Q_REP_KONTROLA" FOR EACH row 
BEGIN 
  IF inserting THEN 
      IF :NEW.DATE_INSERT IS NULL THEN :NEW.DATE_INSERT := SYSDATE;
      END IF;
  END IF;

END;

/
ALTER TRIGGER "DB_DSA"."TBI_QREPKONTROLATM" ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: TBI_QREPKONTROLATM.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: TBI_QREPKONTROLATM.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: TBI_QREPKONTROLATM.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: TBI_TOPASKLIENT_STAT.sql
-- Category: triggers
-- ============================================================================

PROMPT Deploying TBI_TOPASKLIENT_STAT.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Trigger TBI_TOPASKLIENT_STAT
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "DB_DSA"."TBI_TOPASKLIENT_STAT" 
before insert
on db_dsa.kp_s_topas_klienti
referencing new as new old as old
for each row



begin
	 select id into :new.kodStat from db_jt.kp_cis_stat where s_topasKod = :new.Stat;
	 exception when no_data_found then
		:new.kodStat := null;
end;

/
ALTER TRIGGER "DB_DSA"."TBI_TOPASKLIENT_STAT" ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: TBI_TOPASKLIENT_STAT.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: TBI_TOPASKLIENT_STAT.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: TBI_TOPASKLIENT_STAT.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: TBI_UCETPOHYBY.sql
-- Category: triggers
-- ============================================================================

PROMPT Deploying TBI_UCETPOHYBY.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Trigger TBI_UCETPOHYBY
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "DB_DSA"."TBI_UCETPOHYBY" 
BEFORE INSERT
ON DB_DSA.T_UCET_POHYBY
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
declare
		 c_0 char := '0';
		 c_9 char := '9';
BEGIN
	  for i in 1 .. length (:new.ucet) loop
	  		:new.c_znakUcet := substr ( :new.ucet, i, 1 );
			if ( :new.c_znakUcet between c_0 and c_9 ) then
				exit;
			end if;
			:new.c_znakUcet := '';
		end loop;
END;

/
ALTER TRIGGER "DB_DSA"."TBI_UCETPOHYBY" ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: TBI_UCETPOHYBY.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: TBI_UCETPOHYBY.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: TBI_UCETPOHYBY.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: TRAD_RADICIMPORTU.sql
-- Category: triggers
-- ============================================================================

PROMPT Deploying TRAD_RADICIMPORTU.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Trigger TRAD_RADICIMPORTU
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "DB_DSA"."TRAD_RADICIMPORTU" 
after delete on kp_radicImportu
for each row



begin

	null;	--ted ty zdrojove tabulky nemazu protoze nevyuzivam id_fronta => smazou se tim naslednym truncate

/*
	-- 'nevim' v ktere tabulce mel data tak delete na vsem, jednou se trefi
	delete t_ucet_pohyby where id_fronta = :old.id_fronta;
	delete q_rep_booking where id_fronta = :old.id_fronta;
	delete kp_tmp_zdrojDat where id_fronta = :old.id_fronta;
*/

/*	v triggeru nelze truncate = dela implicitni commit
	-- zatim to mam ze nemusim brat ohled na id_fronta, protoze nedelam z jedne db_link vice importu najednou
	if ( :old.s_dbLink = 'T' ) then
		P_ExecuteSQL ( 'truncate table t_ucet_pohyby' );
	elsif ( :old.s_dbLink = 'S' ) then
		P_ExecuteSQL ( 'truncate table kp_tmp_zdrojdat' );
	elsif ( :old.s_dbLink = 'Q' ) then
		P_ExecuteSQL ( 'truncate table q_rep_booking' );
	else
		raise_application_error ( -20000, 'neni kod link=' || :old.s_dbLink );
	end if;
*/
end;

/
ALTER TRIGGER "DB_DSA"."TRAD_RADICIMPORTU" ENABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: TRAD_RADICIMPORTU.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: TRAD_RADICIMPORTU.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: TRAD_RADICIMPORTU.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


-- ============================================================================
-- Deploy: XSDCDS.sql
-- Category: triggers
-- ============================================================================

PROMPT Deploying XSDCDS.sql...

DECLARE
    v_error_code VARCHAR2(20);
    v_error_msg VARCHAR2(4000);
    v_object_exists EXCEPTION;
    PRAGMA EXCEPTION_INIT(v_object_exists, -955);
BEGIN
    EXECUTE IMMEDIATE q'[
--------------------------------------------------------
--  DDL for Trigger XSDCDS
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "DB_DSA"."XSDCDS" 
BEFORE INSERT
ON DB_DSA.KP_REL_PROTISTRANA 
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW



DECLARE
tmpVar NUMBER;
BEGIN
   if ( :new.doid = 'CHRUMPETER' ) then
   	  raise_application_error ( -20001, 'xxx ' || :new.s_nazev );
   end if;
END ;


/
ALTER TRIGGER "DB_DSA"."XSDCDS" DISABLE
]';
    DBMS_OUTPUT.PUT_LINE('SUCCESS: XSDCDS.sql');
EXCEPTION
    WHEN v_object_exists THEN
        DBMS_OUTPUT.PUT_LINE('INFO: XSDCDS.sql - Object already exists, skipping');
    WHEN OTHERS THEN
        v_error_code := SQLCODE;
        v_error_msg := SUBSTR(SQLERRM, 1, 200);
        DBMS_OUTPUT.PUT_LINE('ERROR: XSDCDS.sql - ' || v_error_code || ': ' || v_error_msg);
        -- Continue with next object
END;
/


PROMPT Completed triggers deployment
PROMPT


-- ============================================================================
-- Post-Deployment: Check Invalid Objects
-- ============================================================================

PROMPT ============================================================================
PROMPT Checking for INVALID objects...
PROMPT ============================================================================

SELECT object_type, object_name, status
FROM user_objects
WHERE status = 'INVALID'
ORDER BY object_type, object_name;

-- ============================================================================
-- Post-Deployment: Recompile Invalid Objects
-- ============================================================================

PROMPT ============================================================================
PROMPT Recompiling INVALID objects...
PROMPT ============================================================================

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

            DBMS_OUTPUT.PUT_LINE('RECOMPILED: ' || obj.object_type || ' ' || obj.object_name);
        EXCEPTION
            WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE('FAILED: ' || obj.object_type || ' ' || obj.object_name || ': ' || SQLERRM);
        END;
    END LOOP;
END;
/

-- ============================================================================
-- Final Status Report
-- ============================================================================

PROMPT ============================================================================
PROMPT Final Object Status
PROMPT ============================================================================

SELECT object_type, status, COUNT(*) as count
FROM user_objects
GROUP BY object_type, status
ORDER BY object_type, status;

PROMPT ============================================================================
PROMPT DB_DSA Schema Deployment Complete
PROMPT ============================================================================

EXIT;
