#!/bin/bash
# Import the correct variables
source ./variables.sh

# Creating the database
echo "CREATE DATABASE IF NOT EXISTS expensemanager CHARACTER SET utf8 collate utf8_unicode_ci" | mysql --user=${MYSQL_USERNAME} --password=${MYSQL_PASSWORD}
# Creating all the necessary DataSources
#     STEP #1: Creating the Module
${WILDFLY_LOCATION}/bin/jboss-cli.sh --connect --controller=localhost:9990 --command="module add --name=com.mysql --resources=${MYSQL_JDBC_FILE} --dependencies=javax.api,javax.transaction.api"
#     STEP #2: Creating the JDBC Driver Available
${WILDFLY_LOCATION}/bin/jboss-cli.sh --connect --controller=localhost:9990 --command="/subsystem=datasources/jdbc-driver=mysql:add(driver-name=mysql,driver-module-name=com.mysql,driver-class-name=com.mysql.jdbc.Driver)"
#     STEP #3: Creating the DataSource and linking to JDBC Driver
${WILDFLY_LOCATION}/bin/jboss-cli.sh --connect --controller=localhost:9990 --command="/subsystem=datasources/data-source=MySQLDS:add( driver-name=mysql, user-name=${MYSQL_USERNAME}, password=${MYSQL_PASSWORD}, connection-url=jdbc:mysql://localhost:3306/expensemanager, min-pool-size=5, max-pool-size=15, jndi-name=java:/jdbc/MySQLDS, enabled=true, validate-on-match=true, valid-connection-checker-class-name=org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLValidConnectionChecker, exception-sorter-class-name=org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLExceptionSorter )"
#     STEP #$: Setup security
cp foo.keystore ${WILDFLY_LOCATION}/standalone/configuration
${WILDFLY_LOCATION}/bin/jboss-cli.sh --connect --controller=localhost:9990 --command="/core-service=management/security-realm=UndertowRealm:add()"
${WILDFLY_LOCATION}/bin/jboss-cli.sh --connect --controller=localhost:9990 --command="/core-service=management/security-realm=UndertowRealm/server-identity=ssl:add(keystore-path=foo.keystore,keystore-password=secret, keystore-relative-to=jboss.server.config.dir)"
${WILDFLY_LOCATION}/bin/jboss-cli.sh --connect --controller=localhost:9990 --command="/subsystem=undertow/server=default-server/https-listener=https/:add(socket-binding=https,security-realm=UndertowRealm)"