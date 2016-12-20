package com.skronawi.spring.examples.rest.docu.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/greetings")
public class GreetingsResource {

    private Map<String, Greeting> id2Greeting = new HashMap<>();

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE, headers = {"Authorization"})
    public ResponseEntity<Greeting> createGreeting(@RequestBody Greeting greeting) {

        if (greeting == null) {
            throw new IllegalArgumentException("greeting must not be null");
        }
        if (greeting.getName() == null || greeting.getName().length() == 0) {
            throw new IllegalArgumentException("a greeting must have a valid name");
        }
        greeting.setId(UUID.randomUUID().toString());
        greeting.setTimestamp(new Date());

        id2Greeting.put(greeting.getId(), greeting);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("dummy-response-header", "bar");

        return new ResponseEntity<>(greeting, httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Set<Greeting> getGreetings(@RequestParam(required = false) String name) {

        Stream<Greeting> stream = id2Greeting.values().stream();

        if (name == null || name.length() == 0) {
            return stream.collect(Collectors.toSet());
        }

        return findGreetingsByName(stream, name);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            path = "/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Greeting getGreeting(@PathVariable String id) {

        return id2Greeting.get(id);
    }

    private Set<Greeting> findGreetingsByName(Stream<Greeting> stream, String name) {
        return stream.filter(greeting -> greeting.getName().equalsIgnoreCase(name)).collect(Collectors.toSet());
    }
}
