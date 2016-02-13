package com.skronawi.spring.examples.rest.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //for enabling fine-granulare access-control on methods, e.g. in resources. see http://docs.spring.io/spring-security/site/docs/current/reference/html/jc.html#enableglobalmethodsecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    //several configure(AuthenticationManagerBuilder auth) -------------------------------------------------------------

    //use a backend backed UserDetailsService to handle authentication
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    //very basic user information
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("donald").password("d8ck").roles("USER", "ADMIN");
//    }

    //several configure(HttpSecurity http) -----------------------------------------------------------------------------

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //for REST, no cookies, only tokens
                .and()
                    .authorizeRequests().antMatchers("/catgold").permitAll() //
                .and()
                    .authorizeRequests().antMatchers("/treasure").authenticated() //more fine-grained control via method-annotations
                .and()
                    .httpBasic()
                        .realmName("rest-auth")
                .and()
                    .csrf()
                        .disable()
        ;
    }

    //here the "DELETE /treasure" is configured to be allowed for admins, not via the @PreAuthorize on the method.
    //as the @PreAuthorize is not honored in the MockMVC tests
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .sessionManagement()
//                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //for REST, no cookies, only tokens
//                .and()
//                    .authorizeRequests().antMatchers("/catgold").permitAll() //
//                .and()
//                    //order is important! stricter authorizeRequests() must come first!
//                    .authorizeRequests().antMatchers(HttpMethod.DELETE, "/treasure").hasRole("ADMIN") //if so, then don't use @PreAuthorize on the resource method
//                .and()
//                    .authorizeRequests().antMatchers("/treasure").authenticated()
//                .and()
//                    .httpBasic()
//                        .realmName("rest-auth")
//                .and()
//                    .csrf()
//                        .disable()
//        ;
//    }


    //default impl of configure(HttpSecurity http) - just FYI
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests().anyRequest().authenticated()
//                .and().formLogin()
//                .and().httpBasic();
//    }

    //other stuff ------------------------------------------------------------------------------------------------------

    //needed somehow, see http://stackoverflow.com/questions/24865588/how-to-enable-secured-annotations-with-java-based-configuration/27218203#27218203
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
