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
        todo.setCreationDate(new Date());
        todos.put(id, todo);
        return todo;
    }

    public Todo update(Todo todo) {
        Todo existingTodo = get(todo.getId());
        existingTodo.setIssue(todo.getIssue());
        existingTodo.setDone(todo.isDone());
        return todo;
    }

    public void delete(String id) {
        todos.remove(id);
    }

    public Todo get(String id) {
        Todo todo = todos.get(id);
        if (todo == null) {
            throw new IllegalArgumentException("todo by id not found: " + id);
        }
        return todo;
    }
}
