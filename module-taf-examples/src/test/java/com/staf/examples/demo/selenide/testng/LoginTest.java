package com.staf.examples.demo.selenide.testng;

import static com.staf.common.metadata.ModuleType.SELENIDE;
import static com.staf.common.metadata.ModuleType.TESTNG;

import com.staf.common.metadata.Toolset;
import org.testng.annotations.Test;

@Toolset({SELENIDE, TESTNG})
public class LoginTest extends AbstractTest {
    @Test
    public void simpleLogTest() {
        super.simpleLogTest();
    }

    @Test
    public void loginTest() {
        super.loginTest();
    }

    @Test
    public void loginFragmentTest() {
        super.loginFragmentTest();
    }

    @Test
    public void loginStepTest() {
        super.loginStepTest();
    }

    @Test(enabled = false)
    public void failedTest() {
        super.failedTest();
    }

    @Test
    public void gridItemsTest() {
        super.gridItemsTest();
    }
}
