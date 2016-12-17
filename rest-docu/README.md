# Description

This project is about Spring REST Documentation. There exist these modules:

* `service`:
the service with example endpoints
* `mvc`:
the documentation is generated using Spring's MockMVC 
* `restassures`:
the documentation is generated using RESTassured

Here JUnit is used as test framework as it is better integrated than TestNG when it comes to doc generation.

# What can it do?

* the tests generate "snippets" in the `target/generated-snippets` folder. these are then combined with hand-written
documentation from `src/docs/asciidocs` in the corresponding test module
* the final documentation is then available in `target/generated-docs`
* just do a `mvn clean package` in one of the test modules
  * in the (unit-)test phase the snippets will  be generated
  * in the package phase the asciidoc will be assembled