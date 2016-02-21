package com.skronawi.spring.examples.websockets.sockjs;

import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(echoSocketHandler(), "/echo").withSockJS();
    }

    @Bean
    public EchoSocketHandler echoSocketHandler() {
        return new EchoSocketHandler();
    }
}