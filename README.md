# Example of API automation developed for PetStoreService - https://petstore.swagger.io/

## Used tools:

* Spring Boot 
* Rest Assured
* JUnit5
* Lombok
* Java Faker
* Allure
* Awaitility

## How to execute tests

Running tests without report: `mvn clean test`

To run tests with Allure report use below commands (report will be automaticaly served in the default browser):

* `mvn clean test allure:report`
* `mvn allure:serve`
    
#### General Allure report:
![General Allure report](https://media.discordapp.net/attachments/861178984499118093/861179009148649533/unknown.png?width=1295&height=611)  

![Suites](https://media.discordapp.net/attachments/861178984499118093/861179577198968842/unknown.png?width=1295&height=634)  
