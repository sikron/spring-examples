package com.skronawi.spring.examples.valid.custom;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

/**
 * inspired by http://sdqali.in/blog/2015/12/04/validating-requestparams-and-pathvariables-in-spring-mvc/
 */
@Aspect
@Component
public class ValidationAspect {

    @Autowired
    private LocalValidatorFactoryBean validator;

    @Autowired
    private MessageSource messageSource;

    //@Before("@annotation(com.skronawi.spring.examples.valid.custom.MyValid)") //@annotation is for ElementType.METHOD, not PARAMETER
    @Before("execution(* *(@MyValid (*)))")
    public void validateByAnnotation(JoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();
        Object arg = args[0];

//        //FIXME does not use the messages from the bundle, only the explicit specified ones (in code) or default ones
//        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(arg);
//        if (!constraintViolations.isEmpty()) {
//            throw new ConstraintViolationException(constraintViolations);
//        }

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(arg, "thing");
        validator.validate(arg, bindingResult);
        if (!bindingResult.getFieldErrors().isEmpty()) {
            Map<String,List<String>> violationsPerField = new HashMap<>();
            for (FieldError fe : bindingResult.getFieldErrors()){
                violationsPerField.putIfAbsent(fe.getField(), new ArrayList<>());
                List<String> fieldViolations = violationsPerField.get(fe.getField());
                fieldViolations.add(messageSource.getMessage(fe, null)); //FIXME so the messages directly on the annotations are not used
            }
            throw new MyConstraintViolationException(violationsPerField);
        }
    }
}
