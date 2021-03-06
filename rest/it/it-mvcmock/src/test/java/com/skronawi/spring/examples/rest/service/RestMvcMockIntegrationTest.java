package com.skronawi.spring.examples.rest.service;

import com.skronawi.spring.examples.rest.communication.Error;
import com.skronawi.spring.examples.rest.communication.MyPageable;
import com.skronawi.spring.examples.rest.communication.RestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@ContextConfiguration(classes = RestService.class)
public class RestMvcMockIntegrationTest extends AbstractRestTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeClass
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        cleanup();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() throws Exception {
        Set<RestData> restDatas = getAllAndAssertResponse();
        for (RestData restData : restDatas) {
            deleteAndAssertResponse(restData.getId());
        }
    }

    @Override
    protected RestData createAndAssertResponse(RestData restData) throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/data")
                        .content(objectMapper.writeValueAsString(restData))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        RestData createdRestData = objectMapper.readValue(response.getContentAsString(), RestData.class);

        resultActions
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(header().string("location", "http://localhost/data/" + createdRestData.getId()))  //error: cannot access Matcher  ??
        ;
        return createdRestData;
    }

    @Override
    protected Set<RestData> getAllAndAssertResponse() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/data"));
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        return objectMapper.readValue(response.getContentAsString(), restDataSetType);
    }

    @Override
    protected Set<RestData> getAllAndAssertResponse(MyPageable myPageable) throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/data")
                .param("offset", String.valueOf(myPageable.getOffset()))
                .param("limit", String.valueOf(myPageable.getLimit())));
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        return objectMapper.readValue(response.getContentAsString(), restDataSetType);
    }

    @Override
    protected RestData getAndAssertResponse(String id) throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/data/" + id));
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        return objectMapper.readValue(response.getContentAsString(), RestData.class);
    }

    @Override
    protected RestData updateAndAssertResponse(RestData restData) throws Exception {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    protected void deleteAndAssertResponse(String id) throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/data/" + id));
        resultActions.andExpect(status().isOk());
    }

    @Override
    protected void getAndNotFound(String id) throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/data/" + id));
        resultActions.andExpect(status().isNotFound());//.andExpect(content().string("Data not found by id " + id)); //error: cannot access Matcher  ??
        Error error = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), Error.class);
        Assert.assertEquals(error.getMessage(), "Data not found by id " + id);
    }
}
