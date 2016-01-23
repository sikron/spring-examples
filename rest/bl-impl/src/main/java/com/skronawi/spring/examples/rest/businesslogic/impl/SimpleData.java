package com.skronawi.spring.examples.rest.businesslogic.impl;

import com.skronawi.spring.examples.rest.businesslogic.api.Data;

public class SimpleData implements Data {

    private String id;
    private String data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
