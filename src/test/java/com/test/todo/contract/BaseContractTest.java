package com.test.todo.contract;

import com.test.todo.BaseTest;
import com.test.todo.TestData;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class BaseContractTest extends BaseTest {

    @BeforeTest(groups = "contract")
    public void prepareData() {
        TestData.INSTANCE.generateTestData();
    }

    @AfterTest(groups = "contract")
    public void clearData() {
        TestData.INSTANCE.clearTestData();
    }

}
