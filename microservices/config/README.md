# Description

This is a config server with Spring Boot. It provides the `properties` for other services. Those reside in the `resources` directory.
These files are named according to the values of the `spring.application.name` in the `bootstrap.properties` of the corresponding services.
Usually the config files reside in a Github repo, but i wanted to keep it simple.
Josh's config is in https://github.com/joshlong/bootiful-microservices-config, just for reference.

# Features

* none

# Requirements

* none

# What can it do?

* start the service via the `com.skronawi.spring.examples.microservices.config.MicroServiceConfigServiceApplication`
* perform a `GET http://localhost:8888/microservice-rest-service/native` to get the config of the REST service.
* the other services accordingly