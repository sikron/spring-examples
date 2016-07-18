package com.skronawi.spring.examples.valid.custom;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class Thing {

    private String id;

    //hibernate validators, not the javax stuff
    @NotBlank //use default message
    @NotEmpty(message = "{NotEmpty.thing.name}") //use message from my-messages.properties //FIXME shouldn't this work automatically without specifying the key here?
    private String name;

    //from javax
    //use explicit message here; try interpolated values
    @Min(value = 5, message = "a value of {min} is minimum, '${validatedValue}' is to small")
    private int amount;

    @NotNull
    //use explicit message here; try more complex interpolated values, e.g. with formatting
    @Future(message = "The value \"${formatter.format('%1$tY-%1$tm-%1$td', validatedValue)}\" is not in future!")
    private Date dueDate;

    @NotNull
    private List<String> tags;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
