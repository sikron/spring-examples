# Description

This is a Eureka server with Spring Boot. Other services can register against it. It is bootstraped with `bootstrap.properties` and gets is actual config from the config server.

# Features

* i currently don't know, what's the purpose of this server for the other microservices here. i think it is only a example.

# Requirements

* a instance of the `config` server

# What can it do?

* start the service via the `com.skronawi.spring.examples.microservices.eureka.MicroServiceEurekaApplication`
* navigate your browser to`http://localhost:8761/` to see the currently registered services
* without the config server the port is just 8080