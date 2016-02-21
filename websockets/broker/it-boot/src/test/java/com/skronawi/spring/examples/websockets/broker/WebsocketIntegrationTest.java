package com.skronawi.spring.examples.websockets.broker;

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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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

        //use latches to proceed step by step

        final CountDownLatch connectedLatch = new CountDownLatch(1);

        connection = stompClient.connect("ws://localhost:" + port + "/hello",

                new StompSessionHandlerAdapter() {
                    @Override
                    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                        System.out.println("connected");
                        connectedLatch.countDown();
                    }

                    @Override
                    public void handleException(StompSession session, StompCommand command, StompHeaders headers,
                                                byte[] payload, Throwable exception) {
                        exception.printStackTrace();
                        Assert.fail();
                    }
                }).get();

        //wait for connection, then go on to sending a greeting
        connectedLatch.await(5, TimeUnit.SECONDS);

        final CountDownLatch responseLatch = new CountDownLatch(1);
        connection.subscribe("/topic/greetings", new StompFrameHandler() {
            public Type getPayloadType(StompHeaders headers) {
                return Greeting.class;
            }

            public void handleFrame(StompHeaders headers, Object payload) {
                Assert.assertEquals(((Greeting) payload).getContent(), "Hello, simon!");
                responseLatch.countDown();
            }
        });
        connection.send("/hello", new HelloMessage("simon"));

        //wait for response
        responseLatch.await(5, TimeUnit.SECONDS);
    }
}