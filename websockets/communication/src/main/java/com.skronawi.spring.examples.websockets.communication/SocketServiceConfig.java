package com.skronawi.spring.examples.websockets.communication;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
@ComponentScan(basePackages = "com.skronawi.spring.examples.websockets")
public class SocketServiceConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); //base endpoint for broker
        config.setApplicationDestinationPrefixes("/app"); //base endpoint for messages to the app
    }

    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/echo").withSockJS(); //the /app/echo endpoint
    }
}
