package com.skronawi.spring.examples.security.overrides;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class TestConfig {

    @Bean
    public MyService myService(){

        //annotations on method level override same annotations on class level
        //return new MySecuredService();
        //return new MyPreAuthorizedService();
        //return new MySecuredPreAuthorizedService(); //also here, although annotations are different !?!?!

        //this behaves weird !! see https://github.com/spring-projects/spring-security/issues/2116
        return new MyPreAuthorizedSecuredService();
    }
}
