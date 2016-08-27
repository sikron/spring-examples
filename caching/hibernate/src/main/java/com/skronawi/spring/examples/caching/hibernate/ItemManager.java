package com.skronawi.spring.examples.caching.hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

@Service
public class ItemManager {

    private ItemRepository itemRepository;

    @Autowired
    public ItemManager(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item create(Item item) {
        Item innerItem = new Item(null, item.getValue());
        return itemRepository.save(innerItem);
    }

    public Item get(String id) {
        return itemRepository.findOne(id);
    }

    public Set<Item> getAll() {
        return itemRepository.findAll();
    }

    public void delete(String id) {
        itemRepository.delete(id);
    }

    public Item getNTimes(String id, int times) {
        Item item = null;
        for (int i = 0; i < times; i++) {
            item = itemRepository.findOne(id);
        }
        return item;
    }

    @Transactional
    public Item getNTimesInTransaction(String id, int times) {
        Item item = null;
        for (int i = 0; i < times; i++) {
            item = itemRepository.findOne(id);
        }
        return item;
    }

    public Item update(Item item) {
        Item one = itemRepository.findOne(item.getId());
        if (one == null) {
            throw new IllegalArgumentException("not found by id " + item.getId());
        }
        one.setValue(item.getValue());
        return itemRepository.save(one);
    }

    public Item queryByIdNTimes(String id, int times) {
        Item item = null;
        for (int i = 0; i < times; i++) {
            item = itemRepository.queryById(id);
        }
        return item;
    }

    @Transactional
    public Item queryByIdNTimesInTransaction(String id, int times) {
        Item item = null;
        for (int i = 0; i < times; i++) {
            item = itemRepository.queryById(id);
        }
        return item;
    }

    public Set<Item> queryByValueNTimes(String value, int times) {
        Set<Item> items = new HashSet<>();
        for (int i = 0; i < times; i++) {
            items = itemRepository.queryByValue(value);
        }
        return items;
    }

    @Transactional
    public Set<Item> queryByValueNTimesInTransaction(String value, int times) {
        Set<Item> items = new HashSet<>();
        for (int i = 0; i < times; i++) {
            items = itemRepository.queryByValue(value);
        }
        return items;
    }

    public Item updateNTimes(Item item, int times) {
        for (int i = 0; i < times; i++) {
            item = itemRepository.save(item);
        }
        return item;
    }

    @Transactional
    public Item updateNTimesInTransaction(Item item, int times) {
        for (int i = 0; i < times; i++) {
            item = itemRepository.save(item);
        }
        return item;
    }

    public void deleteNTimes(String id, int times) {
        for (int i = 0; i < times; i++) {
            itemRepository.delete(id);
        }
    }

    @Transactional
    public void deleteNTimesInTransaction(String id, int times) {
        for (int i = 0; i < times; i++) {
            itemRepository.delete(id);
        }
    }

    @Transactional
    public void get2TimesInTransactionAndWaitBetweenAndAssertEqualValues(String id, CountDownLatch waitForLatch,
                                                                         CountDownLatch signalLatch) {
        Item one = itemRepository.findOne(id);
        System.out.println("got the item the first time");
        signalLatch.countDown();
        try {
            waitForLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Item two = itemRepository.findOne(id);
        System.out.println("got the item the second time");
        if (!one.getValue().equals(two.getValue())) {
            throw new IllegalStateException("values differ");
        }
    }

    public Set<Item> findByValueNTimes(String value, int times) {
        Set<Item> items = new HashSet<>();
        for (int i = 0; i < times; i++) {
            items = itemRepository.findByValue(value);
        }
        return items;
    }
}
