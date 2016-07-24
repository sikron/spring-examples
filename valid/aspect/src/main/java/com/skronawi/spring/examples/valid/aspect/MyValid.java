package com.skronawi.spring.examples.valid.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER}) //only on method parameters for now
@Retention(RetentionPolicy.RUNTIME)
public @interface MyValid {
}
