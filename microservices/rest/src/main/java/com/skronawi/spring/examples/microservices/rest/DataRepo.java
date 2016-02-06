package com.skronawi.spring.examples.microservices.rest;

import java.util.ArrayList;
import java.util.Collection;

public class DataRepo {

    private Collection<MicroServiceRestData> datas = new ArrayList<>();

    public void add(MicroServiceRestData data) {
        datas.add(data);
    }

    public Collection<MicroServiceRestData> getAll() {
        return datas;
    }
}
