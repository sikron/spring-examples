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

    public Item queryByIdNTimes(String id, int times){
        Item item = null;
        for (int i = 0; i < times; i++) {
            item = itemRepository.queryById(id);
        }
        return item;
    }

    @Transactional
    public Item queryByIdNTimesInTransaction(String id, int times){
        Item item = null;
        for (int i = 0; i < times; i++) {
            item = itemRepository.queryById(id);
        }
        return item;
    }

    public Item queryByValueNTimes(String value, int times){
        Item item = null;
        for (int i = 0; i < times; i++) {
            item = itemRepository.queryByValue(value);
        }
        return item;
    }

    @Transactional
    public Item queryByValueNTimesInTransaction(String value, int times){
        Item item = null;
        for (int i = 0; i < times; i++) {
            item = itemRepository.queryByValue(value);
        }
        return item;
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
}
