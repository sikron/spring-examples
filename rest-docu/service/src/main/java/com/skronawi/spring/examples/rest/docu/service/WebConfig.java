package com.skronawi.spring.examples.rest.docu.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.skronawi.spring.examples.rest.docu.service")
public class WebConfig extends WebMvcConfigurerAdapter {
}
