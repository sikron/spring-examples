package com.skronawi.spring.examples.caching.impls;

import java.util.Set;

public interface Store<K, V> {

    void put(K key, V value);

    V get(K key);

    void delete(K key);

    Set<V> get();

}
