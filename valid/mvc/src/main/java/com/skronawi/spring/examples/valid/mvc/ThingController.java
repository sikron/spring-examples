package com.skronawi.spring.examples.valid.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping(path = "/")
@RestController
public class ThingController {

    @Autowired
    private ThingManager thingManager;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Thing create(@RequestBody @Valid Thing thing) {
//    public Thing create(@RequestBody @Valid Thing thing, BindingResult bindingResult) {
        /*
        - @Valid only has an effect in RestController, NOT in the ThingManager, ALTHOUGH web environment is started in
          tests.
        - If bindingResult is NOT injected, then just a 400 error is returned without body. the method here is not
          entered.
        - If bindingResult is injected, the method is entered and processed. The method has to handle any errors in
          bindingResult and return an error by itself.
         */
        return thingManager.create(thing);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Thing get(String id) {
        return thingManager.get(id);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMANVE(MethodArgumentNotValidException manve) {
        String response = "";
        List<FieldError> fieldErrors = manve.getBindingResult().getFieldErrors();
        for (FieldError fe : fieldErrors) {
            response += messageSource.getMessage(fe, null) + "\n";
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /*
    ---------------------------------------------------
    Either the annotations on the attributes of Thing are active, then the validating happens
    by default using just the @Valid annotation here. The LocalValidatorFactoryBean will be used
    as validator in this case.

    Or without the annotations a own validator object can be registered, which will be used
    instead. In this case the validator has to be bound with initBinder here.
     */

    @Autowired
    @Qualifier("thingValidator")
    private Validator validator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }
}
