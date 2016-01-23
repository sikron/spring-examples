package com.skronawi.spring.examples.rest.inmemorypersistence;

public class InMemoryModelFactory implements com.skronawi.spring.examples.jpa.api.ModelFactory<InMemoryData> {

    public InMemoryData createData() {
        return new InMemoryData();
    }
}
