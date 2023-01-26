package com.test.todo.api;

import com.test.todo.dto.ITodo;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TodosController {

    // This is a temp solution to avoid storing this sensitive data inside the code.
    // Potentially this should be used with some secrets manager
    private static final String USERNAME = System.getProperty("username");
    private static final String PASSWORD = System.getProperty("password");
    private static final String AUTH = System.getProperty("auth");

    @Getter
    private static int lastStatusCode;

    private final RequestSpecification rs = given()
            .baseUri("http://localhost:8080")
            .basePath("/todos")
            .contentType("application/json");

    private final RequestSpecification rsWithAuth = rs
            // Normally I would do like this, but for some reason this gives me 401 and header works as expected
            // .auth().basic(USERNAME, PASSWORD);
            .headers("Authorization", "Basic " + AUTH);

    public Response getTodos(Object offset, Object limit) {
        Map<String, Object> queryParams = new HashMap<>();
        if (offset != null) queryParams.put("offset", offset);
        if (limit != null) queryParams.put("limit", limit);
        Response response = given(rs).queryParams(queryParams).get().then().log().ifError().extract().response();
        lastStatusCode = response.getStatusCode();
        return response;
    }

    public Response createTodo(ITodo todo) {
        Response response = given(rs).body(todo).post().then().log().ifError().extract().response();
        lastStatusCode = response.getStatusCode();
        return response;
    }

    public Response updateTodo(Object id, ITodo updatedTodo) {
        Response response = given(rs).body(updatedTodo).put("/" + id).then().log().ifError().extract().response();
        lastStatusCode = response.getStatusCode();
        return response;
    }

    public Response deleteTodo(Object id) {
        Response response = given(rsWithAuth).delete("/" + id).then().log().ifError().extract().response();
        lastStatusCode = response.getStatusCode();
        return response;
    }
}
