package com.skronawi.spring.examples.valid.aspect;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ThingManager {

    private Map<String, Thing> things = new HashMap<>();

    public Thing create(@MyValid Thing thing) {
        thing.setId(UUID.randomUUID().toString());
        things.put(thing.getId(), thing);
        return thing;
    }

    public Thing update(@MyValid Thing thing) {
        Thing exisThing = things.get(thing.getId());
        if (exisThing == null) {
            throw new IllegalArgumentException("thing to be updated not existing by id " + thing.getId());
        }
        things.put(thing.getId(), thing);
        return thing;
    }

    public Thing get(String id) {
        return things.get(id);
    }

    public void delete(String id) {
        things.remove(id);
    }
}
