package com.skronawi.spring.examples.caching.hibernate;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.Set;

@Repository
public interface ItemRepository extends CrudRepository<Item, String> {

    Set<Item> findAll();

    @Query("select i from Item i where i.id = :id")
    Item queryById(@Param("id") String id);

    //see e.g. http://forum.spring.io/forum/spring-projects/data/106093-query-caching
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Query("select i from Item i where i.value = :value")
    Item queryByValue(@Param("value") String value);
}
