package com.skronawi.spring.examples.rest.oauth.service;

import org.springframework.security.crypto.codec.Base64;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public class BasicAuthenticator {

    private final String username;
    private final String password;

    public BasicAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void authenticate(MockHttpServletRequestBuilder builder) {
        String authValue = basicAuthHeader(username, password);
        builder.header("authorization", authValue);
    }

    private String basicAuthHeader(String username, String password) {
        return "Basic " + new String(Base64.encode((username + ":" + password).getBytes()));//capital "B" is needed
    }
}
