package com.skronawi.spring.examples.caching.hibernate.firstlevel;

import com.skronawi.spring.examples.caching.hibernate.HibernateTestDatebaseConfig;
import com.skronawi.spring.examples.caching.hibernate.Item;
import com.skronawi.spring.examples.caching.hibernate.ItemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.stream.IntStream;

@ContextConfiguration(classes = {HibernateTestDatebaseConfig.class, DefaultCacheConfig.class})
public class HibernateFirstLevelCachingTest extends AbstractTestNGSpringContextTests {

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

    @Transactional
    @Test
    public void getMultipleTimesWithinInServiceButExternalTransaction() throws Exception {
        //@Transactional has no effect on test
        itemManager.getNTimes(createdItem.getId(), 10);
    }

    @Test
    public void getMultipleTimesInTransaction() throws Exception {
        //transaction support is active by @EnableTransactionManagement, so in this transaction the same entityManager
        //is re-used, thus only 1 sql query.
        itemManager.getNTimesInTransaction(createdItem.getId(), 10);
    }

    @Test
    public void queryMultipleTimes() throws Exception{
        //not cached
        itemManager.queryByIdNTimes(createdItem.getId(), 10);
    }

    @Test
    public void queryMultipleTimesInTransaction() throws Exception{
        //not cached
        itemManager.queryByIdNTimesInTransaction(createdItem.getId(), 10);
    }

    @Test
    public void queryMultipleTimesByValue() throws Exception{
        //not cached
        itemManager.queryByValueNTimes(createdItem.getValue(), 10);
    }

    @Test
    public void queryMultipleTimesByValueInTransaction() throws Exception{
        //not cached
        itemManager.queryByValueNTimesInTransaction(createdItem.getValue(), 10);
    }

    @Test
    public void updateMultipleTimes() throws Exception{
        createdItem.setValue("updated");
        itemManager.updateNTimes(createdItem, 10);
    }

    @Test
    public void updateMultipleTimesInTransaction() throws Exception{
        createdItem.setValue("updated");
        itemManager.updateNTimesInTransaction(createdItem, 10);
    }

//    @Test
//    public void deleteMultipleTimes() throws Exception{
//        Item item = new Item();
//        item.setValue("blub");
//        item = itemManager.create(item);
//        itemManager.deleteNTimes(item.getId(), 10);
//    }
//
//    @Test
//    public void deleteMultipleTimesInTransaction() throws Exception{
//        Item item = new Item();
//        item.setValue("blub");
//        item = itemManager.create(item);
//        itemManager.deleteNTimesInTransaction(item.getId(), 10);
//    }
}
