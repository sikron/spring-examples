package com.skronawi.spring.examples.valid.custom;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@Aspect
@Component
public class ValidationAspect {

    @Autowired
    private LocalValidatorFactoryBean validator;

    //    @Before("@annotation(com.skronawi.spring.examples.valid.custom.MyValid)") //@annotation is for ElementType.METHOD, not PARAMETER
    @Before("execution(* *(@MyValid (*)))")
    public void validateByAnnotation(JoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();
        Object arg = args[0];

        //FIXME does not use the messages from the bundle, only the explicit specified ones (in code) or default ones
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(arg);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}
