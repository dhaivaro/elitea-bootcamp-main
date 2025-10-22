package com.staf.testng.listener;

import com.staf.driver.core.DriverManager;
import io.qameta.allure.Attachment;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;

/**
 * Listener for proper interaction with test lifecycle NOTE: it requires to put file into resources
 * folder (META-INF/services/io.qameta.allure.listener.TestLifecycleListener)
 */
@Slf4j
public final class AllureTestNgListener implements TestLifecycleListener {
    @Override
    public void beforeTestStop(final TestResult result) {
        if (result.getStatus() == Status.FAILED || result.getStatus() == Status.BROKEN) {
            addImgAttachment();
        }
    }

    @Attachment("Test Failure")
    public byte[] addImgAttachment() {
        log.info("Attempt to take screenshot on test failure");
        return DriverManager.getInstance().getCurrentDriver().screenshot(OutputType.BYTES);
    }
}
