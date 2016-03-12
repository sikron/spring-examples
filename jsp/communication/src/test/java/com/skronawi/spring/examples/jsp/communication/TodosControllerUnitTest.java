package com.skronawi.spring.examples.jsp.communication;

import org.springframework.ui.ExtendedModelMap;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TodosControllerUnitTest {

    @Test
    public void testHomePage() throws Exception {
        TodosController controller = new TodosController(new TodosRepo(), new ViewState());
        Assert.assertEquals(controller.getTodos(new ExtendedModelMap()), "todos");
    }
}
