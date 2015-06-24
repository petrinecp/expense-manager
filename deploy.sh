#!/bin/bash
# Import the correct variables
source variables.sh

# Deploy application to Wildfly
${WILDFLY_LOCATION}/bin/jboss-cli.sh --connect --command="deploy target/expense-manager.war --name=ExpenseManager --runtime-name=expense-manager.war"