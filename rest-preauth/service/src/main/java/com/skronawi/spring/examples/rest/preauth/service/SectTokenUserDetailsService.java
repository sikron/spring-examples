package com.skronawi.spring.examples.rest.preauth.service;

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

        if (!username.equals("abcd1234")) {
            throw new UsernameNotFoundException("user not found: " + username);
        }

        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        return new User("donald", "password-dummy", authorities);
    }
}
