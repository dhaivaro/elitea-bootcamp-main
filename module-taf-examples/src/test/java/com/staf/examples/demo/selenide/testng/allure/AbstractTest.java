package com.staf.examples.demo.selenide.testng.allure;

import static com.staf.common.metadata.ModuleType.ALLURE;
import static com.staf.common.metadata.ModuleType.SELENIDE;
import static com.staf.common.metadata.ModuleType.TESTNG;

import com.codeborne.selenide.Selenide;
import com.staf.common.metadata.Toolset;
import com.staf.examples.demo.selenide.CommonTest;
import com.staf.testng.listener.SuiteListener;
import io.qameta.allure.Attachment;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;

// More examples: https://github.com/allure-framework/allure-java/tree/master/allure-testng
@Slf4j
@Toolset({SELENIDE, ALLURE, TESTNG})
@Listeners({SuiteListener.class})
public abstract class AbstractTest extends CommonTest {
    // some common logic for all tests

    // TODO: think about it's movement to testng package
    //    TODO: it is possible ot organize via TestLifecycleListener (see
    // com.staf.testng.listener.AllureTestNGListener)
    @AfterMethod
    public void afterMethod(ITestResult tr) {
        if (!tr.isSuccess()) {
            log.info("Test failure");
            addImgAttachment();
        }
    }

    @Attachment("Test Failure")
    public byte[] addImgAttachment() {
        log.info("Attempt to take screenshot on test failure");
        return Selenide.screenshot(OutputType.BYTES);
    }
}
