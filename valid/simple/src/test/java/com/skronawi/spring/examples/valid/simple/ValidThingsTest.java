package com.skronawi.spring.examples.valid.simple;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@WebAppConfiguration
@ContextConfiguration(classes = TestConfig.class)
public class ValidThingsTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeClass
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    protected Thing createAndAssertResponse(Thing thing) throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/")
                        .content(objectMapper.writeValueAsString(thing))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        String contentAsString = response.getContentAsString();
        return objectMapper.readValue(contentAsString, Thing.class);
    }

    @Test
    public void create() throws Exception {
        Thing thing = createAndAssertResponse(ThingModels.empty());
        Assert.assertNotNull(thing.getId());
    }
}
