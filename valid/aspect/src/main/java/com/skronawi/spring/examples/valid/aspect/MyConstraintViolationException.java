package com.skronawi.spring.examples.valid.aspect;

import java.util.List;
import java.util.Map;

public class MyConstraintViolationException extends RuntimeException{

    private final Map<String, List<String>> violationsPerField;

    public MyConstraintViolationException(Map<String, List<String>> violationsPerField) {
        this.violationsPerField = violationsPerField;
    }
}
