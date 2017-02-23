# Simple-Restful-Banking-Service #

### About this project ###

* This is small project that demonstrates a simple REST based web service API to manage a simple Bank Account. 
* The API exposes RESTful web services end points that facilitate 3 cores bank account actions, namely, balance checking, deposit acceptance and withdrawal execution. 
* Version 1.0-SNAPSHOT

### Technologies ###
* The project is based on the Java 1.8 / Spring 4.3 / Spring Boot 1.4 / Spring Data technology stacks.
* It uses the HSQLDB embedded database,with the database stored in memory.
* It uses the Gradle/TestNg testing stack.
* Uses the JaCoCo Library for code coverage tracking and reporting.


### What is required to run the project? ###
* Java 1.7 and above should be installed, the JAVA_HOME enviroment variable might need to be setup.  
* Gradle should installed. It may also require groovy to installed.

### Running the app ###
* Use : gradlew clean bootRun
* Execute the above command on a terminal from the root directory of the project. This will start the Spring Boot application on localhost:8787
 
### Using Postman to test the endpoints (all end points are POST based) ###
* Postman can be used to test the working of the restful endpoints. It can be installed from the Chrome Store and runs only on Google Chrome.

* balance endpoint
Use http://localhost:8787/rest/account/balance , body : {"accountId" : "1" }  and "Content-Type: application/json"

* deposit endpoint
Use http://localhost:8787/rest/account/deposit , body : {"accountId" : "1","depositAmount" : 30000 }  and "Content-Type: application/json"

* withdrawal endpoint
Use http://localhost:8787/rest/account/withdrawal , body : {"accountId" : "1","withdrawalAmount" : 30000 }  and "Content-Type: application/json"
  
### On UNIX based systems, curl can also be used to test the endpoints. On Windows, where Cygwin is installed, curl can also be used ###
* Check balance
curl -H "Content-Type: application/json" -X POST -d "{\"accountId\":1}"  http://localhost:8787/rest/account/balance

* Deposit
curl -H "Content-Type: application/json" -X POST -d "{\"accountId\":1,\"depositAmount\":10000}" http://localhost:8787/rest/account/deposit

* Withdrawal
curl -H "Content-Type: application/json" -X POST -d "{\"accountId\":1,\"withdrawalAmount\":5000}" http://localhost:8787/rest/account/withdrawal


### Running the tests ###
* Use : gradlew test

### Tests and Code coverage Reports ###
Test reports will be generated here:
PROJECT_ROOT\build\reports\tests,index.html should give a summary of the test results.

The JaCoCo code coverage reports will be generated here:
PROJECT_ROOT\build\reports\coverage, index.html should give a summary of the code coverage.

Code coverage achieved for now is at 71%

### For comments, feedback or other communication  ###
* daima.dataservices (at) gmail.com
