package com.skronawi.spring.examples.rest.auth.service;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public class DontAuthenticateAuthenticator implements RequestAuthenticator {

    public void authenticate(MockHttpServletRequestBuilder builder) {
        //nothing to do
    }
}
