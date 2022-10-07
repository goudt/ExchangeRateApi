# exchange-rate-api

[![Build Status](https://travis-ci.org/codecentric/springboot-sample-app.svg?branch=master)](https://travis-ci.org/codecentric/springboot-sample-app)
[![Coverage Status](https://coveralls.io/repos/github/codecentric/springboot-sample-app/badge.svg?branch=master)](https://coveralls.io/github/codecentric/springboot-sample-app?branch=master)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

Minimal [Spring Boot](http://projects.spring.io/spring-boot/) sample app.

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

Execute the `main` method in the `com.betvictor.exchangerateapi.ExchangeRateApiApplication` class from your IDE.

Alternatively you can run the following shell command

(Linux)
```shell
 ./mvnw spring-boot:run

```
(Windows)
```shell
 ./mvnw.cmd spring-boot:run

```
## Documentation
Run the application locally and navigate to `http://localhost:8080/swagger-ui/index.html` to see the swagger documantation page. 

## Notes
### Caching
* Every successful call to the external provider is cached in an in memory Map. 
* On every API request, we try to retrieve the rates from cache and if an entry doesn't exist or it's creation date is erlier 
than the time duration provided in application properties, then a call to the 3rd party provider is made.
* Sole purpose of the `com.betvictor.exchangerateapi.service.CacheService` abstract class is to provide a way 
for different caching mechanism to implement it (such as a Redis database) through extension.


