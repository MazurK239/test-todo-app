package com.test.todo;

import com.test.todo.dto.TodoDto;
import com.test.todo.dto.TodoNoTypesDto;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TodosController {

    // This is a temp solution to avoid storing this sensitive data inside the code.
    // Potentially this should be used with some secrets manager
    private static final String USERNAME = System.getProperty("username");
    private static final String PASSWORD = System.getProperty("password");
    private static final String AUTH = System.getProperty("auth");

    private final RequestSpecification rs = given()
            .baseUri("http://localhost:8080")
            .basePath("/todos")
            .contentType("application/json");

    private final RequestSpecification rsWithAuth = rs
            // Normally I would do like this, but for some reason this gives me 401 and header works as expected
            // .auth().basic(USERNAME, PASSWORD);
            .headers("Authorization", "Basic " + AUTH);

    public Response createTodo(TodoDto todo) {
        return given(rs).body(todo).post();
    }

    public Response createTodoNoTypes(TodoNoTypesDto todo) {
        return given(rs).body(todo).post();
    }

    public Response getTodos() {
        return getTodos(null, null);
    }

    public Response getTodos(Object offset, Object limit) {
        Map<String, Object> queryParams = new HashMap<>();
        if (offset != null) queryParams.put("offset", offset);
        if (limit != null) queryParams.put("limit", limit);
        return given(rs).queryParams(queryParams).get();
    }

    public Response updateTodo(Long id, TodoDto updatedTodo) {
        return given(rs).body(updatedTodo).put("/" + id);
    }

    public Response updateTodoNoTypes(Object id, TodoNoTypesDto updatedTodo) {
        return given(rs).body(updatedTodo).put("/" + id);
    }

    public Response deleteTodo(Long id) {
        return given(rsWithAuth).delete("/" + id);
    }

    public Response deleteTodoNoTypes(Object id) {
        return given(rsWithAuth).delete("/" + id);
    }
}
