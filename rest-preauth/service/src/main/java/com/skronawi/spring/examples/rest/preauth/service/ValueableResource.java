package com.skronawi.spring.examples.rest.preauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ValueableResource {

    @Autowired
    private Treasure treasure;

    @RequestMapping(path = "/treasure", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> getTheTreasure() {
        return new ResponseEntity<>(treasure.get(), HttpStatus.OK);
    }
}
