This is the exprog.hu HoneyWeb and BeeComposit reference implementation project for DynaForm force presentation. A disability carpooling project that made in Hungary based on Seacon Europe Ltd. carpool modul for SeaFleet.

I make an @Entity annotatons based database manager framework for easy and rapid administration program development that use Primefaces and its Extesions package.

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

# Install and configure components
(under construction)