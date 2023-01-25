package com.test.todo.contract;

import com.test.todo.dto.TodoNoTypesDto;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * I know that it is possible to merge those two tests into one with a common DataProvider,
 * but I decided to split them intentionally. That way it would be easier to explore those tests
 * in the reporting tool
 */
@Test(groups = "contract")
public class PostTodoTest extends BaseContractTest {

    @Test(dataProvider = "postCorrectParams")
    public void postWithCorrectParamsTest(Object id, Object text, Object completed) {
        Response response = controller.createTodoNoTypes(TodoNoTypesDto.builder()
                .id(id)
                .text(text)
                .completed(completed)
                .build());
        Assert.assertEquals(response.getStatusCode(), 201, "The status code is incorrect");
    }

    @Test(dataProvider = "postIncorrectParams")
    public void postWithIncorrectParamsTest(Object id, Object text, Object completed) {
        Response response = controller.createTodoNoTypes(TodoNoTypesDto.builder()
                .id(id)
                .text(text)
                .completed(completed)
                .build());
        Assert.assertEquals(response.getStatusCode(), 400, "The status code is incorrect");
    }

    @DataProvider
    public Object[][] postCorrectParams() {
        return new Object[][]{
                {0, "string", false},
                {1, "string", true},
                {12345678, "", false},
                {Long.MAX_VALUE, "null", true},
        };
    }

    @DataProvider
    public Object[][] postIncorrectParams() {
        return new Object[][]{
                {null, "string", true},
                {1234, null, false},
                {1234, "string", null},
                {-1234, "string", true},
                {"1234", "string", true},
                {1234, 1234, true},
                {1234, "string", "true"},
        };
    }
}
