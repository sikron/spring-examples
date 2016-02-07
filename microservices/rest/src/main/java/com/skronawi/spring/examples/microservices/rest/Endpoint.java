package com.skronawi.spring.examples.microservices.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

@EnableBinding(Sink.class)  //the sink endpoint of the message stream.
@IntegrationComponentScan //needed for the @ServiceActivator to be bound
@MessageEndpoint //Spring sets up the message stream
public class Endpoint {

    @Autowired
    private DataRepo dataRepo;

    @ServiceActivator(inputChannel = Sink.INPUT)
    public void set(String data) {
        dataRepo.add(new MicroServiceRestData(data));
    }
}
