# Solution for Task 2

## Getting Started

------------

These instructions will get you a copy of the project up and running on your local machine.

### Before we proceed

------------


Maven is required to build and execute the TestNG xml. Compiler and Java version are as scpecified:

```
Verify maven:  mvn -version
Java version: openjdk version "15.0.1" 2020-10-20

 <properties>
        <maven.compiler.source>15</maven.compiler.source>
        <maven.compiler.target>15</maven.compiler.target>
 </properties>

```
### Tools used

------------
* RestAssured
* TestNG
* Cucumber, Picocontainer 
* slf4j logging
* Extent Reports

### Installing

------------


```
mvn clean install -DskipTests
```

### Brief description of the solution implementations

------------


##### 1. Implementation with TestNG and RestAssured framework-

This solution has the entire execution in one test class. I have created bookings with test data driven from Excel and updated these booking with random test data generated locally. Further tests in this class are to get, delete and get again to validate the delete action. This will generate reports created with TestNg custom listeners.


```
TestNG test class - src/test/missionMarsTests/testNG/TestNGSolutionTask2Test.java
Excel file location for test data - restful-booking-api-automation/src/test/resources

```
##### 1. Implementation with TestNG, RestAssured and Cucumber -

This souliton has two feature files implemented to solve this problem(independetly). Feature file `firstSolution.feature` covers two Scenarios with examples to runs create, update, get and delete events. While the other feature file `secondSolution.feature` runs these all events within one Scenario with help of data tables. Below is the further related data with this implentation.
```
Cucumber step definition location- src/main/java/cucumber/stepDefinitions
Feature files- src/main/resources/features
```
Tests are not executed in parallel to simplify assertion with previous events.


## Running the tests

------------

The project has the soultion implemented with TestNG, RestAssured and Cucumber. There are 3 solutions implemented covering all the required test cases.

First solution is made with TestNG and RestAssured framework.
Run it with below command inside project directory:

```
mvn test -DsuiteXml=testNg.xml
```

Second solution is made with TestNG and Cucumber and you can run it with below command:
```
mvn test -DsuiteXml=cukesTestNG.xml
```
Please read below test descriptions before running the tests.



### Reporting

------------


For exectuion with cukesTestNG.xml, reports are located at``` restful-booking-api-automation/target/cucumber ```

For execution with testng.xml, report will be available inside```restful-booking-api-automation/report ```





------------




