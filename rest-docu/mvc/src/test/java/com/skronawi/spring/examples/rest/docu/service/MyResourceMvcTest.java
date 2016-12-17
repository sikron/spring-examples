package com.skronawi.spring.examples.rest.docu.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebConfig.class)
@WebAppConfiguration
public class MyResourceMvcTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(MockMvcRestDocumentation.document("{class-name}/{method-name}")).build();
    }

    @Test
    public void testGreetingsWithParam() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/greetings").param("name", "simon"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("hi simon\n"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.TEXT_PLAIN))

                .andDo(MockMvcRestDocumentation.document("greeting-with-param",
                        RequestDocumentation.requestParameters(RequestDocumentation.parameterWithName("name")
                                .description("Greeting's target"))
//                        , PayloadDocumentation.responseFields(PayloadDocumentation.fieldWithPath("id").description("Greeting's generated id"),
//                                PayloadDocumentation.fieldWithPath("content").description("Greeting's content"),
//                                PayloadDocumentation.fieldWithPath("optionalContent").description("Greeting's optional content")
//                                        .type(JsonFieldType.STRING).optional())
                        ));
    }
}
