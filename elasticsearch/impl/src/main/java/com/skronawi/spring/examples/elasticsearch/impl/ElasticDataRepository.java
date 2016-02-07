package com.skronawi.spring.examples.elasticsearch.impl;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ElasticDataRepository extends ElasticsearchCrudRepository<ElasticData, String> {

    //does not work. cannot return Set
//    Set<ElasticData> findAll();
}
