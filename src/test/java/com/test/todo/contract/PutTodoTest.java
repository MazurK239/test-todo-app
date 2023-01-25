package com.test.todo.contract;

import com.test.todo.TestData;
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
public class PutTodoTest extends BaseContractTest {

    @Test(dataProvider = "putCorrectParams")
    public void putWithCorrectParamsTest(Object id, Object newId, Object newText, Object newCompleted) {
        Response response = controller.updateTodoNoTypes(id, TodoNoTypesDto.builder()
                .id(newId)
                .text(newText)
                .completed(newCompleted)
                .build());
        Assert.assertEquals(response.getStatusCode(), 200, "The status code is incorrect");
    }

    @Test(dataProvider = "putIncorrectParams")
    public void putWithIncorrectParamsTest(Object id, Object newId, Object newText, Object newCompleted, int statusCode) {
        Response response = controller.updateTodoNoTypes(id, TodoNoTypesDto.builder()
                .id(newId)
                .text(newText)
                .completed(newCompleted)
                .build());
        Assert.assertEquals(response.getStatusCode(), statusCode, "The status code is incorrect");
    }

    @DataProvider
    public Object[][] putCorrectParams() {
        Long validId = TestData.INSTANCE.getTestData().get(0).getId();
        return new Object[][]{
                {validId, validId, "string", true},
                {validId, validId, "", false},
                {Long.MAX_VALUE, Long.MAX_VALUE, "null", true},
        };
    }

    @DataProvider
    public Object[][] putIncorrectParams() {
        Long validId = TestData.INSTANCE.getTestData().get(0).getId();
        return new Object[][]{
                {1010101010, 1, "string", true, 404},
                {validId, null, "string", true, 400},
                {validId, validId, null, false, 400},
                {validId, validId, "string", null, 400},
                {validId, "1234", "string", true, 400},
                {validId, validId, 1234, true, 400},
                {validId, validId, "string", "true", 400},
                // two below cases return 404, although I think that 400 would be better in case id doesn't have proper format in the request
                {null, 1, "string", true, 400},
                {"1234", validId, "string", true, 400},
        };
    }
}
