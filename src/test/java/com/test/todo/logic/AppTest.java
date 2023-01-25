package com.test.todo.logic;

import com.test.todo.BaseTest;
import com.test.todo.dto.TodoDto;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@Test(groups = "app")
public class AppTest extends BaseTest {

    @Test
    public void createTodoCheckThatOnlyOneNewItemIsCreatedTest() {
        // arrange
        TodoDto newTodo = TodoDto.newTodo();
        List<TodoDto> todosBefore = todoHandler.getTodos();
        if (todosBefore.stream().anyMatch(todo -> todo.getId().equals(newTodo.getId()))) {
            todoHandler.deleteTodo(newTodo.getId());
            todosBefore = todoHandler.getTodos();
        }

        // act
        todoHandler.createTodo(newTodo);

        // assert
        List<TodoDto> todosAfter = todoHandler.getTodos();
        Assert.assertEquals(todosAfter.size(), todosBefore.size() + 1,
                "The number of todos is incorrect after the creation of a new item");
        Assert.assertTrue(todosAfter.stream().anyMatch(todo -> todo.equals(newTodo)),
                "A created todo " + newTodo.toString() + " is absent in the list of items");
    }

    @Test
    public void createTodoCheckCreatedParametersTest() {

    }

    @Test
    public void createTodoWithExistingIdTest() {

    }

    @Test
    public void checkLimitAndOffsetLogicTest() {

    }

    @Test
    public void updateTodoAndCheckParametersTest() {

    }

    @Test
    public void updateIdTest() {

    }

    @Test
    public void updateIdSetExistingTest() {

    }

    @Test
    public void updateNonexistentTodoTest() {

    }

    @Test
    public void deleteTodoAndCheckDeletedTest() {

    }

    @Test
    public void deleteNonexistentTodoTest() {

    }

}
