package com.skronawi.spring.examples.rest.preauth.service;

public class Treasure {

    private String content = "diamonds and gold";

    public String get() {
        return content;
    }

    public String empty(){
        String tmp = content;
        content = "empty";
        return tmp;
    }
}
