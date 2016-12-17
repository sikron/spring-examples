package com.skronawi.spring.examples.rest.docu.service;

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
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(randomPort = true)
public class MyResourceRestAssuredTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Value("${local.server.port}")
    private int port;

    private RequestSpecification documentationSpec;

    @Before
    public void setUp() {
        documentationSpec = new RequestSpecBuilder()
                .addFilter(RestAssuredRestDocumentation.documentationConfiguration(this.restDocumentation))
                .addFilter(RestAssuredRestDocumentation.document("{class-name}/{method-name}")).build();
    }

    @Test
    public void testGreetingsWithParam() throws Exception {

        RestAssured.given(this.documentationSpec)
                .accept("text/plain")
                .when()
                    .port(this.port)
                    .get("/greetings?name=simon")
                .then()
                    .assertThat().statusCode(CoreMatchers.is(HttpStatus.OK.value()))
                    .assertThat().contentType(ContentType.TEXT)
                    .assertThat().content(CoreMatchers.is("hi simon\n"))
        ;
    }

}
