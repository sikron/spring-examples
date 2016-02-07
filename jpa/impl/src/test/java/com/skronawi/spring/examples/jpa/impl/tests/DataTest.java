package com.skronawi.spring.examples.jpa.impl.tests;

import com.skronawi.spring.examples.jpa.impl.JpaData;
import com.skronawi.spring.examples.jpa.impl.JpaDataPersistence;
import com.skronawi.spring.examples.jpa.impl.JpaModelFactory;
import com.skronawi.spring.examples.jpa.impl.TestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.util.Set;

@ContextConfiguration(classes = TestConfig.class)  //Spring uses this configuration class for setting up the test environment
public class DataTest extends AbstractTestNGSpringContextTests { //TestNG as test runner must have access to the Spring environment, thus the subclassing

    @Autowired
    private JpaDataPersistence persistence;
    @Autowired
    private JpaModelFactory modelFactory;

    @AfterClass(alwaysRun = true)
    public void cleanup() throws Exception {
        Set<JpaData> all = persistence.getAll();
        for (JpaData jpaData : all) {
            persistence.delete(jpaData.getId());
        }
    }

    @Test
    public void testCreateGet() throws Exception {

        JpaData data = modelFactory.createData();
        data.setData("this is a test");

        JpaData createdData = persistence.create(data);

        Assert.assertNotNull(createdData.getId());
        Assert.assertEquals(createdData.getData(), data.getData());
    }

    @Test
    public void testDelete() throws Exception {

        JpaData data = modelFactory.createData();
        data.setData("this is a test");

        JpaData createdData = persistence.create(data);

        persistence.delete(createdData.getId());

        JpaData deleted = persistence.get(createdData.getId());
        Assert.assertNull(deleted);
    }

    @Test
    public void testUpdate() throws Exception{

        JpaData data = modelFactory.createData();
        data.setData("this is a test");

        JpaData createdData = persistence.create(data);

        createdData.setData("this is a updated test");
        JpaData updatedData = persistence.update(createdData);

        Assert.assertEquals(updatedData.getData(), "this is a updated test");
    }
}
