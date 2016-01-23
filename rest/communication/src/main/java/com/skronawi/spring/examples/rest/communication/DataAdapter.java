package com.skronawi.spring.examples.rest.communication;

import com.skronawi.spring.examples.rest.businesslogic.api.Data;

public class DataAdapter implements Data {

    private final RestData data;

    public DataAdapter(RestData data){
        this.data = data;
    }

    public String getId() {
        return data.getId();
    }

    public void setId(String id) {
        data.setId(id);
    }

    public void setData(String data) {
        this.data.setData(data);
    }

    public String getData() {
        return data.getData();
    }
}
