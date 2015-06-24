# Configuration
**Prerequisite**

1. Installed MySQL Server
2. Downloaded Wildfly 8.2.0.Final
http://wildfly.org/downloads/

**Setup**

1. Download MySQL jdbc driver
http://dev.mysql.com/downloads/connector/j/
2. Fill out variables.sh
3. Start Wildfly by running `bin/standalone.sh --server-config=standalone.xml`
4. Execute setup.sh
5. Build with `mvn clean install`
5. Execute deploy.sh
6. Restart Wildfly (start with the same configuration file)
6. Visit
https://localhost:8443/expense-manager/

# Demo
https://expensemanager-supercool.rhcloud.com/

# Project Overview
### Entity-Relationship Diagram
![ERD](http://i.imgur.com/1IVAKZl.png)

### Rule
Užívateľ si vytvára pravidlá, podľa ktorých sa mu platby automaticky kategorizujú.  
Užívateľ si môže definovať pravidlá podľa:
* **čísla účtu** - číslo účtu v pridávanej platbe sa zhoduje s číslom účtu pravidla
* **výskytu výrazu v poznámke** - v poznámke pridávanej platby sa nachádza výraz definovaný v pravidle 
 
_Pravidlo podľa čísla účtu má prednosť pred pravidlom s výrazom v poznámke._

### Payment
Popis platieb

# Testing
All tests are started by command mvn clean test -Parq-wildfly-remote if there is running wildfly server.
if no wildfly server is running then by command clean test -Parq-wildfly-managed.
