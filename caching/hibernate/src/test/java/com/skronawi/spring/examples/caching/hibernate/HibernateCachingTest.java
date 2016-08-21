package com.skronawi.spring.examples.caching.hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.stream.IntStream;

@ContextConfiguration(classes = {HibernateCachingTestDatebaseConfig.class})
public class HibernateCachingTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ItemManager itemManager;

    private Item createdItem;

    @BeforeClass
    public void createItemToGet() {
        Item item = new Item();
        item.setValue("cache test");
        createdItem = itemManager.create(item);
    }

    @Test
    public void getMultipleTimes() throws Exception {
        //every get will result in 1 sql query
        IntStream.range(0, 10).forEach(i -> itemManager.get(createdItem.getId()));
    }

    @Test
    public void getMultipleTimesWithinInService() throws Exception {
        //although the loop over the repository.findOne() is within the manager, 10 sql queries are made.
        //so not 1 session here automatically.
        itemManager.getNTimes(createdItem.getId(), 10);
    }

    @Test
    public void getMultipleTimesInTransaction() throws Exception {
        //transaction support is active by @EnableTransactionManagement, so in this transaction the same entityManager
        //is re-used, thus only 1 sql query.
        itemManager.getNTimesInTransaction(createdItem.getId(), 10);
    }
}
