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
they have to be enabled manually!
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

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                //TODO in the test a id has to be given nevertheless. how to omit this and use this central config?
//                .alwaysDo(MockMvcRestDocumentation.document("{class-name}/{method-name}"))
                .build();

        this.document = MockMvcRestDocumentation.document("{class-name}/{method-name}",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
        );

        objectMapper = new ObjectMapper();
    }

    @Ignore // only generates request-fields.adoc, request-headers.adoc, response-fields.adoc and response-headers.adoc!?
    @Test
    public void testUsePreparedDocumentResultHandler() throws Exception {

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

    @Ignore //does not work without request!? maybe the examples are old:
    //- https://ordina-jworks.github.io/conference/2016/06/30/SpringIO16-Spring-Rest-Docs.html
    //- http://www.baeldung.com/spring-rest-docs
    @Test
    public void testExternalDocumentationCreation() throws Exception{

        // just a dummy documentation

        document.snippets(
                RequestDocumentation.requestParameters(RequestDocumentation.parameterWithName("name")
                        .description("the name of the one, who should be greeted")),
                PayloadDocumentation.responseFields(PayloadDocumentation.fieldWithPath("name")
                        .description("the name of the one, who has been greeted").type(JsonFieldType.STRING))
        );
    }

    @Ignore //just for docu purposes, how hypermedia links can be documented
    @Test
    public void testHypermediaDocu() throws Exception{

        // just a dummy documentation

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
                                PayloadDocumentation.fieldWithPath("name").description("the name of the one, who should be greeted"),
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
