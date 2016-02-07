package com.skronawi.spring.examples.websockets.communication;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class HomeController {

    @MessageMapping("/hello") //the incoming endpoint
    @SendTo("/topic/greetings") //the outgoing message queue topic
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(500); // simulated delay
        return new Greeting("Hello, " + message.getName() + "!");
    }
}
