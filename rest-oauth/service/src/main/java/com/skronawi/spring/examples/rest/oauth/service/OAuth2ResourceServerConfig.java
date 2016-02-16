package com.skronawi.spring.examples.rest.oauth.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId("rest-oauth_resources");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers().antMatchers("/it")
                .and()
                .authorizeRequests().antMatchers("/it").authenticated()
                .and()
                .authorizeRequests()
                .antMatchers("/it").access("#oauth2.hasScope('read')");
                //".hasScope('blub')" -> donald cannot access it as the aClient does not have the 'blub' scope
    }
}