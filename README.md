![Exprog.hu](/Homework4Telekocsi/development/exprog-open.png?raw=true "exprog.hu")

This is the exprog.hu HoneyWeb and BeeComposit reference implementation project for DynaForm force presentation. A disability carpooling project that made in Hungary based on Seacon Europe Ltd. carpool modul for SeaFleet. Program licensed under GPLv3.

I make an @Entity annotations based database manager framework for easy and rapid administration program development that use Primefaces and its Extesions package.

I can rapidly connect database structure creation with JSF GUI creation with dynamic forms, filterable and orderable tables from entity annotations.

Additional I can make JSF GUIs with Velocity templates from annotated entities, so JSF can be dynamic and static generated for old codebase users, and I can generate any non JSF (like angular) GUIs. I'd like make mixed JavaEE and Microprofile services with Keycloak. With this code I can easily help developers to migrate from old codebase to new designed codebase.

HoneyWeb is the basic processor and GUI buildre tool for annotated entites.

BeeComposit is the basic user interface builder for login, user group and rights management.

I use this configuration and components:

- PostgreSQL database server

- Wildfly 11 application server

- PrimeFaces 6.2.3 and PrimeFaces-Extensions 6.2.3 JSF2 GUI

- Maven project builder

- JBoss Developer Studio 11.3.0.GA or JBoss Tools 4.5.3.Final plugin with Eclipse Oxygen 3 Java IDE

- JavaMail

- Quarzt scheduling with database persistance

- JasperReports for printable reports

- Hibernate JPA

- Activiti BPML (future plane)

- Multi language support

With 18 years development experience I found that the database manager systems has a simple structure, that can organized around few things. The main entity annotations says everithing about the strategy:

- @EntityInfo, @EntityFieldInfo, @LookupFieldInfo
- @DynamicMirror, @QueryInfo, @QueryFieldInfo
- @EntitySpecificRights, @FieldRightsInfo, @FieldEntitySpecificRightsInfo,
- @EditorInfo, @EditorFieldInfo

You can view sample video about development at https://exprog.hu/yourchoice.html and discover usage at https://exprog.hu/Homework4Telekocsi

# Mind map
![Mind map](/Homework4Telekocsi/development/mindmap.png?raw=true "Mind map")

# Generic UML descriptions

## BeeComposit
![BeeComposit UML](/Homework4Telekocsi/development/beecomposit.gif?raw=true "BeeComposit UML")

## HoneyWeb
![HoneyWeb UML](/Homework4Telekocsi/development/honeyweb-components.gif?raw=true "HoneyWeb UML")

## Carpooling UML
![Carpooling UML](/Homework4Telekocsi/development/telekocsi.gif?raw=true "Carpooling UML")

Shared XMind map: http://www.xmind.net/m/9xYh

# Install and configure components
## Requirements
- Java enviroment (JRE or JDK)
- PostgreSQL database
- Wildfly JavaEE application server
- Maven

