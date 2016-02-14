package com.skronawi.spring.examples.rest.oauth.service;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@ContextConfiguration(classes = {WebConfig.class, WebSecurityConfig.class,
        OAuth2AuthorizationServerConfig.class, OAuth2ResourceServerConfig.class})
public class AuthenticationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeClass
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void itIsSecure() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/it")).andExpect(status().isUnauthorized());
    }

    @Test
    public void authenticateAndGetIt_password() throws Exception {

        MockHttpServletRequestBuilder tokenRequest = MockMvcRequestBuilders
                .post("/oauth/token")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                //form-data
                .param("username", "donald")
                .param("password", "d8ck")
                .param("grant_type", "password");
        new BasicAuthenticator("aClient", "client_secret").authenticate(tokenRequest);

        String responseAsString = mockMvc.perform(tokenRequest).andReturn().getResponse().getContentAsString();

        if (responseAsString.contains("error")) {
            Assert.fail("the response indicates an error: " + responseAsString);
        }

        DefaultOAuth2AccessToken oAuth2AccessToken = new ObjectMapper().readValue(responseAsString,
                DefaultOAuth2AccessToken.class);
        String token = oAuth2AccessToken.getValue();

        MockHttpServletRequestBuilder itRequest = MockMvcRequestBuilders.get("/it");
        itRequest.header("authorization", "Bearer " + token);
        mockMvc.perform(itRequest)
                .andExpect(status().isOk())
                .andExpect(content().bytes("here you go".getBytes()));
    }
}
