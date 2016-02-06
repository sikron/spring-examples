package com.skronawi.spring.examples.microservices.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class MicroServiceConfigServiceApplication {

    /*
    https://github.com/joshlong/bootiful-microservices-config
     */

    public static void main(String[] args) {
        SpringApplication.run(MicroServiceConfigServiceApplication.class, args);
    }
}
