package com.skronawi.spring.examples.amqp.mqc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class ErrorHandler implements org.springframework.util.ErrorHandler {

    @Autowired
    private Producer producer;

    public void handleError(Throwable throwable) {

    }
}
