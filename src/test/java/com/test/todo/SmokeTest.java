package com.test.todo;

import com.test.todo.dto.TodoDto;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SmokeTest extends BaseTest {

    private Long testTodoId;

    @Test
    public void postTest() {
        TodoDto todo = TodoDto.newTodo();
        testTodoId = todo.getId();
        Response response = controller.createTodo(todo);
        Assert.assertEquals(response.getStatusCode(), 201, "The status code is incorrect");
    }

    @Test(dependsOnMethods = "postTest")
    public void getTest() {
        Response response = controller.getTodos();
        Assert.assertEquals(response.getStatusCode(), 200, "The status code is incorrect");
    }

    @Test(dependsOnMethods = "postTest")
    public void updateTest() {
        Response response = controller.updateTodo(testTodoId, TodoDto.builder().id(testTodoId).text("updated").completed(true).build());
        Assert.assertEquals(response.getStatusCode(), 200, "The status code is incorrect");
    }

    @Test(dependsOnMethods = {"updateTest", "getTest"})
    public void deleteTest() {
        Response response = controller.deleteTodo(testTodoId);
        Assert.assertEquals(response.getStatusCode(), 204, "The status code is incorrect");
    }
}
