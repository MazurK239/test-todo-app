package com.test.todo.logic;

import com.test.todo.BaseTest;
import com.test.todo.dto.TodoDto;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Test(groups = "app")
public class AppTest extends BaseTest {

    private List<TodoDto> todosBefore;

    @BeforeMethod
    public void getTodos() {
        todosBefore = todoHandler.getTodos();
    }

    @Test
    public void createTodoCheckThatOnlyOneNewItemIsCreatedTest() {
        // arrange
        TodoDto newTodo = prepareNewTodo();

        // act
        todoHandler.createTodo(newTodo);

        // assert
        checkStatusCode(201);

        List<TodoDto> todosAfter = todoHandler.getTodos();
        Assert.assertEquals(todosAfter.size(), todosBefore.size() + 1,
                "The number of todos is incorrect after the creation of a new item");
    }

    @Test
    public void createTodoCheckCreatedParametersTest() {
        // arrange
        TodoDto newTodo = prepareNewTodo();

        // act
        todoHandler.createTodo(newTodo);

        // assert
        checkStatusCode(201);

        List<TodoDto> todosAfter = todoHandler.getTodos();
        Assert.assertTrue(todosAfter.stream().anyMatch(todo -> todo.equals(newTodo)),
                "A created todo " + newTodo.toString() + " is absent in the list of items");
    }

    @Test
    public void createTodoWithExistingIdTest() {
        // arrange
        TodoDto existingTodo = getExistingTodo();
        TodoDto newTodo = TodoDto.builder().id(existingTodo.getId()).text("new descr").completed(false).build();

        // act
        todoHandler.createTodo(newTodo);

        // assert
        checkStatusCode(400);

        List<TodoDto> todosAfter = todoHandler.getTodos();
        Assert.assertEquals(todosAfter.size(), todosBefore.size(),
                "The number of todos is incorrect after the creation attempt of an invalid item");

        long numberOfTodosWithSameId = todosAfter.stream()
                .filter(todo -> todo.getId().equals(existingTodo.getId()))
                .count();
        Assert.assertEquals(numberOfTodosWithSameId, 1,
                "A created todo " + newTodo.toString() + " is absent in the list of items");
    }

    @Test(dataProvider = "offsetLimitParams")
    public void checkLimitAndOffsetLogicTest(Integer offset, Integer limit, Integer totalCount) {
        // arrange
        createListOfTodos(totalCount);

        // act
        List<TodoDto> todos = todoHandler.getTodos(offset, limit);

        // assert
        checkStatusCode(200);

        int expectedTotalCount = todosBefore.size();
        int expectedOffset = Optional.ofNullable(offset).orElse(0);
        int expectedLimit = Optional.ofNullable(limit).orElse(expectedTotalCount);
        Assert.assertEquals(todos.size(), Math.min(expectedLimit, Math.max(0, expectedTotalCount - expectedOffset)),
                "The number of results is incorrect");
    }

    @Test
    public void updateTodoAndCheckParametersTest() {
        // arrange
        TodoDto todo = getExistingTodo();
        long todoId = todo.getId();

        // act
        todo.setText("updated description at " + System.currentTimeMillis());
        todo.setCompleted(!todo.getCompleted());
        todoHandler.updateTodo(todoId, todo);

        // assert
        checkStatusCode(200);

        List<TodoDto> todosAfter = todoHandler.getTodos();
        Assert.assertEquals(todosAfter.size(), todosBefore.size(), "The number of todos changed after update");

        TodoDto updatedTodo = todoHandler.getTodoById(todoId);
        Assert.assertEquals(updatedTodo, todo, "Some parameters haven't been updated correctly");
    }

    @Test
    public void updateIdTest() {
        // arrange
        TodoDto todo = getExistingTodo();
        long todoId = todo.getId();

        // act
        long newTodoId = getUniqueId();
        todo.setId(newTodoId);
        todoHandler.updateTodo(todoId, todo);

        // assert
        checkStatusCode(200);

        List<TodoDto> todosAfter = todoHandler.getTodos();
        Assert.assertEquals(todosAfter.size(), todosBefore.size(), "The number of todos changed after update");

        TodoDto nullTodo = todoHandler.getTodoById(todoId);
        Assert.assertNull(nullTodo, "Todo with old id is still present");

        TodoDto updatedTodo = todoHandler.getTodoById(newTodoId);
        Assert.assertEquals(updatedTodo, todo, "Some parameters haven't been updated correctly");
    }

