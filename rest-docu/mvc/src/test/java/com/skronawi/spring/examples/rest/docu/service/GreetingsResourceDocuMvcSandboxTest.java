package com.skronawi.spring.examples.rest.docu.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.hypermedia.HypermediaDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

/*
just a few tests, which show different things, which are noteworthy or different to the "good case".
they have to be enabled manually by out-commenting the @Ignore!
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebConfig.class)
@WebAppConfiguration
public class GreetingsResourceDocuMvcSandboxTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private RestDocumentationResultHandler document;
    private ObjectMapper objectMapper;

    @Before
    public void setUp(){

        this.document = MockMvcRestDocumentation.document("{class-name}/{method-name}",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()));

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(document)
                .build();

        objectMapper = new ObjectMapper();
    }

    @Ignore //does not work without external request!?
    @Test
    public void testExternalCallForDocumentationCreation() throws Exception{

//        document.document(
        document.snippets(
                PayloadDocumentation.requestFields(
                        //customize the table a little, see also rest-docu/mvc/src/test/resources/org/springframework/restdocs/templates/asciidoctor/request-fields.snippet
                        Attributes.attributes(Attributes.key("title").value("Fields for creating a greeting")),
                        PayloadDocumentation.fieldWithPath("name").description("the name of the one, who should be greeted")
                                //add a extra columns with constraints
                                .attributes(Attributes.key("constraints").value("must not be null or empty")),
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
        );

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
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Ignore //just for example purposes, how hypermedia links can be documented
    @Test
    public void testHypermediaDocuExample() throws Exception{

        document.snippets(
                HypermediaDocumentation.links(
                        HypermediaDocumentation.halLinks(), HypermediaDocumentation.linkWithRel("self")
                                .description("The employee's resource"),
                        HypermediaDocumentation.linkWithRel("employee").optional().description("The employee's projection")),

                PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("username").description("The employee unique database identifier")
                                .type(String.class),
                        PayloadDocumentation.fieldWithPath("_links").description("links to other resources")
                ));
    }

    @Ignore
    @Test
    public void testCodeDocuMismatchAttributeNameResultsInTestError() throws Exception {

        /*
        in the code the attribute is "name", in the docu part the attribute is "greetee" => results in a test error !
         */

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

                .andDo(document.document(
                        PayloadDocumentation.requestFields(
                                //customize the table a little, see also rest-docu/mvc/src/test/resources/org/springframework/restdocs/templates/asciidoctor/request-fields.snippet
                                Attributes.attributes(Attributes.key("title").value("Fields for creating a greeting")),
                                PayloadDocumentation.fieldWithPath("name").description("the name of the one, who should be greeted")
                                        //add a extra columns with constraints
                                        .attributes(Attributes.key("constraints").value("must not be null or empty")),
                                PayloadDocumentation.fieldWithPath("id").ignored(),
                                PayloadDocumentation.fieldWithPath("timestamp").ignored()),

                        PayloadDocumentation.responseFields(

                                // !! "greetee" is wrong !!
                                PayloadDocumentation.fieldWithPath("greetee").description("the name of the one, who has been greeted")
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
}
