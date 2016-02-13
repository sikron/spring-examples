package com.skronawi.spring.examples.rest.auth.service;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public interface RequestAuthenticator {

    void authenticate(MockHttpServletRequestBuilder builder);
}
