package com.skronawi.spring.examples.rest.oauth.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ValuableResource {

    @RequestMapping(path = "/it", method = RequestMethod.GET)
    public ResponseEntity<String> getIt() {
        return new ResponseEntity<String>("here you go", HttpStatus.OK);
    }
}
