package com.skronawi.spring.examples.amqp.deadletter;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Cleanup {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ConnectionConfig.class);
        AmqpAdmin admin = context.getBean(AmqpAdmin.class);
        admin.deleteExchange("working_exchange");
        admin.deleteQueue("working_queue");
        admin.deleteExchange("deadletter_exchange");
        admin.deleteQueue("deadletter_queue");
        context.close();
    }
}
