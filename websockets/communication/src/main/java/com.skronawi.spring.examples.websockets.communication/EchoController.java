package com.skronawi.spring.examples.websockets.communication;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class EchoController {

    @MessageMapping("/echo")
    @SendTo("/topic/echos")
    public String post(@RequestBody String message){
        return message;
    }
}
