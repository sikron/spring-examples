# Description

This is a simple REST service with Spring Boot. It serves as a gateway from the outside to the `rest` microservice 'inside'.

# Features
* It relays REST calls to the `rest` service
* It serves as a message source for a Redis stream (see its properties in the config server for the id of the stream binding)
* It uses a Hystrix circuit breaker for the `GET http://localhost:8082/data` call with a empty fallback in case of problems with the `rest` service

# Requirements

* a Redis server on localhost:6379, e.g. in a vagrant box like in https://github.com/JasonPunyon/redishobo
* a instance of the `config` server
* a instance of the `eureka` server
* a instance of the `rest` server

# What can it do?

* start the REST service via the `com.skronawi.spring.examples.microservices.gateway.MicroserviceGatewayApplication`
* perform a `GET http://localhost:8082/data` to get the plain data (relayed from the `rest` service)
* perform a `POST http://localhost:8082/data` with a proper JSON, which gets relayed to the `rest` service via the Redis stream
* perform a `GET http://localhost:8082/data` again to see the created JSON