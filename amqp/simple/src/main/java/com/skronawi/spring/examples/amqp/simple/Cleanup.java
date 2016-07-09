package com.skronawi.spring.examples.amqp.simple;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Cleanup {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext classPathXmlApplicationContext =
                new ClassPathXmlApplicationContext("classpath:mq-context.xml");
        AmqpAdmin admin = classPathXmlApplicationContext.getBean(AmqpAdmin.class);
        admin.deleteExchange("message_exchange");
        admin.deleteQueue("message_queue");
        classPathXmlApplicationContext.close();
    }
}
