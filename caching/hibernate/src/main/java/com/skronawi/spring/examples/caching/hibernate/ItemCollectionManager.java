package com.skronawi.spring.examples.caching.hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ItemCollectionManager {

    private ItemCollectionRepository repository;

    @Autowired
    public ItemCollectionManager(ItemCollectionRepository repository) {
        this.repository = repository;
    }

    public ItemCollection create(ItemCollection itemCollection) {
        return repository.save(itemCollection);
    }

    public ItemCollection getById(String id) {
        return repository.findOne(id);
    }

    public ItemCollection getByIdCached(String id) {
        return repository.findById(id);
    }

    public Set<ItemCollection> findByValueCached(String value){
        return repository.findByValue(value);
    }

    public Set<ItemCollection> findByAttributeNotCached(String attribute){
        return repository.findByAttribute(attribute);
    }
}
