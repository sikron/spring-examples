package com.skronawi.spring.examples.caching.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Set;

@ContextConfiguration(classes = CachingConfig.class)
public class ItemManagerCachingTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ItemManager itemManager;

    @Autowired
    private AccessCountingItemStore accessCountingResettableItemStore;

    @BeforeMethod
    public void reset(){
        accessCountingResettableItemStore.reset();
    }

    @Test
    public void createGet(){

        Item item = new Item(null, "the-value");
        Item createdItem = itemManager.create(item);
        itemManager.getById(createdItem.getId());
        itemManager.getById(createdItem.getId());
        itemManager.getById(createdItem.getId());

        Assert.assertEquals(accessCountingResettableItemStore.getNumberOfPuts(), 1);
        Assert.assertEquals(accessCountingResettableItemStore.getNumberOfGets(), 0);
    }

    @Test
    public void createUpdate(){

        Item item = new Item(null, "the-value");
        Item createdItem = itemManager.create(item);
        createdItem.setValue(createdItem.getValue() + "updated");
        Item updatedItem = itemManager.update(createdItem);

        //the "update" does not perform a real update internally only a change-by-reference
        Assert.assertEquals(accessCountingResettableItemStore.getNumberOfPuts(), 1);
    }

    @Test
    public void getDelete(){

        itemManager.create(new Item(null, "1"));
        Item item = itemManager.getById("1");

        Assert.assertEquals(accessCountingResettableItemStore.getNumberOfPuts(), 1);
        Assert.assertEquals(accessCountingResettableItemStore.getNumberOfGetsById(), 1);

        itemManager.delete("1");
        item = itemManager.getById("1");

        Assert.assertEquals(accessCountingResettableItemStore.getNumberOfDeletes(), 1);
        Assert.assertEquals(accessCountingResettableItemStore.getNumberOfGetsById(), 2);
        Assert.assertNull(item);
    }

    @Test
    public void multipleGetAllsWithCreateInBetween(){

        //create 2
        itemManager.create(new Item(null, "1"));
        itemManager.create(new Item(null, "2"));

        Assert.assertEquals(accessCountingResettableItemStore.getNumberOfPuts(), 2);

        //get them
        itemManager.getById();
        Set<Item> items = itemManager.getById();

        Assert.assertEquals(items.size(), 2);
        Assert.assertEquals(accessCountingResettableItemStore.getNumberOfGets(), 1);

        //now add a 3rd and see whether the result of the formerly cached get with 2 items is correct now with 3 items
        itemManager.create(new Item(null, "3"));
        items = itemManager.getById();

        Assert.assertEquals(accessCountingResettableItemStore.getNumberOfPuts(), 3);

        //FIXME the cached result of getAll does not have the newly created value!!!   //FIXED
        Assert.assertEquals(items.size(), 3); //should have increased
        Assert.assertEquals(accessCountingResettableItemStore.getNumberOfGets(), 2); //should have increased
    }

    @Test
    public void multipleGetAllsWithUpdateInBetween(){

        Item item = itemManager.create(new Item(null, "1"));
        itemManager.getById();

        Assert.assertEquals(accessCountingResettableItemStore.getNumberOfPuts(), 1);
        Assert.assertEquals(accessCountingResettableItemStore.getNumberOfGets(), 1);

        item.setValue("other");
        itemManager.update(item);

        //not changed, because no real internal put is made
        Assert.assertEquals(accessCountingResettableItemStore.getNumberOfPuts(), 1);

        Set<Item> items = itemManager.getById();
        //FIXME the cached result of get-all does not have the updated value   //FIXED
        Assert.assertEquals(items.size(), 1);
        Assert.assertEquals(items.iterator().next().getValue(), "other");
    }

    @Test
    public void multipleGetAllsWithDeleteInBetween(){

        Item item = itemManager.create(new Item(null, "1"));
        itemManager.getById();

        Assert.assertEquals(accessCountingResettableItemStore.getNumberOfPuts(), 1);
        Assert.assertEquals(accessCountingResettableItemStore.getNumberOfGets(), 1);

        itemManager.delete(item.getId());

        //not changed, because no real internal put is made
        Assert.assertEquals(accessCountingResettableItemStore.getNumberOfDeletes(), 1);

        Set<Item> items = itemManager.getById();
        //FIXME the cached result of get-all does still have the deleted item   //FIXED
        Assert.assertEquals(items.size(), 0);
    }
}
