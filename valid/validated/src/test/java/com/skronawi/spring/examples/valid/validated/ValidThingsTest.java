package com.skronawi.spring.examples.valid.validated;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Calendar;

@ContextConfiguration(classes = ValidatedConfig.class)
public class ValidThingsTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ThingManager thingManager;

    @Test
    public void createInvalidThing() throws Exception {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.HOUR, -24);
        Thing thing = new Thing();
        thing.setDueDate(instance.getTime());
        try {
            thingManager.create(thing);
            Assert.fail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
