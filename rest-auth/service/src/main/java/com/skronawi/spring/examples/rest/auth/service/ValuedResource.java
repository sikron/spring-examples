package com.skronawi.spring.examples.rest.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ValuedResource {

    @RequestMapping(path = "/treasure")
    public ResponseEntity<String> getTheTreasure() {
        return new ResponseEntity<String>("diamonds and gold", HttpStatus.OK);
    }
}
