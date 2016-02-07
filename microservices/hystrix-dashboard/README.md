# Description

This is a Hystrix Dashboard for the circuit breaker in the `gateway` module.

# Features
* it shows the state of the circuit breaker

# Requirements

* a Redis server on localhost:6379, e.g. in a vagrant box like in https://github.com/JasonPunyon/redishobo
* a instance of the `config` server
* a instance of the `eureka` server
* a instance of the `rest` server
* a instance of the `gateway` server
* a few calls to the `GET http://localhost:8082/data` of the gateway

# What can it do?

* start the service via the `com.example.HystrixDashboardApplication`
* navigate your browser to`http://localhost:8010/` to see the state of the circuit breaker
* if you kill the `rest` service and perform a few `GET http://localhost:8082/data`, then you will see the effect on the dashboard