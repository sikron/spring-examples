package com.skronawi.spring.examples.caching.hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class ItemManager {

    private ItemRepository itemRepository;

    @Autowired
    public ItemManager(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item create(Item item) {
        Item innerItem = new Item(UUID.randomUUID().toString(), item.getValue());
        return itemRepository.save(innerItem);
    }

    public Item get(String id) {
        return itemRepository.findOne(id);
    }

    public Set<Item> getAll() {
        return itemRepository.findAll();
    }

    public void delete(String id){
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
        if (one == null){
            throw new IllegalArgumentException("not found by id " + item.getId());
        }
        one.setValue(item.getValue());
        return itemRepository.save(one);
    }
}
