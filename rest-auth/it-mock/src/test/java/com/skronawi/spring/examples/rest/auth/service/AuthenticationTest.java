package com.skronawi.spring.examples.rest.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@WebAppConfiguration
@ContextConfiguration(classes = {WebConfig.class, WebSecurityConfig.class, AppConfig.class})
public class AuthenticationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeClass
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity()) //so that the springSecurityFilterChain is active, see http://docs.spring.io/spring-security/site/docs/4.0.x/reference/htmlsingle/#test-mockmvc
                .build();
    }

    @DataProvider
    public static Object[][] cheapPlunderAccesses() {
        return new Object[][]{
                new Object[]{new DontAuthenticateAuthenticator(), MockMvcResultMatchers.status().isOk()},
                new Object[]{new BasicAuthenticator("daisy", "d8ck"), MockMvcResultMatchers.status().isOk()},
                new Object[]{new BasicAuthenticator("donald", "d8ck"), MockMvcResultMatchers.status().isOk()},
        };
    }

    @Test(dataProvider = "cheapPlunderAccesses")
    public void everyoneCanAccessTheCatgold(RequestAuthenticator authenticator, ResultMatcher status) throws Exception {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/catgold");
        authenticator.authenticate(requestBuilder);

        mockMvc.perform(requestBuilder)
                .andExpect(status)
//                .andExpect(MockMvcResultMatchers.content().string("cheap plunder")) //"matcher not found" on compiling
                .andExpect(MockMvcResultMatchers.content().bytes("cheap plunder".getBytes()))
        ;
    }

    @DataProvider
    public static Object[][] getTreasureAccesses() {
        return new Object[][]{
                new Object[]{new DontAuthenticateAuthenticator(), MockMvcResultMatchers.status().isUnauthorized()}, //401
                new Object[]{new BasicAuthenticator("daisy", "d8ck"), MockMvcResultMatchers.status().isOk()},
                new Object[]{new BasicAuthenticator("donald", "d8ck"), MockMvcResultMatchers.status().isOk()},
        };
    }

    @Test(dataProvider = "getTreasureAccesses")
    public void onlyAuthenticatedUsersCanGetTheTreasure(RequestAuthenticator authenticator, ResultMatcher status)
            throws Exception {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/treasure");
        authenticator.authenticate(requestBuilder);

        ResultActions resultActions = mockMvc.perform(requestBuilder);
        resultActions.andExpect(status);

        if (status.equals(MockMvcResultMatchers.status().isOk())) {
            resultActions.andExpect(MockMvcResultMatchers.content().bytes("diamonds and gold".getBytes()));
        }
    }
}
