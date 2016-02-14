package com.skronawi.spring.examples.rest.oauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {
        endpoints.authenticationManager(this.authenticationManager);
    }

    /*
    .authorizedGrantTypes("password")
    webapp takes the user's credentials and exchanges them for a token at the auth server.

    .authorizedGrantTypes("authorization")
    webapp redirects to auth server. user enters credentials. webapp gets a auth_code. webapp uses this auth_code to
    get a token from the auth server.
    ---> user interaction necessary

    .authorizedGrantTypes("implicit")
    webapp redirects to auth server. user enters credentials. webapp gets the token directly.
    ---> user interaction necessary
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient("aClient")
                .secret("client_secret")
                .authorizedGrantTypes("password")
                .scopes("read", "write")
                .resourceIds("rest-oauth_resources");
    }
}