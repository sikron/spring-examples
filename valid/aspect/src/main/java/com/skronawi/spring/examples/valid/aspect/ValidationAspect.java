package com.skronawi.spring.examples.valid.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

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

    //@Before("@annotation(com.skronawi.spring.examples.valid.aspect.MyValid)") //@annotation is for ElementType.METHOD, not PARAMETER
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

                //if key for "fe" is existing in the my-messages, this value is used, otherwise the annotations default value is used
                //without specific locale the system's locale is used, which is "en" on my system. but there is no "en" properties, so the default one is used
                String message = messageSource.getMessage(fe, null);
                //e.g. for @NotEmpty the default message is "may not be empty", which does not state the field, so adapt it
                if (message.equals(fe.getDefaultMessage())){
                    message = fe.getField() + ": " + message;
                }

                fieldViolations.add(message); //FIXME the messages directly on the annotations are not used
            }
            throw new MyConstraintViolationException(violationsPerField);
        }
    }
}
