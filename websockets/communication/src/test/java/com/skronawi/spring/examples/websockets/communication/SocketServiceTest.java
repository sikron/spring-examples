package com.skronawi.spring.examples.websockets.communication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;

@ContextConfiguration(classes = SocketServiceConfig.class)
public class SocketServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeClass
    public void initTests() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @org.testng.annotations.Test

}
