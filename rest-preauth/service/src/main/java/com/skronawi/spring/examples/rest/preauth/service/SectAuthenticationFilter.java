package com.skronawi.spring.examples.rest.preauth.service;

import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;

/*
see
- http://www.learningthegoodstuff.com/2014/12/spring-security-pre-authentication-and.html
- http://howtodoinjava.com/spring/spring-security/spring-3-security-siteminder-pre-authentication-example/
 */
public class SectAuthenticationFilter extends RequestHeaderAuthenticationFilter {

    public SectAuthenticationFilter() {
        super();
        this.setPrincipalRequestHeader("Authorization");
        this.setExceptionIfHeaderMissing(false);
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {

        //FIXME exceptions here are not handled in the RestExceptionHandler, result will be a 500!!!!

        String authorizationHeaderValue = (String) (super.getPreAuthenticatedPrincipal(request));

        //FIXME put this part into the SectTokenUserDetailsService, AuthenticationExceptions there are handled

//        if (authorizationHeaderValue == null || authorizationHeaderValue.equals("")) {
//            throw new IllegalArgumentException("no authorization value");
//        }
//
//        if (!authorizationHeaderValue.startsWith("sect ")) {
//            throw new IllegalArgumentException("authorization value has wrong format");
//        }
//
//        authorizationHeaderValue = authorizationHeaderValue.replace("sect ", "");
//        return authorizationHeaderValue;

        return authorizationHeaderValue;
    }
}