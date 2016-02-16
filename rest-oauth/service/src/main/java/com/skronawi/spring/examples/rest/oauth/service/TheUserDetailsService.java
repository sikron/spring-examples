package com.skronawi.spring.examples.rest.oauth.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TheUserDetailsService implements UserDetailsService {

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //only donald and daisy
        if (!"donald".equals(username)) {
            throw new UsernameNotFoundException("user not found by username: " + username);
        }

        //donald gets the "ADMIN" additionally
        ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        //in the backend, the passwords are stored encrypted, so fake this by encrypting the plain password
        return new User(username, "d8ck", authorities);
    }
}
