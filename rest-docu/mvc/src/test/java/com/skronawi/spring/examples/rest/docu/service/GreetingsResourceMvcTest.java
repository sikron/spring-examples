package com.skronawi.spring.examples.rest.docu.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebConfig.class)
@WebAppConfiguration
public class GreetingsResourceMvcTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private RestDocumentationResultHandler document;

    @Before
    public void setUp(){

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                //TODO in the test a id has to be given nevertheless. how to omit this and use this central config?
//                .alwaysDo(MockMvcRestDocumentation.document("{class-name}/{method-name}"))
                .build();

        this.document = MockMvcRestDocumentation.document("{class-name}/{method-name}",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()));
    }

    @Test
    public void testCompleteGetGreeting() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/greetings").param("name", "simon").header("Authorization", "foo"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("simon")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))

                .andDo(MockMvcRestDocumentation.document("{class-name}/{method-name}",
                        RequestDocumentation.requestParameters(RequestDocumentation.parameterWithName("name")
                                .description("the name of the one, who should be greeted")),
                        PayloadDocumentation.responseFields(PayloadDocumentation.fieldWithPath("name")
                                .description("the name of the one, who has been greeted").type(JsonFieldType.STRING)),
                        HeaderDocumentation.requestHeaders(HeaderDocumentation.headerWithName("Authorization")
                                .description("the authorization value")),
                        HeaderDocumentation.responseHeaders(HeaderDocumentation.headerWithName("dummy-response-header")
                                .description("a dummy response header"))
                        )
                );
    }

    // --------- only examples with "documentation" purposes, what is possible to document and how ---------------------

    @Ignore //does not work without request!? maybe the examples are old:
    //- https://ordina-jworks.github.io/conference/2016/06/30/SpringIO16-Spring-Rest-Docs.html
    //- http://www.baeldung.com/spring-rest-docs
    @Test
    public void testExternalDocumentationCreation() throws Exception{

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

    @Ignore //does not run without adaption of Greeting. only for demonstration purposes of what errors can be prevented
    @Test
    public void testCodeDocuMismatchAttributeName() throws Exception {

        /*
        in the code the attribute and query-param is "greetee", in the docu part the attribute is "name"
        ==> results in a test error !

        this only really results in a test-error if the Greeting object really has the attribute "greetee"
         */

        mockMvc.perform(
                MockMvcRequestBuilders.get("/greetings").param("greetee", "simon"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))

                .andDo(MockMvcRestDocumentation.document("{class-name}/{method-name}",
                        RequestDocumentation.requestParameters(RequestDocumentation.parameterWithName("name")
                                .description("the name of the one, who should be greeted")),
                        PayloadDocumentation.responseFields(PayloadDocumentation.fieldWithPath("name")
                                .description("the name of the one, who has been greeted").type(JsonFieldType.STRING))
                        )
                );
    }

    @Ignore //only for demonstration purposes of what errors can be prevented
    @Test
    public void testCodeDocuMismatchMissingAttribute() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/greetings").param("name", "simon"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("simon")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))

                .andDo(MockMvcRestDocumentation.document("{class-name}/{method-name}",
                        RequestDocumentation.requestParameters(RequestDocumentation.parameterWithName("name")
                                .description("the name of the one, who should be greeted")),
                        PayloadDocumentation.responseFields(PayloadDocumentation.fieldWithPath("name")
                                .description("the name of the one, who has been greeted").type(JsonFieldType.STRING)),
                        //"notexisting" is missing in the Greeting object
                        PayloadDocumentation.responseFields(PayloadDocumentation.fieldWithPath("notexisting")
                                .description("this field is contained in the docu, but missing in the object").type(JsonFieldType.STRING))
                        )
                );
    }
}
