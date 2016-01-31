package com.skronawi.spring.examples.rest.communication;

public class DataNotFoundException extends RuntimeException {

    private final String id;

    public DataNotFoundException(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
