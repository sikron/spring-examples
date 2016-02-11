package com.skronawi.spring.examples.rest.auth.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.skronawi.spring.examples.rest.auth.service") //needed HERE to find the REST related stuff! not e.g. in AppConfig
public class WebConfig extends WebMvcConfigurerAdapter {
}
