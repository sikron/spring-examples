package com.skronawi.spring.examples.websockets.communication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@ContextConfiguration(classes = SocketServiceConfig.class)
public class SocketServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeClass
    public void initTests() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void sendAndReceive() throws Exception{

//        StandardWebSocketClient simpleWebSocketClient = new StandardWebSocketClient();
//        List<Transport> transports = new ArrayList<>(1);
//        transports.add(new WebSocketTransport(simpleWebSocketClient));
//
//        SockJsClient sockJsClient = new SockJsClient(transports);
//        sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());
//
//        StompMessageHandler messageHandler = new StompMessageHandler();
//        StompWebSocketHandler websocketHandler = new StompWebSocketHandler(messageHandler, new StringMessageConverter());
//
//        WebSocketConnectionManager manager = new WebSocketConnectionManager(sockJsClient, websocketHandler, "ws://localhost:8080/stomp");
//
//        manager.start();
    }
}
