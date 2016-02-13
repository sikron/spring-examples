package com.skronawi.spring.examples.rest.auth.service;

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

    //every authenticated user can get the treasure (see the WebSecurityConfig). "GET" does not affect the treasure-state!
    @RequestMapping(path = "/treasure", method = RequestMethod.GET)
    public ResponseEntity<String> getTheTreasure() {
        return new ResponseEntity<String>(treasure.get(), HttpStatus.OK);
    }

    //only a admin has the right to claim the treasure. afterwards it is "empty"
    @RequestMapping(path = "/treasure", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") //use this IF NOT specified already in the WebSecurityConfig
    public ResponseEntity<String> claimTheTreasure() {
        return new ResponseEntity<String>(treasure.empty(), HttpStatus.OK);
    }

    //anyone can get the catgold, even un-authenticated users (see the WebSecurityConfig)
    @RequestMapping(path = "/catgold", method = RequestMethod.GET)
    public ResponseEntity<String> getTheCatgold() {
        return new ResponseEntity<String>("cheap plunder", HttpStatus.OK);
    }
}
