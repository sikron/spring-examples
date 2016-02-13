# Description

This is a simple REST service with Spring Boot. It is bootstrapped with the `resources/bootstrap.properties`, where it finds the address of the config server to get the actual configuration from.
There also its identification resides as `spring.application.name`. This is used by the config server to provide the right properties.

# Features

* It is configured via a Spring Cloud Config server. This config can be refreshed during runtime (see the `/configvalue` endpoint in the `MicroServiceRestServiceController`)
* It can register itself on a Eureka service
* It serves as a message sink for a Redis stream (see its properties in the config server for the id of the stream binding)

# Requirements

* a Redis server on localhost:6379, e.g. in a vagrant box like in https://github.com/JasonPunyon/redishobo
* a instance of the `config` server
* a instance of the `eureka` server

# What can it do?

* start the REST service via the `com.skronawi.spring.examples.microservices.rest.MicroServiceRestServiceApplication`
* perform a `GET http://localhost:8081/data` to get the plain data
* perform a `GET http://localhost:8081/configvalue` to get the dynamically refreshable configvalue. 
* change this value in the `microservice-rest-service.properties` in the **deployed** running config server
* perform a `POST http://localhost:8081/refresh` and then a `GET http://localhost:8081/configvalue` to see the effect