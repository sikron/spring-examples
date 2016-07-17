package com.skronawi.spring.examples.valid.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@ContextConfiguration(classes = CustomValidConfig.class)
public class ValidThingsTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ThingManager thingManager;

    @Test(expectedExceptions = ConstraintViolationException.class)
    public void createInvalidThing() throws Exception {
        thingManager.create(new Thing());
    }

    @Test
    public void getWithoutValid() throws Exception {
        thingManager.get("2");
    }

    @Test
    public void createValidThing() throws Exception {
        Thing thing = new Thing();
        thing.setName("simon");
        thing.setAmount(6);
        thing.setDueDate(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)));
        thingManager.create(thing);
    }
}
