package com.skronawi.spring.examples.rest.preauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class ValueableResource {

    @Autowired
    private Treasure treasure;

    @RequestMapping(path = "/treasure", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String getTheTreasure() {
        return treasure.get();
    }
}
