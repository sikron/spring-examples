package com.skronawi.spring.examples.rest.preauth.service;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

public class SectTokenUserDetailsService implements UserDetailsService {

    //the username is actually the token
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //FIXME this part should have been done in the SectAuthenticationFilter, but exceptions there cannot be handled in REST
        if (username == null || username.equals("")) {
            throw new AuthenticationCredentialsNotFoundException("no authorization value");
        }
        if (!username.startsWith("sect ")) {
            throw new AuthenticationCredentialsNotFoundException("authorization value has wrong format");
        }
        username = username.replace("sect ", "");

        if (!username.equals("abcd1234")) {
            throw new UsernameNotFoundException("user not found: " + username);
        }

        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        return new User("donald", "password-dummy", authorities);
    }
}
