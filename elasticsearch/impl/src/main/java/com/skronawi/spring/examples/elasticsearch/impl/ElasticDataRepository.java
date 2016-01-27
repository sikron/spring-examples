package com.skronawi.spring.examples.elasticsearch.impl;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElasticDataRepository extends ElasticsearchCrudRepository<ElasticData, String> {

}
