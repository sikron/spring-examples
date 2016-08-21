package com.skronawi.spring.examples.caching.hibernate;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ItemRepository extends CrudRepository<Item, String> {

    Set<Item> findAll();
}
