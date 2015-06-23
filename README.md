# Configuration
1. Download MySQL jdbc driver
http://dev.mysql.com/downloads/connector/j/
2. Fill out variables.sh
3. Start Wildfly by running bin/standalone.sh
4. Execute setup.sh

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