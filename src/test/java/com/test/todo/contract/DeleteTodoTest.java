package com.test.todo.contract;

import com.test.todo.TestData;
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
public class DeleteTodoTest extends BaseContractTest {

    @Test
    public void deleteWithCorrectParamsTest() {
        Long validId = TestData.INSTANCE.getTestData().get(0).getId();
        Response response = controller.deleteTodo(validId);
        Assert.assertEquals(response.getStatusCode(), 204, "The status code is incorrect");
    }

    @Test(dataProvider = "putIncorrectParams")
    public void deleteWithIncorrectParamsTest(Object id, int statusCode) {
        Response response = controller.deleteTodo(id);
        Assert.assertEquals(response.getStatusCode(), statusCode, "The status code is incorrect");
    }


    @DataProvider
    public Object[][] putIncorrectParams() {
        return new Object[][]{
                // two below cases return 404, although I think that 400 would be better in case id doesn't have proper format in the request
                {null, 400},
                {"1234", 400},
        };
    }
}
