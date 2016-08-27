package com.skronawi.spring.examples.caching.hibernate.secondlevel;

import com.skronawi.spring.examples.caching.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@ContextConfiguration(classes = {TestDatabaseConfig.class, SecondLevelCacheConfig.class})
public class QueryCachingTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ItemManager itemManager;

    @Autowired
    private ItemCollectionManager itemCollectionManager;

    private Item createdItem;

    @BeforeClass
    public void createItemToGet() {

        Item item = new Item();
        item.setValue("cache test");
        createdItem = itemManager.create(item);
    }

    @Test
    public void queryByIdMultipleTimes() throws Exception {

        //repository query does not have a QueryHint, so multiple queries
        Item item = itemManager.queryByIdNTimes(createdItem.getId(), 1);
        itemManager.queryByIdNTimes(createdItem.getId(), 1);
        itemManager.queryByIdNTimes(createdItem.getId(), 1);
        itemManager.queryByIdNTimes(createdItem.getId(), 1);
    }

    @Test
    public void queryByValueMultipleTimes() throws Exception {

        //repository query does have a QueryHint, so 1 query only
        itemManager.queryByValueNTimes(createdItem.getValue(), 1);
        itemManager.queryByValueNTimes(createdItem.getValue(), 1);
        itemManager.queryByValueNTimes(createdItem.getValue(), 1);
        itemManager.queryByValueNTimes(createdItem.getValue(), 1);
    }

    @Test
    public void findByValueMultipleTimes() throws Exception {

        //repository query does have a QueryHint, so 1 query only
        itemManager.findByValueNTimes(createdItem.getValue(), 1);
        itemManager.findByValueNTimes(createdItem.getValue(), 1);
        itemManager.findByValueNTimes(createdItem.getValue(), 1);
        itemManager.findByValueNTimes(createdItem.getValue(), 1);
    }

    @Test
    public void queryByValueCachedButUpdate() throws Exception {

        //1 query only
        itemManager.queryByValueNTimes(createdItem.getValue(), 1);
        itemManager.queryByValueNTimes(createdItem.getValue(), 1);
        itemManager.queryByValueNTimes(createdItem.getValue(), 1);
        itemManager.queryByValueNTimes(createdItem.getValue(), 1);

        createdItem.setValue("updated");
        createdItem = itemManager.update(createdItem);

        //after update, a real query is performed
        itemManager.queryByValueNTimes(createdItem.getValue(), 1);
    }

    @Test
    public void findByValueMultipleItemsAndUpdate() throws Exception {

        Item item = new Item();
        item.setValue("byValue");
        Item createdItem1 = itemManager.create(item);
        Item createdItem2 = itemManager.create(item);
        Item createdItem3 = itemManager.create(item);

        Set<Item> itemsByValue = itemManager.findByValueNTimes(item.getValue(), 2);
        Assert.assertEquals(itemsByValue.size(), 3);

        createdItem3.setValue("other-updated");
        createdItem3 = itemManager.update(createdItem3);

        itemsByValue = itemManager.findByValueNTimes(item.getValue(), 1);
        Assert.assertEquals(itemsByValue.size(), 2);
    }

    @Test
    public void testCollectionQueryWithSecondLevelCacheById() throws Exception{

        /*
        here the second-level cache is used instead of the query cache, as the collection is retrieved by id
         */

        ItemCollection itemCollection = buildItemCollection();

        itemCollection = itemCollectionManager.create(itemCollection);

        ItemCollection retrievedItemCollection = itemCollectionManager.getById(itemCollection.getId());
        Assert.assertEquals(retrievedItemCollection.getItems().size(), 3);

        //gets by id are not covered by the query cache, but by the 2L cache. so no further query here
        itemCollectionManager.getById(itemCollection.getId());
    }

    @Test
    public void testCallsToItemsAfterItemCollectionCallResultsAreIn2LCache() throws Exception{

        ItemCollection itemCollection = buildItemCollection();

        itemCollection = itemCollectionManager.create(itemCollection);

        ItemCollection retrievedItemCollection = itemCollectionManager.getById(itemCollection.getId());
        Assert.assertEquals(retrievedItemCollection.getItems().size(), 3);

        //the items are in the 2L cache, so get by id will not result in a database call
        itemManager.getNTimes(retrievedItemCollection.getItems().iterator().next().getId(), 1);

        //byValue is a real query and thus not covered by the 2L cache. so this will result in database calls
        itemManager.findByValueNTimes(retrievedItemCollection.getItems().iterator().next().getValue(), 1);
        //the second time, no database calls happen, as the query was cached in the query cache
        itemManager.findByValueNTimes(retrievedItemCollection.getItems().iterator().next().getValue(), 1);
    }

    @Test
    public void testCollectionQueryWithQueryCache() throws Exception{

        ItemCollection itemCollection = buildItemCollection();

        itemCollection = itemCollectionManager.create(itemCollection);

        Set<ItemCollection> retrievedItemCollections = itemCollectionManager.findByValueCached(itemCollection.getValue());
        Assert.assertEquals(retrievedItemCollections.size(), 1);
        Assert.assertEquals(retrievedItemCollections.iterator().next().getItems().size(), 3);

        //the query cache is used, so no database call here
        itemCollectionManager.findByValueCached(itemCollection.getValue());
    }

    @Test
    public void testCollectionQueryWithoutQueryCache() throws Exception{

        ItemCollection itemCollection = buildItemCollection();

        itemCollection = itemCollectionManager.create(itemCollection);

        Set<ItemCollection> retrievedItemCollections = itemCollectionManager.findByAttributeNotCached(itemCollection.getAttribute());
        Assert.assertEquals(retrievedItemCollections.size(), 1);
        Assert.assertEquals(retrievedItemCollections.iterator().next().getItems().size(), 3);

        //the query cache is NOT used, so a database call will be performed here
        itemCollectionManager.findByAttributeNotCached(itemCollection.getAttribute());
    }

    private ItemCollection buildItemCollection() {

        Item item1 = new Item();
        item1.setValue("a");
        Item item2 = new Item();
        item2.setValue("b");
        Item item3 = new Item();
        item3.setValue("c");

        Set<Item> items = new HashSet<>();
        items.add(item1);
        items.add(item2);
        items.add(item3);
        ItemCollection itemCollection = new ItemCollection();

        itemCollection.setItems(items);
        itemCollection.setValue("foo" + UUID.randomUUID().toString());
        itemCollection.setAttribute("bar" + UUID.randomUUID().toString());

        return itemCollection;
    }
}
