package com.skronawi.spring.examples.rest.docu.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import org.springframework.restdocs.restassured.operation.preprocess.RestAssuredPreprocessors;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(randomPort = true)
public class GreetingsResourceDocumentationRestAssuredTest {

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

    @Value("${local.server.port}")
    private int port;

    private RequestSpecification documentationSpec;
    private RestDocumentationFilter documentationFilter;
    private Greeting preparedGreeting;

    @Before
    public void setUp() throws Exception{

        this.documentationFilter = document("{class-name}/{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
        );

        documentationSpec = new RequestSpecBuilder()
                .addFilter(RestAssuredRestDocumentation.documentationConfiguration(this.restDocumentation))
                .addFilter(documentationFilter)
                .build();

        Greeting greeting = new Greeting();
        greeting.setName("prepared");

        preparedGreeting = RestAssured.given()
                .accept(ContentType.JSON)
                .when()
                    .port(this.port)
                    .header("Authorization", "foo")
                    .content(greeting)
                    .contentType(ContentType.JSON)
                    .post("/greetings")
                .then()
                    .assertThat().statusCode(CoreMatchers.is(HttpStatus.CREATED.value()))
                    .assertThat().contentType(ContentType.JSON)
                    .assertThat().content(CoreMatchers.containsString("prepared"))
                .extract()
                    .body().as(Greeting.class)
        ;
    }

    @Test
    public void testCreateGreeting() throws Exception {
        System.out.println(preparedGreeting.getId());
    }

}
