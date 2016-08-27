package com.skronawi.spring.examples.caching.hibernate.secondlevel;

import com.skronawi.spring.examples.caching.hibernate.TestDatabaseConfig;
import com.skronawi.spring.examples.caching.hibernate.Item;
import com.skronawi.spring.examples.caching.hibernate.ItemManager;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.persistence.EntityManagerFactory;
import java.util.concurrent.CountDownLatch;

/*
here no transactions are used. every itemManager-call uses a new entitymanager/session. but due to the used second-level
cache, no unnecessary sql queries are executed.
 */
@ContextConfiguration(classes = {TestDatabaseConfig.class, SecondLevelCacheConfig.class})
public class SecondLevelCachingTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ItemManager itemManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private Item createdItem;

    private Statistics statistics;

    @BeforeClass
    public void createItemToGet() {

        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        statistics = sessionFactory.getStatistics();

        Item item = new Item();
        item.setValue("cache test");
        createdItem = itemManager.create(item);

        Assert.assertEquals(statistics.getSecondLevelCachePutCount(), 1);
    }

    @Test
    public void getMultipleTimes() throws Exception {

        statistics.clear();

        //different entityManagers, so first-level-cache only user per-get(), so 3 sql queries
        //but second-level cache is used, so only 1 sql query.
        itemManager.get(createdItem.getId());
        itemManager.get(createdItem.getId());
        itemManager.get(createdItem.getId());

        //how often did the second level cache NOT contain an id?
        Assert.assertEquals(statistics.getSecondLevelCacheMissCount(), 0);
        //how often DID the 2L cache contain an id?
        Assert.assertEquals(statistics.getSecondLevelCacheHitCount(), 3);
        //how often was the 2L cache filled, e.g. after a actual query to the database returning fresh data?
        Assert.assertEquals(statistics.getSecondLevelCachePutCount(), 0);
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

    @Test
    public void twoInterferingQueries() throws Exception{

        CountDownLatch updateWaitLatch = new CountDownLatch(1);
        CountDownLatch secondGetWaitLatch = new CountDownLatch(1);
        CountDownLatch testWaitLatch = new CountDownLatch(1);

        new Thread(
                () -> {
                    Item item = itemManager.get(createdItem.getId());
                    System.out.println("got the item the first time");
                    updateWaitLatch.countDown();

                    try {
                        secondGetWaitLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Item item2 = itemManager.get(createdItem.getId());
                    System.out.println("got the item the second time");

                    //itemManagers use shared 2L cache, so outer update is recognized here
                    Assert.assertNotEquals(item2.getValue(), item.getValue());

                    testWaitLatch.countDown();
                }).start();

        new Thread(
                () -> {
                    try {
                        updateWaitLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    createdItem.setValue("updated2");
                    createdItem = itemManager.update(createdItem);
                    System.out.println("updated the item");
                    secondGetWaitLatch.countDown();
                }).start();

        testWaitLatch.await();
    }
}
