package com.skronawi.spring.examples.rest.docu.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/greetings")
public class GreetingsResource {

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            headers = {"Authorization"})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Greeting> getGreeting(@RequestParam(defaultValue = "stranger") String name) {

        Greeting greeting = new Greeting();
        greeting.setName(name);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("dummy-response-header", "bar");

        return new ResponseEntity<>(greeting, httpHeaders, HttpStatus.OK);
    }

}
