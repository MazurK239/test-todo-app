package com.test.todo;

import com.test.todo.api.TodosController;
import com.test.todo.businesslogic.TodoHandler;

public class BaseTest {

    protected TodosController controller = new TodosController();
    protected TodoHandler todoHandler = new TodoHandler();
}
