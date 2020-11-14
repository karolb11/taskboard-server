Repozytorum nie zawiera pliku application.properites. Nalezy go dodac recznie.
Wzor wymaganego pliku application.properties:

## Server Properties
server.port= 5000

## Spring DATASOURCE (DataSourceAutoConfiguration & DataSourceProperties)
spring.datasource.url= jdbc:mysql://***UZUEPELNIC***/taskboard?useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false
spring.datasource.username=***UZUEPELNIC***
spring.datasource.password=***UZUEPELNIC***

## App Properties
app.jwtSecret= ***UZUEPELNIC***
app.jwtExpirationInMs = 604800000

## Hibernate Properties

# The SQL dialect makes Hibernate generate better SQL for the chosen database
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5InnoDBDialect
spring.jpa.hibernate.ddl-auto = update

## Hibernate Logging
logging.level.org.hibernate.SQL= DEBUG

# Initialize the datasource with available DDL and DML scripts
spring.datasource.initialization-mode=always

## Jackson Properties
spring.jackson.serialization.WRITE_DATES_AS_TIMESTAMPS= false
spring.jackson.time-zone= UTC
