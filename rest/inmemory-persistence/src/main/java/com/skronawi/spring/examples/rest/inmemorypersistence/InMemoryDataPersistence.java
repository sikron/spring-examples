package com.skronawi.spring.examples.rest.inmemorypersistence;

import com.skronawi.spring.examples.jpa.api.DataPersistence;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class InMemoryDataPersistence implements DataPersistence<InMemoryData> {

    private HashMap<String, InMemoryData> datas = new HashMap<String, InMemoryData>();

    public InMemoryData create(InMemoryData data) {
        data.setId(UUID.randomUUID().toString());
        datas.put(data.getId(), data);
        return data;
    }

    public InMemoryData get(String id) {
        return datas.get(id);
    }

    public Set<InMemoryData> getAll() {
        return new HashSet<InMemoryData>(datas.values());
    }

    public InMemoryData update(InMemoryData data) {
        InMemoryData existing = datas.get(data.getId());
        existing.setData(data.getData());
        return data;
    }

    public void delete(String id) {
        datas.remove(id);
    }
}
