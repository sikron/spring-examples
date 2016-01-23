package com.skronawi.spring.examples.jpa.impl;

import com.skronawi.spring.examples.jpa.api.DataPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class JpaDataPersistence implements DataPersistence<JpaData> {

    @Autowired
    private JpaDataRepository repository;

    public JpaData create(JpaData data) {
        return repository.save(data);
    }

    public JpaData get(String id) {
        return repository.findOne(id);
    }

    public Set<JpaData> getAll() {
        return repository.findAll();
    }

    @Transactional
    public JpaData update(JpaData data) {
        JpaData existing = repository.findOne(data.getId());
        existing.setData(data.getData());
        return repository.save(existing);
    }

    public void delete(String id) {
        repository.delete(id);
    }
}
