package com.skronawi.spring.examples.rest.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;
import org.testng.annotations.Test;

@SpringApplicationConfiguration(classes = {RestService.class, Application.class})
@WebIntegrationTest(randomPort = true)
/*
http://blog.codeleak.pl/2015/03/spring-boot-integration-testing-with.html
http://blog.jdriven.com/2015/03/integration-testing-on-rest-urls-with-spring-boot/
https://github.com/kolorobot/spring-boot-thymeleaf/blob/master/src/main/java/pl/codeleak/demos/sbt/Application.java
*/
public class RestBootIntegrationTest extends AbstractTestNGSpringContextTests {

    @Value("${local.server.port}")
    private String port;

    @Test
    public void testGetAllRestTemplate() throws Exception {
        //i think, this works only with spring boot and @WebIntegrationTest
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject("http://localhost:" + port + "/data", String.class);
        Assert.assertEquals(result, "[]");
    }
}
