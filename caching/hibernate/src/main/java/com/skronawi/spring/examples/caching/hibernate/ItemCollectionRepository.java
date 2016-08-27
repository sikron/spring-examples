package com.skronawi.spring.examples.caching.hibernate;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.Set;

@Repository
public interface ItemCollectionRepository extends CrudRepository<ItemCollection, String> {

    ItemCollection findById(String id);

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Set<ItemCollection> findByValue(String value);

    //not cached
    Set<ItemCollection> findByAttribute(String attribute);
}
