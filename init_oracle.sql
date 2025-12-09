-- KIS Banking Application - Oracle Database Init Script
-- Generated from legacy PL/SQL code

-- Drop existing schema if exists (for clean reinstall)
DROP USER DB_JT CASCADE;

-- Create schema
CREATE USER DB_JT IDENTIFIED BY kis_db_jt_2024
  DEFAULT TABLESPACE USERS
  TEMPORARY TABLESPACE TEMP
  QUOTA UNLIMITED ON USERS;

-- Grant privileges
GRANT CONNECT, RESOURCE, CREATE VIEW, CREATE SYNONYM TO DB_JT;
GRANT UNLIMITED TABLESPACE TO DB_JT;

