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
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

/**
 * this class is intended for actually generating a complete docu of the {@link GreetingsResource}. the other "sandbox"
 * test is intended for testing some single/particular things.
 *
 * see http://docs.spring.io/spring-restdocs/docs/1.1.2.RELEASE/reference/html5/ for more info
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebConfig.class)
@WebAppConfiguration
public class GreetingsResourceDocumentationMvcTest {

    //field descriptors are re-usable, especially also within a array
    private static final List<FieldDescriptor> GREETING_FIELD_DESCRIPTORS = Arrays.asList(
            PayloadDocumentation.fieldWithPath("name").description("the name of the one, who has been greeted")
                    .type(JsonFieldType.STRING),
            PayloadDocumentation.fieldWithPath("id").description("the id of the greeting")
                    .type(JsonFieldType.STRING),
            PayloadDocumentation.fieldWithPath("timestamp").description("when the greeting was performed")
                    .type(JsonFieldType.STRING)
    );

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Greeting preparedGreeting;
    private RestDocumentationResultHandler document;

    @Before
    public void setUp() throws Exception {

        this.document = MockMvcRestDocumentation.document("{class-name}/{method-name}",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()));

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(this.restDocumentation))
                .alwaysDo(document)
                .build();

        objectMapper = new ObjectMapper();

        Greeting greeting = new Greeting();
        greeting.setName("prepared");
        String greetingJson = objectMapper.writeValueAsString(greeting);
        MvcResult preparedGreetingResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/greetings").header("Authorization", "foo")
                        .content(greetingJson).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
        preparedGreeting = objectMapper.readValue(preparedGreetingResult.getResponse().getContentAsString(),
                Greeting.class);
    }

    @Test
    public void testCreateGreeting() throws Exception {

        Greeting greeting = new Greeting();
        greeting.setName("simon");
        String greetingJson = objectMapper.writeValueAsString(greeting);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/greetings").header("Authorization", "foo")
                        .content(greetingJson).contentType(MediaType.APPLICATION_JSON_UTF8))

                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("simon")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))

                .andDo(document.document(

                        PayloadDocumentation.requestFields(

                                //customize the table a little, see also rest-docu/mvc/src/test/resources/org/springframework/restdocs/templates/asciidoctor/request-fields.snippet
                                Attributes.attributes(Attributes.key("title").value("Fields for creating a greeting")),
                                PayloadDocumentation.fieldWithPath("name").description("the name of the one, who should be greeted")
                                        //add a extra columns with constraints
                                        .attributes(Attributes.key("constraints").value("must not be null or empty")),

                                PayloadDocumentation.fieldWithPath("id").ignored(),
                                PayloadDocumentation.fieldWithPath("timestamp").ignored()),

                        PayloadDocumentation.responseFields(GREETING_FIELD_DESCRIPTORS),

                        HeaderDocumentation.requestHeaders(HeaderDocumentation.headerWithName("Authorization")
                                .description("the authorization value")),

                        HeaderDocumentation.responseHeaders(HeaderDocumentation.headerWithName("dummy-response-header")
                                .description("a dummy response header"))
                        )
                );
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

                .andDo(document.document(

                        RequestDocumentation.pathParameters(RequestDocumentation.parameterWithName("id")
                                .description("the id of the greeting to get")),

                        PayloadDocumentation.responseFields(GREETING_FIELD_DESCRIPTORS))
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
                //XOR
                //tests all at once, so the matchers will get arrays!!
//                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", CoreMatchers.everyItem(CoreMatchers.is("prepared"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id", CoreMatchers.everyItem(CoreMatchers.notNullValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].timestamp", CoreMatchers.everyItem(CoreMatchers.notNullValue())))

//                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))

                .andDo(document.document(

                        PayloadDocumentation.responseFields(
                                //also document the list itself
                                PayloadDocumentation.fieldWithPath("[]").description("the set of all greetings")
                                        .type(JsonFieldType.ARRAY))
                                .andWithPrefix("[].", GREETING_FIELD_DESCRIPTORS))
                );
    }

    @Test
    public void testFindGreetingsByName() throws Exception{

        Greeting greeting = new Greeting();
        greeting.setName("ina");
        String greetingJson = objectMapper.writeValueAsString(greeting);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/greetings").header("Authorization", "foo")
                        .content(greetingJson).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/greetings").param("name", "ina"))

                .andExpect(MockMvcResultMatchers.status().isOk())

                //tests all at once, so the matchers will get arrays!!
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", CoreMatchers.everyItem(CoreMatchers.is("ina"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id", CoreMatchers.everyItem(CoreMatchers.notNullValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].timestamp", CoreMatchers.everyItem(CoreMatchers.notNullValue())))

                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))

                .andDo(document.document(

                        RequestDocumentation.requestParameters(RequestDocumentation.parameterWithName("name")
                                .description("the name to search greetings for")),

                        PayloadDocumentation.responseFields(
                                //also document the list itself
                                PayloadDocumentation.fieldWithPath("[]").description("the set of all greetings")
                                        .type(JsonFieldType.ARRAY))
                                //and re-use the documentation of the greetings object with a prefix
                                .andWithPrefix("[].", GREETING_FIELD_DESCRIPTORS))
                );
    }

    //documentation of an error response
    @Test
    public void testCreateGreetingWithoutNameResultsInError() throws Exception {

        Greeting greeting = new Greeting();
        String greetingJson = objectMapper.writeValueAsString(greeting);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/greetings").header("Authorization", "foo")
                        .content(greetingJson).contentType(MediaType.APPLICATION_JSON_UTF8))

                .andExpect(MockMvcResultMatchers.status().isBadRequest())

                .andDo(document.document(

                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("message").description("the error message")
                                        .type(JsonFieldType.STRING)))
                );
    }
}
