package com.skronawi.spring.examples.amqp.javaconfig;

import com.skronawi.spring.examples.amqp.javaconfig.config.ConsumerConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Only exists to load the context.xml and start consuming.
 */
public class Consumer {

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(ConsumerConfig.class);
    }
}
