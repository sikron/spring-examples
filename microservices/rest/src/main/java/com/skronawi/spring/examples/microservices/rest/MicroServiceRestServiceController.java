package com.skronawi.spring.examples.microservices.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

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

    /*
    the sink binding stuff from Endpoint would not work in here
     */
}
