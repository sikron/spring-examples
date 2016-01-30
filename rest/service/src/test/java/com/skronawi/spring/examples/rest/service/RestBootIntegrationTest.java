package com.skronawi.spring.examples.rest.service;

import com.skronawi.spring.examples.rest.communication.RestData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.util.Collections;
import java.util.Set;

@SpringApplicationConfiguration(classes = {RestService.class, Application.class})
@WebIntegrationTest(randomPort = true)
/*
http://blog.codeleak.pl/2015/03/spring-boot-integration-testing-with.html
http://blog.jdriven.com/2015/03/integration-testing-on-rest-urls-with-spring-boot/
https://github.com/kolorobot/spring-boot-thymeleaf/blob/master/src/main/java/pl/codeleak/demos/sbt/Application.java
*/
public class RestBootIntegrationTest extends AbstractRestTest {

    @Value("${local.server.port}")
    private String port;

    private RestTemplate restTemplate;

    @BeforeClass
    public void setUp() throws Exception {
        restTemplate = new RestTemplate();
        cleanup();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() throws Exception {
        Set<RestData> restDatas = getAllAndAssertResponse();
        for (RestData restData : restDatas) {
            deleteAndAssertResponse(restData.getId());
        }
    }

    @Override
    protected RestData createAndAssertResponse(RestData restData) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        String restDataJson = objectMapper.writeValueAsString(restData);
        HttpEntity<String> entity = new HttpEntity<String>(restDataJson, headers);
        ResponseEntity<RestData> response = restTemplate.postForEntity("http://localhost:" + port + "/data",
                entity, RestData.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        Assert.assertTrue(response.getHeaders().getContentType().isCompatibleWith(MediaType.APPLICATION_JSON));
        return response.getBody();
    }

    @Override
    protected Set<RestData> getAllAndAssertResponse() throws Exception {
        ResponseEntity<Set<RestData>> response = restTemplate.exchange("http://localhost:" + port + "/data",
                HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Set<RestData>>() {
                });
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertTrue(response.getHeaders().getContentType().isCompatibleWith(MediaType.APPLICATION_JSON));
        return response.getBody();
    }

    @Override
    protected RestData getAndAssertResponse(String id) throws Exception {
        ResponseEntity<RestData> response = restTemplate.getForEntity("http://localhost:" + port + "/data/" + id,
                RestData.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertTrue(response.getHeaders().getContentType().isCompatibleWith(MediaType.APPLICATION_JSON));
        return response.getBody();
    }

    @Override
    protected RestData updateAndAssertResponse(RestData restData) throws Exception {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    protected void deleteAndAssertResponse(String id) throws Exception {
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/data/" + id,
                HttpMethod.GET, HttpEntity.EMPTY, String.class);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}