    // bug here. The dependency is needed because this bug makes another test fail
    @Test(dependsOnMethods = "updateIdTest")
    public void updateIdSetExistingTest() {
        // arrange
        createListOfTodos(2);
        TodoDto todoToChange = todosBefore.get(0);
        long todoId = todoToChange.getId();
        TodoDto todoWithExistingId = todosBefore.get(1);

        // act
        todoHandler.updateTodo(todoId, TodoDto.builder()
                .id(todoWithExistingId.getId())
                .text(todoToChange.getText())
                .completed(todoToChange.getCompleted())
                .build());

        // assert
        SoftAssert sa = new SoftAssert();
        checkStatusCode(sa, 400);

        List<TodoDto> todosAfter = todoHandler.getTodos();
        Assert.assertEquals(todosAfter.size(), todosBefore.size(), "The number of todos changed after update");

        long numberOfTodosWithSameId = todosAfter.stream()
                .filter(t -> t.getId().equals(todoId))
                .count();
        sa.assertEquals(numberOfTodosWithSameId, 1,
                "An old todo " + todoToChange.toString() + " is absent in the list of items");

        numberOfTodosWithSameId = todosAfter.stream()
                .filter(t -> t.getId().equals(todoWithExistingId.getId()))
                .count();
        sa.assertEquals(numberOfTodosWithSameId, 1,
                "More than one todo with the same id: " + todoWithExistingId.getId());
        sa.assertAll();
    }

    @Test
    public void updateNonexistentTodoTest() {
        // arrange
        long uniqueId = getUniqueId();
        TodoDto todo = TodoDto.newTodo();
        todo.setId(uniqueId);

        // act
        todoHandler.updateTodo(uniqueId, todo);

        // assert
        checkStatusCode(404);

        List<TodoDto> todosAfter = todoHandler.getTodos();
        Assert.assertEquals(todosAfter.size(), todosBefore.size(), "The number of todos changed after update");
        Assert.assertTrue(todosAfter.stream().noneMatch(t -> t.getId().equals(uniqueId)),
                "A todo with id " + uniqueId + " appeared");
    }

    @Test
    public void deleteTodoAndCheckDeletedTest() {
        // arrange
        TodoDto todo = getExistingTodo();

        // act
        todoHandler.deleteTodo(todo.getId());

        // assert
        checkStatusCode(204);

        List<TodoDto> todosAfter = todoHandler.getTodos();
        Assert.assertEquals(todosAfter.size(), todosBefore.size() - 1,
                "The number of todos didn't reduce after deleting a todo");
        Assert.assertFalse(todosAfter.stream().anyMatch(t -> t.getId().equals(todo.getId())),
                "A todo with id " + todo.getId() + " is still present in the list");
    }

    @Test
    public void deleteNonexistentTodoTest() {
        // arrange
        Long uniqueId = getUniqueId();

        // act
        todoHandler.deleteTodo(uniqueId);

        // assert
        checkStatusCode(404);

        List<TodoDto> todosAfter = todoHandler.getTodos();
        Assert.assertEquals(todosAfter.size(), todosBefore.size(),
                "The number of todos changed after deleting a nonexistent todo");
    }

    @DataProvider
    public Object[][] offsetLimitParams() {
        return new Object[][] {
                {null, null, 5},
                {0, null, 5},
                {0, 5, 5},
                {0, 6, 5},
                {0, 4, 5},
                {0, 0, 5},
                {1, 2, 5},
                {3, 0, 5},
                {6, 5, 5},
        };
    }

    private long getUniqueId() {
        Random random = new Random();
        List<Long> ids = todosBefore.stream().map(TodoDto::getId).collect(Collectors.toList());
        long id = Math.abs(random.nextLong());
        while (ids.contains(id)) {
            id = Math.abs(random.nextLong());
        }
        return id;
    }

    private TodoDto prepareNewTodo() {
        TodoDto newTodo = TodoDto.newTodo();
        newTodo.setId(getUniqueId());
        return newTodo;
    }

    private TodoDto getExistingTodo() {
        if (todosBefore.size() >= 1) {
            return todosBefore.get(0);
        }
        TodoDto newTodo = TodoDto.newTodo();
        todoHandler.createTodo(newTodo);
        todosBefore.add(newTodo);
        return newTodo;
    }

    private void createListOfTodos(int count) {
        for (int i = todosBefore.size(); i < count; i++) {
            TodoDto todo = TodoDto.newTodo();
            todoHandler.createTodo(todo);
            todosBefore.add(todo);
        }
    }

}
