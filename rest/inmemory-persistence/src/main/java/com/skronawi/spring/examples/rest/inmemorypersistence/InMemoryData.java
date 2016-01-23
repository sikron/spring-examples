package com.skronawi.spring.examples.rest.inmemorypersistence;

import com.skronawi.spring.examples.jpa.api.Data;

public class InMemoryData implements Data {

    private String id;
    private String data;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
