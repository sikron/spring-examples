package com.skronawi.spring.examples.valid.mvc;

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
    //@NotBlank
    //@NotEmpty
    private String name;

    //from javax
    //@Min(value = 5, message = "a value of 5 is minimum, '${validatedValue}' is to small")
    private int amount;

    //@NotNull
    //@Future
    private Date dueDate;

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
