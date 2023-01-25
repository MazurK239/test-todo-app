package com.test.todo.endpoints;

import com.test.todo.dto.TodoNoTypesDto;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = "endpoints")
public class PostTodoTest extends BaseEndpointsTest {

    @Test(dataProvider = "postParams")
    public void postParamsTest(Object id, Object text, Object completed, int statusCode) {
        Response response = controller.createTodoNoTypes(TodoNoTypesDto.builder()
                .id(id)
                .text(text)
                .completed(completed)
                .build());
        Assert.assertEquals(response.getStatusCode(), statusCode, "The status code is incorrect");
    }

    @DataProvider
    public Object[][] postParams() {
        return new Object[][]{
                // correct params
                {0, "string", false, 201},
                {1, "string", true, 201},
                {12345, "", false, 201},
                {Long.MAX_VALUE, "null", true, 201},
                // incorrect params
                {null, "string", true, 400},
                {1234, null, false, 400},
                {1234, "string", null, 400},
                {-1234, "string", true, 400},
                {"1234", "string", true, 400},
                {1234, 1234, true, 400},
                {1234, "string", "true", 400},
        };
    }
}
