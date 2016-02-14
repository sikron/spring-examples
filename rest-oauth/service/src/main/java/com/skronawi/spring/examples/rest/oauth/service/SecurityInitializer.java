package com.skronawi.spring.examples.rest.oauth.service;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/*
needed for the springSecurityChain filter to be active, see
- http://www.mkyong.com/spring-security/spring-security-hello-world-annotation-example/
- https://github.com/spring-projects/spring-security-javaconfig/blob/master/quickstart.md
 */
public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {
}
