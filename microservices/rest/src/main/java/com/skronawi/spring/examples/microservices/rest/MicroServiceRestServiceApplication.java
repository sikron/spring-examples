package com.skronawi.spring.examples.microservices.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Collection;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroServiceRestServiceApplication {

    /*
    https://github.com/joshlong/bootiful-microservices
    https://www.youtube.com/watch?v=bSplThvNl8Q&index=10&list=PLx2By31njbhrs8caX08BEusyD_fBDu-XG
     */

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