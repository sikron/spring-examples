package com.skronawi.spring.examples.microservices.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient //registering at the eureka service
@SpringBootApplication
public class MicroServiceRestServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroServiceRestServiceApplication.class, args);
    }

    @Bean
    public DataRepo dataRepo() {
        DataRepo dataRepo = new DataRepo();
        dataRepo.add(new MicroServiceRestData("a"));
        dataRepo.add(new MicroServiceRestData("b"));
        dataRepo.add(new MicroServiceRestData("c"));
        return dataRepo;
    }
}