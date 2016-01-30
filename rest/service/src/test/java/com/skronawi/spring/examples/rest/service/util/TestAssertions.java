package com.skronawi.spring.examples.rest.service.util;

import com.skronawi.spring.examples.rest.communication.RestData;
import org.testng.Assert;

import java.util.Set;

public class TestAssertions {

    public static void assertCreated(RestData actualRestData, RestData createdRestData) {
        Assert.assertNotNull(createdRestData);
        Assert.assertNotNull(createdRestData.getId());
        Assert.assertNotEquals("", createdRestData.getId());
        Assert.assertEquals(actualRestData.getData(), createdRestData.getData());
    }

    public static void assertEquals(RestData actualRestData, RestData expectedRestData) {
        Assert.assertNotNull(actualRestData);
        Assert.assertNotNull(expectedRestData);
        Assert.assertEquals(actualRestData.getData(), expectedRestData.getData());
        Assert.assertEquals(actualRestData.getId(), expectedRestData.getId());
    }

    public static void assertContainsById(Set<RestData> all, RestData subject) {
        boolean contains = false;
        for (RestData restData : all){
            if (restData.getId().equals(subject.getId())){
                contains = true;
                break;
            }
        }
        Assert.assertTrue(contains);
    }
}
