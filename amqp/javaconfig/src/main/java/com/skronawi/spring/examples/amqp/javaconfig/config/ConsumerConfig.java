package com.skronawi.spring.examples.amqp.javaconfig.config;

import com.skronawi.spring.examples.amqp.javaconfig.MessageHandler;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(Config.class)
public class ConsumerConfig {

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("message_queue");
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public MessageHandler messageHandler() {
        return new MessageHandler();
    }

    @Bean
    MessageListenerAdapter listenerAdapter(MessageHandler messageHandler) {
        return new MessageListenerAdapter(messageHandler, "handleMessage");
    }
}
