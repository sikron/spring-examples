package com.skronawi.spring.examples.amqp.mqc;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Consumer {

    @Autowired
    private SimpleMessageListenerContainer simpleMessageListenerContainer;

    public void listen(ConsumerCallback consumerCallback) {

        ConsumerMessageListener messageListener = (ConsumerMessageListener) simpleMessageListenerContainer.getMessageListener();
        messageListener.setCallback(consumerCallback);

        //TODO simpleMessageListenerContainer starts listening immediatly. what happens to messages, as long as no callback is set?
        //of course, only 1 callback can be set
    }
}
