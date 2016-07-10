package com.skronawi.spring.examples.amqp.mqc;

public interface ConsumerCallback {

    void handle(MessageWithInfos messageWithInfos);
}
