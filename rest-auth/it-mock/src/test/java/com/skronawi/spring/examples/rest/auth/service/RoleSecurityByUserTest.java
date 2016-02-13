package com.skronawi.spring.examples.rest.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
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

/*
just a different testing approach than RoleSecurityByAuthTest. the same logic is tested though.
 */
@WebAppConfiguration
@ContextConfiguration(classes = {WebConfig.class, WebSecurityConfig.class, AppConfig.class})
public class RoleSecurityByUserTest extends AbstractTestNGSpringContextTests {

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

    @DataProvider
    public static Object[][] claimTreasureAccesses() {
        return new Object[][]{
                new Object[]{SecurityMockMvcRequestPostProcessors.user("dum").password("my"),
                        MockMvcResultMatchers.status().isForbidden()}, //a unauthorized user is not possible here, so no isUnauthorized()!
                new Object[]{SecurityMockMvcRequestPostProcessors.user("daisy").password("d8ck").roles("USER"),
                        MockMvcResultMatchers.status().isForbidden()}, //403
                new Object[]{SecurityMockMvcRequestPostProcessors.user("donald").password("d8ck").roles("USER", "ADMIN"),
                        MockMvcResultMatchers.status().isOk()},
        };
    }

    @Test(dataProvider = "claimTreasureAccesses")
    public void onlyAdminUsersCanClaimTheTreasure(SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user,
                                                  ResultMatcher status)
            throws Exception {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/treasure");
        requestBuilder.with(user);

        ResultActions resultActions = mockMvc.perform(requestBuilder);
        resultActions.andExpect(status);

        if (status.equals(MockMvcResultMatchers.status().isOk())) {
            resultActions.andExpect(MockMvcResultMatchers.content().bytes("diamonds and gold".getBytes()));
        }
    }
}
