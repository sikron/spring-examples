package com.skronawi.spring.examples.rest.preauth.service;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@WebAppConfiguration
@ContextConfiguration(classes = Config.class)
public class PreAuthenticationTest extends AbstractTestNGSpringContextTests {

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

    //this 403 is due to a IllegalArgumentException from the SectAuthenticationFilter, catched in a earlier filter
    @Test
    public void testNoAuthHeader() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/treasure");

        ResultActions resultActions = mockMvc.perform(requestBuilder);
        resultActions.andExpect(MockMvcResultMatchers.status().isForbidden()); //FIXME should that not be 401 ?
    }

    //this 403 is due to a IllegalArgumentException from the SectAuthenticationFilter, catched in a earlier filter
    @Test
    public void testNoSectAuthHeader() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/treasure");
        requestBuilder.header("Authorization", "abcd1234");

        ResultActions resultActions = mockMvc.perform(requestBuilder);
        resultActions.andExpect(MockMvcResultMatchers.status().isForbidden()); //FIXME should that not be 401 ?
    }

    //this 403 is due to a UsernameNotFoundException from the SectTokenUserDetailsService
    @Test
    public void testWrongAuthHeader() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/treasure");
        requestBuilder.header("Authorization", "sect abcd1234_wrong");

        ResultActions resultActions = mockMvc.perform(requestBuilder);
        resultActions.andExpect(MockMvcResultMatchers.status().isForbidden()); //FIXME should that not be 401 ?
    }

    @Test
    public void testAuthHeader() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/treasure");
        requestBuilder.header("Authorization", "sect abcd1234");

        ResultActions resultActions = mockMvc.perform(requestBuilder);
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
