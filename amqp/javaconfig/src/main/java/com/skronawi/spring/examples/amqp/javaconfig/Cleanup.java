package com.skronawi.spring.examples.amqp.javaconfig;

import com.skronawi.spring.examples.amqp.javaconfig.config.Config;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Cleanup {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Config.class);
        AmqpAdmin admin = context.getBean(AmqpAdmin.class);
        admin.deleteExchange("message_exchange");
        admin.deleteQueue("message_queue");
        context.close();
    }
}
