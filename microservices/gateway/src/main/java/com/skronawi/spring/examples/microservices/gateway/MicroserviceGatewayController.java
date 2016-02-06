package com.skronawi.spring.examples.microservices.gateway;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@EnableCircuitBreaker
@RestController
public class MicroserviceGatewayController {

//    @Autowired
    private RestTemplate restTemplate = new RestTemplate();

    @HystrixCommand(fallbackMethod = "getAllFallback")
    @RequestMapping(path = "data", method = RequestMethod.GET)
    public Collection<String> getAll() {
        return this.restTemplate
                .exchange("http://localhost:8081/data", HttpMethod.GET, null,
                        new ParameterizedTypeReference<Collection<MicroServiceGatewayData>>() {
                        }).getBody().stream().map(MicroServiceGatewayData::getData).collect(Collectors.toList());
    }

    private Collection<String> getAllFallback() {
        return new ArrayList<>();
    }
}
