package com.skronawi.spring.examples.rest.businesslogic.impl;

import com.skronawi.spring.examples.jpa.api.DataPersistence;
import com.skronawi.spring.examples.jpa.api.ModelFactory;
import com.skronawi.spring.examples.rest.businesslogic.api.Data;
import com.skronawi.spring.examples.rest.businesslogic.api.DataBusinessLogic;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class SimpleDataBusinessLogic implements DataBusinessLogic {

    @Autowired
    private DataPersistence dataPersistence;
    @Autowired
    private ModelFactory modelFactory;

    public Data create(Data data) {
        com.skronawi.spring.examples.jpa.api.Data dataToPersist = modelFactory.createData();
        dataToPersist.setData(data.getData());
        return DataMapper.fromPersistence(dataPersistence.create(dataToPersist));
    }

    public Set<Data> getAll() {
        return DataMapper.fromPersistence(dataPersistence.getAll());
    }

    public Data get(String id) {
        return DataMapper.fromPersistence(dataPersistence.get(id));
    }

    public Data update(Data data) {
        com.skronawi.spring.examples.jpa.api.Data existingData = dataPersistence.get(data.getId());
        existingData.setData(data.getData());
        return DataMapper.fromPersistence(dataPersistence.update(existingData));
    }

    public void delete(String id) {
        dataPersistence.delete(id);
    }
}
