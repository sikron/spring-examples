package com.skronawi.spring.examples.amqp.mqc;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class Producer {

    @Autowired
    private AmqpTemplate workingTemplate;
    @Autowired
    private AmqpTemplate retryTemplate;
    @Autowired
    private AmqpTemplate deadletterTemplate;

    public String send(String message) {

        //TODO in case of problems, e.g. try 3 times to send with 10 seconds delay

        MessageWithInfos messageWithInfos = new MessageWithInfos();
        messageWithInfos.setId(UUID.randomUUID().toString());
        messageWithInfos.setPayload(message);

        workingTemplate.convertAndSend(messageWithInfos);
        return messageWithInfos.getId();
    }

    public void retry(MessageWithInfos messageWithInfos) {

        messageWithInfos.setRetryCount(1);
        retryTemplate.convertAndSend(messageWithInfos);
    }

    public void deadletter(MessageWithInfos messageWithInfos) {
        deadletterTemplate.convertAndSend(messageWithInfos);
    }
}
