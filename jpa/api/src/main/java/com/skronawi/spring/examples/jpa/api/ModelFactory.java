package com.skronawi.spring.examples.jpa.api;

public interface ModelFactory<T extends Data> {

    T createData();
}
