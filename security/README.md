# Description

This project is about Security in Spring. There exist these modules:

* `overrides`:
I just wanted to understand, how the `@PreAuthorize` and `@Secured` worked and how they could be combined. E.g. on class-level
and on method-level and also mixed. I realized, that the `@PreAuthorize` should be preferred as it is more flexible. Also
i found out that mixing those two annotations in 1 class does not work properly. It should be avoided.  
What can be said as summary is: "annotations on method level override same annotations on class level"

# How to start the Tests

In the TestConfig the annotated service implementation can be chosen to be used for the tests. Just out-comment the other
ones. The services with mixed annotations behave not as expected, see `https://github.com/spring-projects/spring-security/issues/2116`.