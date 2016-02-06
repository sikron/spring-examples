package com.skronawi.spring.examples.microservices.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;

//@EnableBinding(Sink.class)
//@IntegrationComponentScan //?
//@MessageEndpoint
@RefreshScope
@RestController
class MicroServiceRestServiceController {

    @Autowired
    private DataRepo dataRepo;

    @Value("${configvalue}")
    private String configvalue;

    @RequestMapping(path = "data", method = RequestMethod.GET)
    public Collection<MicroServiceRestData> getAll() {
        return dataRepo.getAll();
    }

    @RequestMapping(method = RequestMethod.GET, path = "configvalue")
    public String getConfigvalue() {
        return this.configvalue;
    }

//    @ServiceActivator(inputChannel = Sink.INPUT)
//    public void set(String data) {
//        datas.add(new MicroServiceRestData(data));
//        System.out.println("******* data " + data);
//    }
}
