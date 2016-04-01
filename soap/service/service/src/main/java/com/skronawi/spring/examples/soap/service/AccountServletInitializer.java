package com.skronawi.spring.examples.soap.service;

import org.springframework.ws.transport.http.support.AbstractAnnotationConfigMessageDispatcherServletInitializer;

//not AbstractAnnotationConfigDispatcherServletInitializer but AbstractAnnotationConfig_Message_DispatcherServletInitializer
public class AccountServletInitializer
        extends AbstractAnnotationConfigMessageDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebServiceConfig.class};
    }

    @Override
    public boolean isTransformWsdlLocations() {
        return true;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/endpoints/*"};
    }
}
