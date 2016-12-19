package com.skronawi.spring.examples.rest.docu.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebConfig.class)
@WebAppConfiguration
public class GreetingsResourceDocumentationMvcTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Greeting preparedGreeting;

    @Before
    public void setUp() throws Exception {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();

        objectMapper = new ObjectMapper();

        Greeting greeting = new Greeting();
        greeting.setName("prepared");
        String greetingJson = objectMapper.writeValueAsString(greeting);
        MvcResult preparedGreetingResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/greetings").header("Authorization", "foo")
                        .content(greetingJson).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        preparedGreeting = objectMapper.readValue(preparedGreetingResult.getResponse().getContentAsString(),
                Greeting.class);
    }

    @Test
    public void testGetSingleGreeting() throws Exception{

        mockMvc.perform(
                /*
                To make the path parameters available for documentation, the request must be built using one of the
                methods on RestDocumentationRequestBuilders rather than MockMvcRequestBuilders !
                 */
                RestDocumentationRequestBuilders.get("/greetings/{id}", preparedGreeting.getId()))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("prepared")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))

                .andDo(MockMvcRestDocumentation.document("{class-name}/{method-name}",

                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),

                        RequestDocumentation.pathParameters(RequestDocumentation.parameterWithName("id")
                                .description("the id of the greeting to get")),

                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("name").description("the name of the one, who has been greeted")
                                        .type(JsonFieldType.STRING),
                                PayloadDocumentation.fieldWithPath("id").description("the id of the greeting")
                                        .type(JsonFieldType.STRING),
                                PayloadDocumentation.fieldWithPath("timestamp").description("when the greeting was performed")
                                        .type(JsonFieldType.STRING))
                        )
                );
    }

    @Test
    public void testGetAllGreetings() throws Exception{

        mockMvc.perform(
                MockMvcRequestBuilders.get("/greetings"))

                .andExpect(MockMvcResultMatchers.status().isOk())

                //tests single greeting objects by index
//                .andExpect(MockMvcResultMatchers.jsonPath("[0].name", CoreMatchers.is("prepared")))
//                .andExpect(MockMvcResultMatchers.jsonPath("[0].id", CoreMatchers.notNullValue()))
//                .andExpect(MockMvcResultMatchers.jsonPath("[0].timestamp", CoreMatchers.notNullValue()))

                //tests all at once, so the matchers will get arrays!!
//                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", CoreMatchers.everyItem(CoreMatchers.is("prepared"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id", CoreMatchers.everyItem(CoreMatchers.notNullValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].timestamp", CoreMatchers.everyItem(CoreMatchers.notNullValue())))

//                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))

                .andDo(MockMvcRestDocumentation.document("{class-name}/{method-name}",

                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),

                        PayloadDocumentation.responseFields(
                                //also document the list itself
                                PayloadDocumentation.fieldWithPath("[]").description("the set of all greetings")
                                        .type(JsonFieldType.ARRAY)
//                                ,PayloadDocumentation.fieldWithPath("[].name").description("the name of the one, who has been greeted")
//                                        .type(JsonFieldType.STRING),
//                                PayloadDocumentation.fieldWithPath("[].id").description("the id of the greeting")
//                                        .type(JsonFieldType.STRING),
//                                PayloadDocumentation.fieldWithPath("[].timestamp").description("when the greeting was performed")
//                                        .type(JsonFieldType.STRING)
                            )
                        )
                );
    }

    @Test
    public void testCreateGreeting() throws Exception {

        Greeting greeting = new Greeting();
        greeting.setName("simon");
        String greetingJson = objectMapper.writeValueAsString(greeting);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/greetings").header("Authorization", "foo")
                        .content(greetingJson).contentType(MediaType.APPLICATION_JSON_UTF8))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("simon")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))

                .andDo(MockMvcRestDocumentation.document("{class-name}/{method-name}",

                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),

                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("name").description("the name of the one, who should be greeted"),
                                PayloadDocumentation.fieldWithPath("id").ignored(),
                                PayloadDocumentation.fieldWithPath("timestamp").ignored()),

                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("name").description("the name of the one, who has been greeted")
                                        .type(JsonFieldType.STRING),
                                PayloadDocumentation.fieldWithPath("id").description("the id of the greeting")
                                        .type(JsonFieldType.STRING),
                                PayloadDocumentation.fieldWithPath("timestamp").description("when the greeting was performed")
                                        .type(JsonFieldType.STRING)),

                        HeaderDocumentation.requestHeaders(HeaderDocumentation.headerWithName("Authorization")
                                .description("the authorization value")),

                        HeaderDocumentation.responseHeaders(HeaderDocumentation.headerWithName("dummy-response-header")
                                .description("a dummy response header"))
                        )
                );
    }

    @Test
    public void testCreateGreetingWithoutNameResultsInError() throws Exception {

        Greeting greeting = new Greeting();
        String greetingJson = objectMapper.writeValueAsString(greeting);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/greetings").header("Authorization", "foo")
                        .content(greetingJson).contentType(MediaType.APPLICATION_JSON_UTF8))

                .andExpect(MockMvcResultMatchers.status().isBadRequest())

                .andDo(MockMvcRestDocumentation.document("{class-name}/{method-name}",

                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),

                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("message").description("the error message")
                                        .type(JsonFieldType.STRING)))
                );
    }
}
