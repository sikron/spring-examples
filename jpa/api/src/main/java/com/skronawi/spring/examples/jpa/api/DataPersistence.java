package com.skronawi.spring.examples.jpa.api;

import java.util.Set;

public interface DataPersistence<T extends Data>  {

    T create(T data);

    T get(String id);

    Set<T> getAll();

    T update(T data);

    void delete(String id);

}
