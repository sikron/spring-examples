package com.skronawi.spring.examples.microservices.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@EnableBinding(Source.class)
@RestController
public class MicroserviceGatewaySource {

    @Autowired
    private Source source;

    @RequestMapping(path = "data", method = RequestMethod.POST)
    public void set(@RequestBody String data) {
        source.output().send(MessageBuilder.withPayload(data).build());
    }
}
