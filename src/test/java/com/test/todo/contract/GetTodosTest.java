package com.test.todo.contract;

import groovy.util.logging.Log4j2;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * I know that it is possible to merge those two tests into one with a common DataProvider,
 * but I decided to split them intentionally. That way it would be easier to explore those tests
 * in the reporting tool
 */
@Log4j2
@Test(groups = "contract")
public class GetTodosTest extends BaseContractTest {

    @Test(dataProvider = "correctParams")
    public void correctParamsTests(Integer offset, Integer limit) {
        Response response = controller.getTodos(offset, limit);
        Assert.assertEquals(response.statusCode(), 200, "The status code is incorrect");
    }

    @Test(dataProvider = "incorrectParams")
    public void incorrectParamsTests(Object offset, Object limit) {
        Response response = controller.getTodos(offset, limit);
        Assert.assertEquals(response.statusCode(), 400, "The status code is incorrect");
    }

    @DataProvider
    public Object[][] correctParams() {
        return new Object[][]{
                {null, null},
                {null, 0},
                {0, null},
                {0, 0},
                {null, 5},
                {0, 10},
                {0, 3},
                {2, 3},
                {15, 10},
        };
    }

    @DataProvider
    public Object[][] incorrectParams() {
        return new Object[][]{
                {-1, 0},
                {-1, null},
                {0, -1},
                {null, -1},
                {"a", null},
                {null, "b"}
        };
    }

}
