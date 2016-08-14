package com.skronawi.spring.examples.caching.impl;

import java.util.Set;

public interface Store<K, V> {

    void put(K key, V value);

    V get(K key);

    void delete(K key);

    Set<V> get();

}
