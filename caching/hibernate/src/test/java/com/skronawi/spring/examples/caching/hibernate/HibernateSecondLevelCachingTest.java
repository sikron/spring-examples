package com.skronawi.spring.examples.caching.hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.stream.IntStream;

/*
here no transactions are used. every itemManager-call uses a new entitymanager/session. but due to the used second-level
cache, no unnecessary sql queries are executed.
 */
@ContextConfiguration(classes = {HibernateSecondLevelCacheTestDatebaseConfig.class})
public class HibernateSecondLevelCachingTest extends AbstractTestNGSpringContextTests {

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

        //different entityManagers, so first-level-cache only user per-get(), so 3 sql queries
        //but second-level cache is used, so only 1 sql query.
        itemManager.get(createdItem.getId());
        itemManager.get(createdItem.getId());
        itemManager.get(createdItem.getId());
    }

    @Test
    public void getAndUpdateAndGetAgainShouldResultInUpdatedItemWithoutQuery(){

        Item item = itemManager.get(createdItem.getId());
        item.setValue("other value");
        Item updatedItem = itemManager.update(item);

        //no sql query executed here, as the updated item is taken from the second-level cache
        Item retrievedItem = itemManager.get(updatedItem.getId());
        Assert.assertEquals(retrievedItem.getValue(), item.getValue());
    }
}
