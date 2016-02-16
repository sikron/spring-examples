package com.skronawi.spring.examples.rest.oauth.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.skronawi.spring.examples.rest.oauth.service")
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        return new TheUserDetailsService();
    }
}
