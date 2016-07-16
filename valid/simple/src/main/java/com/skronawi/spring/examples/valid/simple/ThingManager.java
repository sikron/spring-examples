package com.skronawi.spring.examples.valid.simple;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ThingManager {

    private Map<String, Thing> things = new HashMap<>();

    public Thing create(Thing thing) {
        thing.setId(UUID.randomUUID().toString());
        things.put(thing.getId(), thing);
        return thing;
    }

    public Thing update(Thing thing) {
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
