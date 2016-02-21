package com.skronawi.spring.examples.jsp.communication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class RootConfig {

    @Bean
    public TodosRepo todosRepo() {
        return new TodosRepo();
    }
}
