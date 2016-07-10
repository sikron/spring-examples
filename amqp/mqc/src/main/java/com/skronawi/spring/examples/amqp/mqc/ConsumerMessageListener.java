package com.skronawi.spring.examples.amqp.mqc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class ConsumerMessageListener implements MessageListener {

    @Autowired
    private Producer producer;

    private ConsumerCallback consumerCallback;
    private final ObjectMapper objectMapper;

    public ConsumerMessageListener() {
        objectMapper = new ObjectMapper();
    }

    public void onMessage(Message message) {
        MessageWithInfos messageWithInfos = null;
        try {
            messageWithInfos = objectMapper.readValue(message.getBody(), MessageWithInfos.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        try {
            consumerCallback.handle(messageWithInfos);
        } catch (Exception e) {
            //TODO re-queue
            System.out.println(e); // -> so no exception -> so not in deadletter queue
        }
    }

    public void setCallback(ConsumerCallback consumerCallback) {
        this.consumerCallback = consumerCallback;
    }
}
