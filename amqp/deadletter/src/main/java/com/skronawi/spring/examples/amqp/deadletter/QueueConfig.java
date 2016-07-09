package com.skronawi.spring.examples.amqp.deadletter;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ConnectionConfig.class)
public class QueueConfig {

    @Bean(name = "workingQueue")
    public Queue workingQueue() {
        return QueueBuilder.durable("working_queue")
                
                /*
                the producer sends 1 message every second. so the result will be, that in working_queue
                there will always be the last 10 messages, and the deadletter-queue all older messages
                will be gathered by rabbitMq itself
                 */
                .withArgument("x-dead-letter-exchange", "deadletter_exchange")

                //also, because it is direct and not fanout,
                //see http://stackoverflow.com/questions/28286334/spring-amqp-nothing-showing-up-in-dead-letter-queue
                .withArgument("x-dead-letter-routing-key", "deadletter_queue")

                .withArgument("x-message-ttl", 10 * 1000)
                .build();
    }

    @Bean(name = "workingExchange")
    public DirectExchange workingExchange() {
        return new DirectExchange("working_exchange");
    }

    @Bean(name = "deadletterQueue")
    public Queue deadletterQueue() {
        return new Queue("deadletter_queue");
    }

    @Bean(name = "deadletterExchange")
    public DirectExchange deadletterExchange() {
        return new DirectExchange("deadletter_exchange");
    }

    @Bean(name = "workingBinding")
    public Binding workingBinding(Queue workingQueue, DirectExchange workingExchange) {
        return BindingBuilder.bind(workingQueue).to(workingExchange).with("working_queue"); //TODO why can't i use a different key here?
    }

    @Bean(name = "deadletterBinding")
    public Binding deadletterBinding(Queue deadletterQueue, DirectExchange deadletterExchange) {
        return BindingBuilder.bind(deadletterQueue).to(deadletterExchange).with("deadletter_queue");
    }
}
