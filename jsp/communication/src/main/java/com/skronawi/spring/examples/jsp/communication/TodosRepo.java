package com.skronawi.spring.examples.jsp.communication;

import java.util.*;

public class TodosRepo {

    private Map<String, Todo> todos = new HashMap<String, Todo>();

    public Set<Todo> getAll() {
        return new HashSet<Todo>(todos.values());
    }

    public Todo create(Todo todo) {
        String id = UUID.randomUUID().toString();
        todo.setId(id);
        todos.put(id, todo);
        return todo;
    }

    public Todo update(Todo todo) {
        Todo existingTodo = todos.get(todo.getId());
        if (existingTodo == null) {
            throw new IllegalArgumentException("todo by id not found: " + todo.getId());
        }
        existingTodo.setName(todo.getName());
        existingTodo.setDone(todo.isDone());
        return todo;
    }

    public void delete(String id) {
        todos.remove(id);
    }
}
