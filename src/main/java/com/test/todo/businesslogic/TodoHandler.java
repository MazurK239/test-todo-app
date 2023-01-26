package com.test.todo.businesslogic;

import com.test.todo.api.TodosController;
import com.test.todo.dto.TodoDto;
import io.restassured.common.mapper.TypeRef;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class TodoHandler {

    private final TodosController controller = new TodosController();

    public List<TodoDto> getTodos() {
        return getTodos(null, null);
    }

    public List<TodoDto> getTodos(Integer offset, Integer limit) {
        log.info("Getting a list of todos using offset {} and limit {}", offset, limit);
        return controller.getTodos(offset, limit).as(new TypeRef<>() {});
    }

    public TodoDto getTodoById(Long id) {
        return getTodos().stream()
                .filter(todo -> todo.getId().equals(id))
                .findAny().orElse(null);
    }

    public void createTodo(TodoDto todo) {
        log.info("Creating a todo: {}", todo.toString());
        controller.createTodo(todo);
    }

    public void updateTodo(Long id, TodoDto newTodo) {
        log.info("Updating a todo with id {}. New parameters are {}", id, newTodo.toString());
        controller.updateTodo(id, newTodo);
    }

    public void deleteTodo(Long id) {
        log.info("Deleting a todo with id {}", id);
        controller.deleteTodo(id);
    }
}
