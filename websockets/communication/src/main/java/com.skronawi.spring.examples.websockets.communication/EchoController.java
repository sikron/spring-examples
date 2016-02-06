package com.skronawi.spring.examples.websockets.communication;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class EchoController {

    @MessageMapping("/echo")  //senders have to send to the app endpoint "echo", i.e. /app/echo as configured in SocketServiceConfig
    @SendTo("/topic/echos") //message is relayed to the broker queue /topic/echos
    public String post(@RequestBody String message){
        return "echo: " + message;
    }
}
