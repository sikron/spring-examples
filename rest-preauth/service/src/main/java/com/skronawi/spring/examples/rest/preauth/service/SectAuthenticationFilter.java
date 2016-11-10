package com.skronawi.spring.examples.rest.preauth.service;

import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;

/*
see
- http://www.learningthegoodstuff.com/2014/12/spring-security-pre-authentication-and.html
- http://howtodoinjava.com/spring/spring-security/spring-3-security-siteminder-pre-authentication-example/
 */
public class SectAuthenticationFilter extends RequestHeaderAuthenticationFilter {

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {

        String authorizationHeaderValue = (String) (super.getPreAuthenticatedPrincipal(request));

        if (authorizationHeaderValue == null || authorizationHeaderValue.equals("")) {
            throw new IllegalArgumentException("no authorization value");
        }

        if (!authorizationHeaderValue.startsWith("sect ")) {
            throw new IllegalArgumentException("authorization value has wrong format");
        }

        authorizationHeaderValue = authorizationHeaderValue.replace("sect ", "");
        return authorizationHeaderValue;
    }
}