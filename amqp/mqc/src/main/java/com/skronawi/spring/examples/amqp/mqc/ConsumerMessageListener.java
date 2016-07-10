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
            throw new IllegalStateException(e); //TODO put into dead-letter queue here, as the message is really poisoned
        }

        try {
            consumerCallback.handle(messageWithInfos);
        } catch (Exception e) {

//            System.out.println(e); // -> so no exception -> so not in deadletter queue

            if (messageWithInfos.getTryCount() < 3) {
                producer.retry(messageWithInfos);
            } else {
                producer.deadletter(messageWithInfos);
            }
        }
    }

    public void setCallback(ConsumerCallback consumerCallback) {
        this.consumerCallback = consumerCallback;
    }
}
