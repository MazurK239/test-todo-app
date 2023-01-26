package com.test.todo;

import com.test.todo.api.TodosController;
import com.test.todo.businesslogic.TodoHandler;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class BaseTest {

    protected TodosController controller = new TodosController();
    protected TodoHandler todoHandler = new TodoHandler();

    protected void checkStatusCode(int expectedStatusCode) {
        Assert.assertEquals(controller.getLastStatusCode(), expectedStatusCode, "The status code is incorrect");
    }

    protected void checkStatusCode(SoftAssert sa, int expectedStatusCode) {
        sa.assertEquals(controller.getLastStatusCode(), expectedStatusCode, "The status code is incorrect");
    }
}
