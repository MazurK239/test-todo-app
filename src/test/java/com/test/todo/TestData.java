package com.test.todo;

import com.test.todo.dto.TodoDto;
import io.restassured.common.mapper.TypeRef;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public enum TestData {

    INSTANCE;

    @Getter
    public List<TodoDto> testData = new ArrayList<>();

    private final TodosController controller = new TodosController();

    public void clearTestData() {
        log.info("Clearing test data");
        List<TodoDto> todos = controller.getTodos().as(new TypeRef<>() {});
        todos.forEach(todo -> controller.deleteTodo(todo.getId()));
        testData.clear();
    }

    public void generateTestData() {
        log.info("Generating test data");
        for (int i = 0; i < 5; i++) {
            TodoDto todo = TodoDto.newTodo();
            todo.setText("Test todo " + i);
            testData.add(todo);
            controller.createTodo(todo);
        }
    }
}
