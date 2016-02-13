package com.skronawi.spring.examples.rest.auth.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

public class TheUserDetailsService implements UserDetailsService {

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //only donald and daisy
        if (!"donald".equals(username) && !"daisy".equals(username)) {
            throw new UsernameNotFoundException("user not found by username: " + username);
        }

        //donald gets the "ADMIN" additionally
        ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if ("donald".equals(username)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        //in the backend, the passwords are stored encrypted, so fake this by encrypting the plain password
        return new User(username, new BCryptPasswordEncoder().encode("d8ck"), authorities);
    }
}
