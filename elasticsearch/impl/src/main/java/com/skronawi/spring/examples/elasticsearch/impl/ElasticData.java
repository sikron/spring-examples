package com.skronawi.spring.examples.elasticsearch.impl;

import com.skronawi.spring.examples.jpa.api.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "example", type = "data")
public class ElasticData implements Data {

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
