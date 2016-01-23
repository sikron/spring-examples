package com.skronawi.spring.examples.jpa.impl;

import com.skronawi.spring.examples.jpa.api.ModelFactory;
import org.springframework.stereotype.Service;

@Service
public class JpaModelFactory implements ModelFactory<JpaData> {

    public JpaData createData() {
        return new JpaData();
    }
}
