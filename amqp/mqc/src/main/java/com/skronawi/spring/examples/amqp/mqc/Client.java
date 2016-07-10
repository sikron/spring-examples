package com.skronawi.spring.examples.amqp.mqc;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Client {

    private AnnotationConfigApplicationContext context;

    public void init() {
        context = new AnnotationConfigApplicationContext(Config.class);
    }

    public void teardown() {
        if (context != null) {
            context.close();
        }
    }

    public Producer producer() {
        return context.getBean(Producer.class);
    }

    public Consumer consumer(){
        return context.getBean(Consumer.class);
    }
}
