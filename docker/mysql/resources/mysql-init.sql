CREATE USER 'acntech_debezium'@'%' IDENTIFIED BY 'P455w0rd';

GRANT SELECT,RELOAD,REPLICATION SLAVE,REPLICATION CLIENT,SUPER ON *.* TO 'acntech_debezium'@'%';

FLUSH PRIVILEGES;

SHOW GRANTS for acntech_debezium;