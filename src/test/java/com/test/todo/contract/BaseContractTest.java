package com.test.todo.contract;

import com.test.todo.BaseTest;
import com.test.todo.TestData;
import com.test.todo.dto.TodoDto;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.util.List;

public class BaseContractTest extends BaseTest {

    @BeforeTest(groups = "contract")
    public void prepareData() {
        TestData.INSTANCE.generateTestData();
    }

    @AfterTest(groups = "contract")
    public void clearData() {
        TestData.INSTANCE.clearTestData();
    }

    protected List<TodoDto> extractTodosList(Response response) {
        return response.as(new TypeRef<>() {});
    }

}
