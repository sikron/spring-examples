package com.skronawi.spring.examples.rest.preauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebMvc
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages = "com.skronawi.spring.examples.rest.preauth.service")
public class Config extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                //so that no session is created and every request is verified anew
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                //now set up the pre-authentication filter
                .addFilterBefore(sectAuthenticationFilter(), RequestHeaderAuthenticationFilter.class)
                //the following filter must catch exceptions coming from the sectAuthenticationFilter.
                //they are NOT handled by the REST exception mappers
                .addFilterBefore(sectAuthenticationFilterExceptionHandling(), SectAuthenticationFilter.class)
                .authenticationProvider(preauthAuthProvider())

                .csrf().disable()
        ;
    }

    private OncePerRequestFilter sectAuthenticationFilterExceptionHandling() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                            HttpServletResponse httpServletResponse, FilterChain filterChain)
                    throws ServletException, IOException {
                try {
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
                } catch (Exception e) {
                    httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
                }
            }
        };
    }

    private SectAuthenticationFilter sectAuthenticationFilter() throws Exception {

        SectAuthenticationFilter filter = new SectAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setPrincipalRequestHeader("Authorization");
        filter.setExceptionIfHeaderMissing(false);
        return filter;
    }

    private UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> userDetailsServiceWrapper() {

        UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> wrapper =
                new UserDetailsByNameServiceWrapper<>();
        wrapper.setUserDetailsService(new SectTokenUserDetailsService());
        return wrapper;
    }

    private PreAuthenticatedAuthenticationProvider preauthAuthProvider() {

        PreAuthenticatedAuthenticationProvider preauthAuthProvider =
                new PreAuthenticatedAuthenticationProvider();
        preauthAuthProvider.setPreAuthenticatedUserDetailsService(userDetailsServiceWrapper());
        return preauthAuthProvider;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(preauthAuthProvider());
    }

    @Bean
    public Treasure treasure() {
        return new Treasure();
    }
}
