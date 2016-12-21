package com.skronawi.spring.examples.rest.docu.service;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import org.springframework.restdocs.restassured.operation.preprocess.RestAssuredPreprocessors;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

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
    private Greeting preparedGreeting;
    private RestDocumentationFilter document;

    @Before
    public void setUp() throws Exception{

        document = RestAssuredRestDocumentation.document("{class-name}/{method-name}",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint(),
                        //change the example URI for the documentation, especially the port
                        RestAssuredPreprocessors.modifyUris().host("localhost").scheme("http").port(8080)),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()));

        documentationSpec = new RequestSpecBuilder()
                .addFilter(RestAssuredRestDocumentation.documentationConfiguration(this.restDocumentation))
                .addFilter(document)
                .build();

        Greeting greeting = new Greeting();
        greeting.setName("prepared");

        preparedGreeting = RestAssured
                .given()
                    .port(this.port)
                    .header("Authorization", "foo")
                    .content(greeting)
                    .contentType(ContentType.JSON)
                .when()
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

        Greeting greeting = new Greeting();
        greeting.setName("simon");

        RestAssured
                .given(documentationSpec)
                    .port(this.port)
                    .header("Authorization", "foo")
                    .content(greeting)
                    .contentType(ContentType.JSON)

                .filter(document.document(
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
                ))

                .when()
                    .post("/greetings")

                .then()
                    .assertThat().statusCode(CoreMatchers.is(HttpStatus.CREATED.value()))
                    .assertThat().contentType(ContentType.JSON)
                    .assertThat().header("dummy-response-header", CoreMatchers.is("bar"))
                    .assertThat().body("name", Matchers.equalTo("simon"))
                    .assertThat().body("id", Matchers.notNullValue())
                    .assertThat().body("timestamp", Matchers.notNullValue())
        ;
    }

    @Test
    public void testGetSingleGreeting() throws Exception{

        RestAssured
                .given(documentationSpec)
                    .port(this.port)
                    .accept(ContentType.JSON)

                .filter(document.document(
                        RequestDocumentation.pathParameters(RequestDocumentation.parameterWithName("id")
                                .description("the id of the greeting to get")),

                        PayloadDocumentation.responseFields(GREETING_FIELD_DESCRIPTORS))
                )

                .when()
                    .get("/greetings/{id}", preparedGreeting.getId())

                .then()
                    .assertThat().statusCode(CoreMatchers.is(HttpStatus.OK.value()))
                    .assertThat().contentType(ContentType.JSON)
                    .assertThat().header("dummy-response-header", Matchers.nullValue())
                    .assertThat().body("name", Matchers.equalTo("prepared"))
                    .assertThat().body("id", Matchers.notNullValue())
                    .assertThat().body("timestamp", Matchers.notNullValue())
                ;
    }

    @Test
    public void testGetAllGreetings() throws Exception{

        RestAssured
                .given(documentationSpec)
                    .port(this.port)
                    .accept(ContentType.JSON)

                .filter(document.document(
                        PayloadDocumentation.responseFields(
                                //also document the list itself
                                PayloadDocumentation.fieldWithPath("[]").description("the set of all greetings")
                                        .type(JsonFieldType.ARRAY))
                                .andWithPrefix("[].", GREETING_FIELD_DESCRIPTORS))
                )

                .when()
                    .get("/greetings")

                .then()
                    .assertThat().statusCode(CoreMatchers.is(HttpStatus.OK.value()))
                    .assertThat().contentType(ContentType.JSON)
                    .assertThat().header("dummy-response-header", Matchers.nullValue())
                    .assertThat().body("name", Matchers.hasItems("prepared"))
                    .assertThat().body("id", Matchers.everyItem(Matchers.notNullValue()))
                    .assertThat().body("timestamp", Matchers.everyItem(Matchers.notNullValue()))
                    //https://groups.google.com/forum/#!topic/rest-assured/R-n0YwYj9RA
                    .assertThat().body("size()", Matchers.greaterThanOrEqualTo(1))
        ;
    }

    @Test
    public void testFindGreetingsByName() throws Exception{

        Greeting greeting = new Greeting();
        greeting.setName("ina");
        RestAssured
                .given()
                    .port(this.port)
                    .header("Authorization", "foo")
                    .content(greeting)
                    .contentType(ContentType.JSON)
                .when()
                    .post("/greetings")
                .then()
                    .assertThat().statusCode(CoreMatchers.is(HttpStatus.CREATED.value()))
                    .assertThat().contentType(ContentType.JSON)
                    .assertThat().content(CoreMatchers.containsString("ina"))
                ;

        RestAssured
                .given(documentationSpec)
                    .port(this.port)
                    .accept(ContentType.JSON)
                    .param("name", "ina")

                .filter(document.document(
                        RequestDocumentation.requestParameters(RequestDocumentation.parameterWithName("name")
                                .description("the name to search greetings for")),

                        PayloadDocumentation.responseFields(
                                //also document the list itself
                                PayloadDocumentation.fieldWithPath("[]").description("the set of all greetings")
                                        .type(JsonFieldType.ARRAY))
                                //and re-use the documentation of the greetings object with a prefix
                                .andWithPrefix("[].", GREETING_FIELD_DESCRIPTORS))
                )

                .when()
                    .get("/greetings")

                .then()
                    .assertThat().statusCode(CoreMatchers.is(HttpStatus.OK.value()))
                    .assertThat().contentType(ContentType.JSON)
                    .assertThat().header("dummy-response-header", Matchers.nullValue())
                    .assertThat().body("name", Matchers.hasItems("ina"))
                    .assertThat().body("id", Matchers.everyItem(Matchers.notNullValue()))
                    .assertThat().body("timestamp", Matchers.everyItem(Matchers.notNullValue()))
                    //https://groups.google.com/forum/#!topic/rest-assured/R-n0YwYj9RA
                    .assertThat().body("size()", Matchers.equalTo(1))
        ;
    }

    //documentation of an error response
    @Test
    public void testCreateGreetingWithoutNameResultsInError() throws Exception {

        Greeting greeting = new Greeting();
        RestAssured
                .given(documentationSpec)
                    .port(this.port)
                    .header("Authorization", "foo")
                    .content(greeting)
                    .contentType(ContentType.JSON)

                .filter(document.document(
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("message").description("the error message")
                                        .type(JsonFieldType.STRING)))
                )

                .when()
                    .post("/greetings")

                .then()
                    .assertThat().statusCode(CoreMatchers.is(HttpStatus.BAD_REQUEST.value()))
                    .assertThat().contentType(ContentType.JSON)
        ;
    }
}
