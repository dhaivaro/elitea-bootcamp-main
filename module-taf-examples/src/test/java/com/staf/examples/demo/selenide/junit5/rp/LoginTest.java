package com.staf.examples.demo.selenide.junit5.rp;

import static com.staf.common.metadata.ModuleType.JUNIT5;
import static com.staf.common.metadata.ModuleType.RP;
import static com.staf.common.metadata.ModuleType.SELENIDE;

import com.staf.common.metadata.Toolset;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
@Toolset({SELENIDE, JUNIT5, RP})
public class LoginTest extends AbstractTest {
    @Test
    @DisplayName("Simple log test")
    public void simpleLogTest() {
        super.simpleLogTest();
    }

    @Test
    @DisplayName("Login test")
    public void loginTest() {
        super.loginTest();
    }

    @Test
    @DisplayName("Login fragment test")
    public void loginFragmentTest() {
        super.loginFragmentTest();
    }

    @Test
    @Disabled
    @DisplayName("Failed Login test")
    public void failedTest() {
        super.failedTest();
    }

    @Test
    public void loginStepTest() {
        super.loginStepTest();
    }

    @Test
    public void gridItemsTest() {
        super.gridItemsTest();
    }
}
