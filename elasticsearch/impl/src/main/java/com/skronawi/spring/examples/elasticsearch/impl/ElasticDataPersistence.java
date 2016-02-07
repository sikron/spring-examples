package com.skronawi.spring.examples.elasticsearch.impl;

import com.skronawi.spring.examples.jpa.api.DataPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class ElasticDataPersistence implements DataPersistence<ElasticData> {

    @Autowired
    private ElasticDataRepository repository;

    public ElasticData create(ElasticData data) {

        //without hibernate, @GeneratedValue is not available
        data.setId(UUID.randomUUID().toString());
        /*
        also for elasticsearch this upper id is just a attribute. as real id it uses a internal id, which is not
        accessible here.
        */

        ElasticData save = repository.save(data);
        return save;
    }

    public ElasticData get(String id) {
        return repository.findOne(id);
    }

    public Set<ElasticData> getAll() {

        //repository.findAll() cannot be overwritten to return Set<ElasticData>
        Iterable<ElasticData> all = repository.findAll();

        HashSet<ElasticData> elasticDatas = new HashSet<ElasticData>();
        for (ElasticData elasticData : all){
            elasticDatas.add(elasticData);
        }
        return elasticDatas;
    }

    public ElasticData update(ElasticData data) {
        ElasticData existing = repository.findOne(data.getId());
        existing.setData(data.getData());
        return repository.save(existing);
    }

    public void delete(String id) {
        repository.delete(id);
    }
}
