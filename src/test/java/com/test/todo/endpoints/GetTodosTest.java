package com.test.todo.endpoints;

import com.test.todo.TestData;
import groovy.util.logging.Log4j2;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Log4j2
@Test(groups = "endpoints")
public class GetTodosTest extends BaseEndpointsTest {

    private static int testDataCount;

    @BeforeClass
    public void getTestDataCount() {
        testDataCount = TestData.INSTANCE.getTestData().size();
    }

    @Test(dataProvider = "correctParams")
    public void correctParamsTests(Integer offset, Integer limit, Integer expectedCount) {
        Response response = controller.getTodos(offset, limit);
        Assert.assertEquals(response.statusCode(), 200, "The status code is incorrect");
        Assert.assertEquals(extractTodosList(response).size(), expectedCount,
                "The request with specified limit returned incorrect number of results");
    }

    @Test(dataProvider = "incorrectParams")
    public void incorrectParamsTests(Object offset, Object limit) {
        Response response = controller.getTodos(offset, limit);
        Assert.assertEquals(response.statusCode(), 400, "The status code is incorrect");
    }

    @DataProvider
    public Object[][] correctParams() {
        return new Object[][]{
                {null, null, testDataCount},
                {null, 0, 0},
                {0, null, testDataCount},
                {0, 0, 0},
                {null, 5, Math.min(testDataCount, 5)},
                {0, 10, Math.min(testDataCount, 10)},
                {0, 3, Math.min(testDataCount, 3)},
                {2, 3, Math.min(3, Math.max(testDataCount - 2, 0))},
                {15, 10, Math.min(10, Math.max(testDataCount - 15, 0))},
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
