package com.skronawi.spring.examples.elasticsearch.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.Set;

@ContextConfiguration(classes = TestConfig.class)
public class DataTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ElasticDataPersistence persistence;
    @Autowired
    private ElasticModelFactory modelFactory;

    @AfterClass(alwaysRun = true)
    public void cleanup() throws Exception {
        Set<ElasticData> all = persistence.getAll();
        for (ElasticData ElasticData : all) {
            persistence.delete(ElasticData.getId());
        }
    }

    @Test
    public void testCreateGet() throws Exception {

        ElasticData data = modelFactory.createData();
        data.setData("this is a test");

        ElasticData createdData = persistence.create(data);

        Assert.assertNotNull(createdData.getId());
        Assert.assertEquals(createdData.getData(), data.getData());
    }

    @Test
    public void testDelete() throws Exception {

        ElasticData data = modelFactory.createData();
        data.setData("this is a test");

        ElasticData createdData = persistence.create(data);

        persistence.delete(createdData.getId());

        ElasticData deleted = persistence.get(createdData.getId());
        Assert.assertNull(deleted);
    }

    @Test
    public void testUpdate() throws Exception {

        ElasticData data = modelFactory.createData();
        data.setData("this is a test");

        ElasticData createdData = persistence.create(data);

        createdData.setData("this is a updated test");
        ElasticData updatedData = persistence.update(createdData);

        Assert.assertEquals(updatedData.getData(), "this is a updated test");
    }
}
