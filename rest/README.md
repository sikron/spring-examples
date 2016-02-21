# Description

This project is about Spring REST. There exist these modules:

* `businesslogic-api`:
the interface of the business-logic. the business-logic is just CRUD here.
* `businesslogic-impl`:
the implementation to the interface.
* `communication`:
the REST specific stuff, endpoints, exception-mapper, etc.
* `inmemory-persistence`:
implementation of the persistence interface of the `jpa` project, just to provide CRUD. is in-memory, so no database needed here.
* `service`:
the module, which ties all the upper modules together via Spring Bean Injection, resulting in a webapp.
* `it-core`:
commonly used stuff by the integration tests.
* `it-mvcmock`:
integration tests of the REST service with MockMvc.
* `it-boot`:
integration tests of the REST service with Spring Boot. Here a complete inmemory webapp is setup for the tests to go against.

# Purpose

* Getting to know Spring REST on server side
* Getting to know Integration Tests for Spring REST

# Requirements

* none

# What can it do?

* start the service in the `service` module via the maven jetty plugin, e.g. `mvn jetty:run`
* go to `http://localhost:8080/data`, and do CRUD (see the `DataController`)