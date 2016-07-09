package com.skronawi.spring.examples.rest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.skronawi.spring.examples.rest.communication.MyPageable;
import com.skronawi.spring.examples.rest.communication.RestData;
import com.skronawi.spring.examples.rest.service.util.TestAssertions;
import com.skronawi.spring.examples.rest.service.util.TestUtil;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Set;

/**
 * A abstract class containing all Integration Tests. It requires its subclasses to implement methods, according to the
 * corresponding environment, e.g. MockMVC or Boot.
 */
public abstract class AbstractRestTest extends AbstractTestNGSpringContextTests {

    protected ObjectMapper objectMapper;
    protected CollectionType restDataSetType;

    @BeforeClass
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        restDataSetType = objectMapper.getTypeFactory().constructCollectionType(Set.class, RestData.class);
    }

    @Test
    public void createAndGet() throws Exception {

        //create
        RestData restData = TestUtil.dummyRestData();
        RestData createdRestData = createAndAssertResponse(restData);
        TestAssertions.assertCreated(restData, createdRestData);

        //get single
        RestData retrievedRestData = getAndAssertResponse(createdRestData.getId());
        TestAssertions.assertEquals(retrievedRestData, retrievedRestData);

        //get all
        Set<RestData> allRestDatas = getAllAndAssertResponse();
        TestAssertions.assertContainsById(allRestDatas, retrievedRestData);

        //get all with paging - create a second data, but only test the size
        allRestDatas = getAllAndAssertResponse(new MyPageable(0, 1));
        TestAssertions.assertContainsById(allRestDatas, retrievedRestData);
        RestData restData2 = TestUtil.dummyRestData();
        createAndAssertResponse(restData2);

        allRestDatas = getAllAndAssertResponse();
        Assert.assertEquals(allRestDatas.size(), 2);

        allRestDatas = getAllAndAssertResponse(new MyPageable(0, 1));
        Assert.assertEquals(allRestDatas.size(), 1);
        allRestDatas = getAllAndAssertResponse(new MyPageable(1, 2));
        Assert.assertEquals(allRestDatas.size(), 1);
        allRestDatas = getAllAndAssertResponse(new MyPageable(0, 2));
        Assert.assertEquals(allRestDatas.size(), 2);
    }

    @Test
    public void notFound() throws Exception {
        getAndNotFound("blub");
    }

    protected abstract RestData createAndAssertResponse(RestData restData) throws Exception;

    protected abstract Set<RestData> getAllAndAssertResponse() throws Exception;

    protected abstract Set<RestData> getAllAndAssertResponse(MyPageable myPageable) throws Exception;

    protected abstract RestData getAndAssertResponse(String id) throws Exception;

    protected abstract RestData updateAndAssertResponse(RestData restData) throws Exception;

    protected abstract void deleteAndAssertResponse(String id) throws Exception;

    protected abstract void getAndNotFound(String id) throws Exception;
}