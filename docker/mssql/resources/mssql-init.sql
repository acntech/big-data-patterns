CREATE DATABASE acntech;

USE acntech;

CREATE LOGIN acntech WITH PASSWORD='P455w0rd';
CREATE USER acntech FROM LOGIN acntech;

CREATE SCHEMA acntech AUTHORIZATION acntech;

ALTER USER acntech WITH DEFAULT_SCHEMA = acntech;

GRANT CREATE TABLE TO acntech;
GRANT SELECT, INSERT, UPDATE, DELETE ON SCHEMA :: acntech TO acntech;

EXEC sys.sp_cdc_enable_db;

EXEC sys.sp_cdc_enable_table
     @source_schema = N'acntech',
     @source_name = N'ACCOUNT',
     @role_name = NULL;

EXEC sys.sp_cdc_enable_table
     @source_schema = N'acntech',
     @source_name = N'STATUS',
     @role_name = NULL;

CREATE LOGIN acntech_debezium WITH PASSWORD='P455w0rd';
CREATE USER acntech_debezium FROM LOGIN acntech_debezium;

ALTER USER acntech_debezium WITH DEFAULT_SCHEMA = cdc;

ALTER ROLE db_owner ADD MEMBER acntech_debezium; -- Should use dedicated role
