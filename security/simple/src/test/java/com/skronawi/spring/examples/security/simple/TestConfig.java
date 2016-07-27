package com.skronawi.spring.examples.security.simple;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class TestConfig {

    @Bean
    public MyService myService(){
        //return new MySecuredService();
        return new MyPreAuthorizedService();
    }
}