## Install JRE or JDK
- Download JRE or JDK from Oracle (http://www.oracle.com/technetwork/java/javase/downloads/index.html) or from OpenJDK (http://openjdk.java.net/)
- Install where you want (I will refere to JAVA_HOME as your installation directory if needed)
- Setup enviromet variables (please refer to Java installation documentation)

## Install Maven
- Download Maven from https://maven.apache.org/
- Install where you want (I will refer to MAVEN_HOME as your installation directory if needed)
- Setup enviroment variables (please refer to Maven installation documentation)

## Install Wildfly
- Download Wildfly from http://wildfly.org/downloads/ (currently I tested with wildfly 11)
- Install where you want (I will refere to WILDFLY_HOME as your installation directory)
- Set up enviroment variables (please refer to Wildfly installation documentation)

### Modify $WILDFLY_HOME/standalone/configuration/standalone.xml
#### Add enviroment constants to naming section
- velocityLogFile: where you want to logging the template transformations
- workingDirectory: where the template files located
- webContext: where you want to appear the program context
```xml
        <subsystem xmlns="urn:jboss:domain:naming:2.0">
            <bindings>
                <simple name="java:jboss/Homework4Telekocsi/workingDirectory" value="/var/exprog" type="java.lang.String"/>
                <simple name="java:jboss/Homework4Telekocsi/velocityLogFile" value="/var/exprog/velocity.log" type="java.lang.String"/>
                <simple name="java:jboss/Homework4Telekocsi/webContext" value="http://localhost:8080/Homework4Telekocsi" type="java.lang.String"/>
            </bindings>
            <remote-naming/>
        </subsystem>
```
#### Add security realm
```xml
        <subsystem xmlns="urn:jboss:domain:security:2.0">
            <security-domains>
...
                <security-domain name="telekocsiRealm">
                    <authentication>
                        <login-module code="Database" flag="required">
                            <module-option name="dsJndiName" value="java:jboss/datasources/Homework4TelekocsiDS"/>
                            <module-option name="principalsQuery" value="select password from v_active_user where username=?"/>
                            <module-option name="rolesQuery" value="select group_name as Role,'Roles' from user_join_group where user_name=?"/>
                            <module-option name="unauthenticatedIdentity" value="anonymousUser"/>
                            <module-option name="hashAlgorithm" value="SHA-256"/>
                            <module-option name="hashEncoding" value="base64"/>
                        </login-module>
                    </authentication>
                </security-domain>
...
            </security-domains>
        </subsystem>
```

#### Add PostgreSQL JDBC driver to Wildfly
```xml
        <subsystem xmlns="urn:jboss:domain:datasources:5.0">
            <datasources>
...
                <drivers>
....
                    <driver name="org.postgresql" module="org.postgresql">
                        <xa-datasource-class>org.postgresql.xa.PGXADataSource</xa-datasource-class>
                    </driver>
...
...
            </datasources>
        </subsystem>
...
...
```

### Add JDBC jar file to Wildfly modules directory
- Download PostgreSQL JDBC driver from https://jdbc.postgresql.org/ (I tested with version 42.2.1)
- make 'postgresql' directory in $WILDFLY_HOME/modules/system/layers/base/org/
- make 'main' directory in $WILDFLY_HOME/modules/system/layers/base/org/postgresql
- copy postgresql-42.2.1.jar to this 'main' directory
- make module.xml file to this 'main' directory next to the jar file with content bellow:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.5" name="org.postgresql">
    <resources>
        <resource-root path="postgresql-42.2.1.jar"/>
    </resources>
    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
        <module name="javax.servlet.api" optional="true"/>
    </dependencies>
</module>
```

## Install PostgreSQL
- Download PostgreSQL from https://www.postgresql.org/download/ (Currently I tested with 9.6)

### Import database tables - using pgAdmin or psql in a terminal (please see the Postgres user manuals for decide what you want to do)
- Create database user (please create user with parameters what you set in Homework4Telekocsi/src/main/webapp/WEB-INF/Homework4Telekocsi-ds.xml)
- Set rights to you user for full rights to your database. With this wser program will create tables.
- Create Quartz tables with lunching SQL scripts located at Homework4Telekocsi\src\main\db\quartz_tables_postgres.sql 


## Deploy to Wildfly
- Build the project with maven in root directory where pom.xml located
```bash
mvn package
```
- Then copy generated war to your $WILDFLY_HOME/standalone/deplyomets direcory

### Start Wildfly
- Lunch wildfly with $WILDFLY_HOME/bin/standalone.[sh/bat] - application will create remaining tables in PostgreSQL database

## Create your first admin user
```sql
CREATE OR REPLACE VIEW v_active_user AS 
 SELECT u.sqlserverloginname AS username,
    u.osuserpassword AS password
   FROM systemuser u
  WHERE u.enabled = true;

ALTER TABLE v_active_user
  OWNER TO pzoli;

INSERT INTO systemuser (id, sqlserverloginname, enabled, osuserpassword, username) VALUES
(1, 'a@b.hu', true, 'jjXCzTv2ZBvbDiBQt2kyy7LmA0oN2swdm+qCprpX988=', 'Papp Zoltán (integrity)');

INSERT INTO user_join_group (user_name, group_name) VALUES
('a@b.hu', 'ROLE_ADMIN');

INSERT INTO public.user_join_group(
	group_name, user_name)
	VALUES ('ROLE_DEVELOPER', 'a@b.hu');
```

With this specified user you can log in with username 'a@b.hu' and password 'q' (hash256 algorithm, salting development require here!)

## Login to your program
If everithing processed well, you can open program in a browser http://localhost:8080/Homework4Telekocsi

![Opening screen](/Homework4Telekocsi/development/opening-screen.png?raw=true "Opening screen")
(I configured Wildfly to operate on 8088 port)

# Sponsored by

## Seacon Hungary Ltd.

[![Seacon](/Homework4Telekocsi/development/seacon-logo.png?raw=true "https://www.seacon.hu")](https://www.seacon.hu)

## Versions

### rev.: 2018.05.10 19:20
#### Errors
- Code generation not work well because working directory not set everiwhere and template not uploaded to git repository (because Seacon Seafleet code generation privacy policy). You feel free modify it for use.
#### Help needed
- Chat service with OmniFace and Websockets
- Import exsisting elements into project (like NCF and QRCode handling for ticketing, PDF generation, SMS sending)
- Maven archtype generation from HoneyWeb and BeeComposit
- Jar package generation from HoneyWeb and BeeComposit
