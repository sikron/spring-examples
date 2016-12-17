package com.skronawi.spring.examples.rest.docu.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/greetings")
public class MyResource {

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String getTheTreasure(@RequestParam(defaultValue = "stranger") String name) {
        return "hi " + name + "\n";
    }

}
