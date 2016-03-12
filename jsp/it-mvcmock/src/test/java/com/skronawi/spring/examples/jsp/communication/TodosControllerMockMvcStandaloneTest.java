package com.skronawi.spring.examples.jsp.communication;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class TodosControllerMockMvcStandaloneTest {

    private MockMvc mockMvc;
    private TodosRepo mockedTodosRepo;
    private ViewState mockedViewState;

    @BeforeClass
    public void setup() {
        mockedTodosRepo = getMockedTodosRepo();
        mockedViewState = getMockedViewState();
        TodosController controller = new TodosController(mockedTodosRepo, mockedViewState);
        mockMvc = standaloneSetup(controller).build();
    }

    private ViewState getMockedViewState() {
        return new ViewState();
    }

    private TodosRepo getMockedTodosRepo() {
        TodosRepo todosRepo = new TodosRepo();
        todosRepo.create(new Todo("a"));
        todosRepo.create(new Todo("b"));
        todosRepo.create(new Todo("c"));
        return todosRepo;
    }

    @Test
    public void testTodosViewAndModel() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(view().name("todos"))
                .andExpect(model().attributeExists("todos"))
                .andExpect(model().attribute("todos", new TodosMatcher(mockedTodosRepo.getAll())))
        ;
    }

    private class TodosMatcher extends BaseMatcher<Set<Todo>> {

        private final Set<Todo> expectedTodos;

        public TodosMatcher(Set<Todo> expectedTodos) {
            this.expectedTodos = expectedTodos;
        }

        public boolean matches(Object o) {
            Set<Todo> actualTodos = (Set<Todo>) o;
            for (Todo expected : this.expectedTodos) {
                boolean matched = false;
                for (Todo actualTodo : actualTodos) {
                    if (actualTodo.getIssue().equals(expected.getIssue())
                            && actualTodo.getId().equals(expected.getId())
                            && actualTodo.isDone() == expected.isDone()) {
                        matched = true;
                    }
                }
                if (!matched) {
                    return false;
                }
            }
            return true;
        }

        public void describeTo(Description description) {
        }
    }
}