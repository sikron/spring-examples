package com.skronawi.spring.examples.rest.auth.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@ComponentScan(basePackages = "com.skronawi.spring.examples.rest.auth.service")//needed also here to find the SecurityInitializer for the 'springSecurityFilterChain'
public class AppConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new TryoutUserDetailsService();
    }
}
