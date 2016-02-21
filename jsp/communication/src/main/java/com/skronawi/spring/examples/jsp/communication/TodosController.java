package com.skronawi.spring.examples.jsp.communication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;

@Controller
@RequestMapping(path = "/")
public class TodosController {

    private TodosRepo todosRepo;

    @Autowired
    public TodosController(TodosRepo todosRepo) {
        this.todosRepo = todosRepo;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String todos(Model model) {
        model.addAttribute("todos", todosRepo.getAll());
        return "todos";
    }

    //just for testing
    @PostConstruct
    public void setup(){
        todosRepo.create(new Todo("homework"));
        todosRepo.create(new Todo("dishes"));
        todosRepo.create(new Todo("yarn"));
    }
}
