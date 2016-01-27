package com.skronawi.spring.examples.elasticsearch.impl;

import com.skronawi.spring.examples.jpa.api.ModelFactory;
import org.springframework.stereotype.Service;

@Service
public class ElasticModelFactory implements ModelFactory<JpaData> {

    public JpaData createData() {
        return new JpaData();
    }
}
