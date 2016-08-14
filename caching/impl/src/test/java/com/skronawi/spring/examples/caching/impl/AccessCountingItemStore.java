package com.skronawi.spring.examples.caching.impl;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AccessCountingItemStore implements Store<String, Item> {

    private Map<String, Item> items = new HashMap<>();
    private int numberOfPuts = 0;
    private int numberOfGetsById = 0;
    private int numberOfDeletes = 0;
    private int numberOfGets = 0;

    @Override
    public void put(String key, Item value) {
        numberOfPuts++;
        items.put(key, value);
    }

    @Override
    public Item get(String key) {
        numberOfGetsById++;
        return items.get(key);
    }

    @Override
    public void delete(String key) {
        numberOfDeletes++;
        items.remove(key);
    }

    @Override
    public Set<Item> get() {
        numberOfGets++;
        return items.values().stream().collect(Collectors.toSet());
    }

    public int getNumberOfPuts() {
        return numberOfPuts;
    }

    public int getNumberOfGetsById() {
        return numberOfGetsById;
    }

    public int getNumberOfDeletes() {
        return numberOfDeletes;
    }

    public int getNumberOfGets() {
        return numberOfGets;
    }

    public void reset() {
        items = new HashMap<>();
        numberOfPuts = 0;
        numberOfGetsById = 0;
        numberOfDeletes = 0;
        numberOfGets = 0;
    }
}
