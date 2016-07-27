package com.skronawi.spring.examples.valid.mvc;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Date;


@Component("thingValidator")
public class ThingValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return Thing.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        Thing thing = (Thing)o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.thing.name");

        if (thing.getAmount() < 5){
            errors.rejectValue("amount", "Min.thing.amount");
        }
        if (thing.getDueDate() == null){
            errors.rejectValue("dueDate", "NotNull.thing.dueDate");
        }
        if (thing.getDueDate() != null && thing.getDueDate().getTime() < new Date().getTime()){
            errors.rejectValue("dueDate", "Future.thing.dueDate");
        }
        if (thing.getTags() == null){
            errors.rejectValue("tags", "NotNull.thing.tags");
        }
    }
}
