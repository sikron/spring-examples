package com.skronawi.spring.examples.amqp.javaconfig;

public class MessageHandler {

    public void handleMessage(Message message) {
        System.out.println(message);
    }
}
