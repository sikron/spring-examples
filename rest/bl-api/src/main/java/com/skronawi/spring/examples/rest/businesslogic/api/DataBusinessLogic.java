package com.skronawi.spring.examples.rest.businesslogic.api;

import java.util.Set;

public interface DataBusinessLogic {

    Data create(Data data);

    Set<Data> getAll();

    Data get(String id);

    Data update(Data data);

    void delete(String id);
}
