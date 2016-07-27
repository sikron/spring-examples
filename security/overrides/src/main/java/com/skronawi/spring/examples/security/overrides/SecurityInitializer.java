package com.skronawi.spring.examples.security.overrides;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityInitializer extends GlobalMethodSecurityConfiguration{
}
