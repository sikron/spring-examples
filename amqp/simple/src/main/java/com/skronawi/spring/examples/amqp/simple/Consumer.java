package com.skronawi.spring.examples.amqp.simple;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Only exists to load the context.xml and start consuming.
 */
public class Consumer {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("classpath:mq-consumer-context.xml");
    }
}
