package com.skronawi.spring.examples.websockets.communication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@SpringApplicationConfiguration(classes = {AppConfig.class, WebSocketConfig.class, Application.class})
@WebIntegrationTest(randomPort = true)
public class WebsocketIntegrationTest extends AbstractTestNGSpringContextTests {

    @Value("${local.server.port}")
    private int port;

    private WebSocketStompClient stompClient;
    private StompSession connection;

    @BeforeClass
    public void setup() throws Exception {

        //http://docs.spring.io/spring/docs/current/spring-framework-reference/html/websocket.html
//        WebSocketClient transport = new StandardWebSocketClient();
//        WebSocketStompClient stompClient = new WebSocketStompClient(transport);
//        stompClient.setMessageConverter(new StringMessageConverter());

        //http://stackoverflow.com/questions/30413380/websocketstompclient-wont-connect-to-sockjs-endpoint
        List<Transport> transports = new ArrayList<Transport>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        WebSocketClient transport = new SockJsClient(transports);
        stompClient = new WebSocketStompClient(transport);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @AfterClass(alwaysRun = true)
    public void teardown() throws Exception {
        if (connection != null) {
            connection.disconnect();
        }
    }

    @Test
    public void test() throws Exception {

        connection = stompClient.connect("ws://localhost:" + port + "/hello",

                new StompSessionHandlerAdapter() {
                    @Override
                    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                        System.out.println("connected");
                    }

                    @Override
                    public void handleException(StompSession session, StompCommand command, StompHeaders headers,
                                                byte[] payload, Throwable exception) {
                        exception.printStackTrace();
                        Assert.fail();
                    }
                }).get();

        connection.subscribe("/topic/greetings", new StompFrameHandler() {
            public Type getPayloadType(StompHeaders headers) {
                return Greeting.class;
            }

            public void handleFrame(StompHeaders headers, Object payload) {
                Assert.assertEquals(((Greeting) payload).getContent(), "Hello, simon!");
            }
        });

        connection.send("/hello", new HelloMessage("simon"));

        //sleep, so that the tomcat has a chance to accept and return. otherwise it would be teared down, as the test is over
        Thread.sleep(5000);
    }
}