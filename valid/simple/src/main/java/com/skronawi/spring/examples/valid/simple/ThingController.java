package com.skronawi.spring.examples.valid.simple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping(path = "/")
@RestController
public class ThingController {

    @Autowired
    private ThingManager thingManager;

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
}
