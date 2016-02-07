package com.skronawi.spring.examples.websockets.communication;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.skronawi.spring.examples.websockets.communication")
public class AppConfig extends WebMvcConfigurerAdapter { //Spring uses this for initialising its web functionality

    /*
    i don't know, what this 'enable()' is for, but without it, requests don't work
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
