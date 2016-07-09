package com.skronawi.spring.examples.amqp.simple;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Only exists to load the context.xml and start producing.
 */
public class Producer {

    private static final AtomicInteger count = new AtomicInteger();

    //after starting the context, this class will be proxied by the scheduler. then inject the context for later use
    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) throws InterruptedException {
        new ClassPathXmlApplicationContext("classpath:mq-producer-context.xml");
    }

    @Scheduled(fixedRate = 1000)
    public void sendMessage() {
        System.out.println("Sending message #" + count.incrementAndGet());
        AmqpTemplate amqpTemplate = applicationContext.getBean(AmqpTemplate.class);
        amqpTemplate.convertAndSend(new Message(String.valueOf(count.get()), "this is message nr. " + count.get()));
    }
}
