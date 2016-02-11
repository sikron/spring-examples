package com.skronawi.spring.examples.rest.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvaluedResource {

    @RequestMapping(path = "/catgold", method = RequestMethod.GET)
    public ResponseEntity<String> getTheTreasure() {
        return new ResponseEntity<String>("cheap plunder", HttpStatus.OK);
    }
}
