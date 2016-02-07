package com.skronawi.spring.examples.microservices.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.EnableZuulServer;

@EnableDiscoveryClient
@SpringBootApplication
//@EnableZuulProxy //another example, i did not quite understand
public class MicroserviceGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceGatewayApplication.class, args);
    }
}
