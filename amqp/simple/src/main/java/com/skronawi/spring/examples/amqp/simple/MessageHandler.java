package com.skronawi.spring.examples.amqp.simple;

public class MessageHandler {

    public void handleMessage(Message message){
        System.out.println(message);
    }
}
