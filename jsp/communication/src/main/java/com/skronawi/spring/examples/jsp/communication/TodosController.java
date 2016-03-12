package com.skronawi.spring.examples.jsp.communication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping(path = "/")
public class TodosController {

    private TodosRepo todosRepo;
    private ViewState viewState;

    @Autowired
    public TodosController(TodosRepo todosRepo, ViewState viewState) {
        this.todosRepo = todosRepo;
        this.viewState = viewState;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getTodos(Model model) {
        populate(model);
        return "todos";
    }

    @RequestMapping(method = RequestMethod.POST, params = {"issue"})
    public String createTodo(Todo todo, Model model) {
        todosRepo.create(todo);
        populate(model);
        return "todos";
    }

    @RequestMapping(method = RequestMethod.POST, params = {"resolve"})
    public String resolveTodo(@RequestParam String id, Model model) {
        Todo todo = todosRepo.get(id);
        todo.setDone(true);
        populate(model);
        return "todos";
    }

    @RequestMapping(method = RequestMethod.POST, params = {"only_open_checkbox"})
    public String showDones(@RequestParam(value = "only_open_checkbox") Long checkBoxValue, Model model) {
        viewState.setShowOnlyOpen(checkBoxValue == 1);
        populate(model);
        return "todos";
    }

    private void populate(Model model) {
        Set<Todo> todos = todosRepo.getAll();
        Set<Todo> todosForModel = new HashSet<Todo>();
        for (Todo todo : todos) {
            if (todo.isDone() != viewState.isShowOnlyOpen()) {
                todosForModel.add(todo);
            }
        }
        model.addAttribute("todos", todosForModel);
        String checkedValue = "";
        if (viewState.isShowOnlyOpen()) {
            checkedValue = "checked";
        }
        model.addAttribute("checked", checkedValue);
    }

    //just for testing
    @PostConstruct
    public void setup() {
        todosRepo.create(new Todo("homework"));
        todosRepo.create(new Todo("dishes"));
        todosRepo.create(new Todo("yarn"));
    }
}
