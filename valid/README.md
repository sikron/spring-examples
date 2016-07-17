# Description

Playing around with validation in Spring.

# Modules

* `simple`
Just a first try. Found out, that the `@Valid` annotation only works in the REST level and not e.g. in the business
logic level
* `custom`
Trying to make a own annotation `@MyValid` and an aspect to also be able to validate in business logic