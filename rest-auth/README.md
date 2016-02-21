# Description

This project is about Spring REST Authentication. There exist these modules:

* `rest-auth-service`:
a complete REST service with Spring Java configs. there exist resources with different levels of authentication and authorization
* `it-mock`:
integration tests of the REST service with MockMvc.

# Purpose

* Getting to know Spring REST Authentication on server side
* Getting to know Integration Test support for Authentication

# Requirements

* none

# What can it do?

* start the service in the `rest-auth-service` module via the maven jetty plugin, e.g. `mvn jetty:run`
    * `GET http://localhost:8080/catgold`
is unauthenticated and returns "cheap plunder"
    * `GET http://localhost:8080/treasure`
is authenticated with plain basic auth and returns "diamonds and gold". only those users from the `TheUserDetailsService` are allowed.
    * `DELETE http://localhost:8080/treasure`
is authenticated with plain basic auth and empties the treasure. "diamonds and gold" is returned, and subsequent requests get only "empty". only the ADMIN user donald may do this.

or

* start the integration tests `AuthenticationTest`, `RoleSecurityByAuthTest` or `RoleSecurityByUserTest`

# Different Approaches

In `WebSecurityConfig` there exist several try-outs of method implementations. these are my learning steps, so to say.

The limitation, that only ADMIN users may claim (`DELETE`) the treasure, is
 
* either in `ValuableResource` via the `@PreAuthorize` or 
* in the `WebSecurityConfig` in the `configure(HttpSecurity http)` via the `.hasRole("ADMIN")`.