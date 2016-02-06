package com.skronawi.spring.examples.microservices.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class MicroServiceEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroServiceEurekaApplication.class, args);
    }
}
