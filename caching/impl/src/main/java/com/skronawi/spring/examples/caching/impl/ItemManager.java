package com.skronawi.spring.examples.caching.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ItemManager {

    /*
    2 caches are used, 1 for the single methods, 1 for the getAll method. this is necessary. e.g. if a getAll() is
    called, the resulting set is cached. if then a create, delete or update is called, the related entry is identified
    by id and the cache can handle it. but the former getAll-result-cache-entry is not affected.

    if then the getAll() is called, it will return the old cached set without the created/deleted/updated item.

    unfortunately the caching of the getAll() is only set-wide and is not fine-granular with respect to the actually
    modified item.
    */

    public static final String ITEM_CACHE_NAME = "item";
    public static final String ITEMS_CACHE_NAME = "item";

    private Store<String, Item> itemStore;

    @Autowired
    public ItemManager(Store<String, Item> itemStore) {
        this.itemStore = itemStore;
    }

    @CachePut(cacheNames = ITEM_CACHE_NAME, key = "#result.id")
    @CacheEvict(cacheNames = ITEMS_CACHE_NAME, allEntries = true)
    public Item create(Item item) {
        Item innerItem = new Item(UUID.randomUUID().toString(), item.getValue());
        itemStore.put(innerItem.getId(), innerItem);
        return new Item(innerItem.getId(), innerItem.getValue());
    }

    @Cacheable(cacheNames = ITEM_CACHE_NAME)
    public Item getById(String id) {
        return itemStore.get(id);
    }

    @Cacheable(cacheNames = ITEMS_CACHE_NAME)
    public Set<Item> getById() {
        return itemStore.get().stream().map(item -> new Item(item.getId(), item.getValue())).collect(Collectors.toSet());
    }

    @CachePut(cacheNames = ITEM_CACHE_NAME, key = "#result.id")
    @CacheEvict(cacheNames = ITEMS_CACHE_NAME, allEntries = true)
    public Item update(Item item) {
        Item existingItem = itemStore.get(item.getId());
        if (existingItem == null) {
            throw new IllegalArgumentException("not existing by id " + item.getId());
        }
        existingItem.setValue(item.getValue());
        return new Item(existingItem.getId(), existingItem.getValue());
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = ITEM_CACHE_NAME),
            @CacheEvict(cacheNames = ITEMS_CACHE_NAME, allEntries = true)
    })
    public void delete(String id) {
        itemStore.delete(id);
    }
}
