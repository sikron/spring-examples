# Description

Playing around with validation in Spring.

# Modules

* `simple`
Just a first try. Found out, that the `@Valid` annotation only works in the REST level and not e.g. in the business
logic level
* `custom`
Trying to make a own annotation `@MyValid` and an aspect to also be able to validate in business logic
* `validated`
Basically the same as `custom`, but using the `@Validated` on the service class, Spring performs the validation itself
without the need for the aspect. But again, the error messages are not resolved correctly. This has to be done e.g. in
a REST exception handler