# Description

Playing around with validation in Spring.

# Modules

* `mvc`
Just a first try. Found out, that the `@Valid` annotation only works in the REST level and not e.g. in the business
logic level. Also found out, that implementations of `org.springframework.validation.Validator` are only really used
when MVC is used.
* `aspect`
Trying to make a own annotation `@MyValid` and an aspect to also be able to validate in business logic.
* `validated`
Basically the same as `aspect`, but using the `@Validated` on the service class, Spring performs the validation itself
without the need for the aspect. But again, the error messages are not resolved correctly. This has to be done e.g. in
a REST exception handler