package com.staf.testng.listener;

import com.staf.driver.core.DriverManager;
import com.staf.testng.util.TestngUtils;
import lombok.extern.slf4j.Slf4j;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Base suite, test listeners for TestNG configuration. Required to start test creation using TestNG
 */
@Slf4j
public final class SuiteListener implements ISuiteListener, ITestListener {
    @Override
    public void onStart(final ISuite suite) {
        log.info("============ SUITE '{}' IS STARTING ============", suite.getName());
    }

    @Override
    public void onFinish(final ISuite suite) {
        log.info("============ SUITE '{}' FINISHED ============", suite.getName());
    }

    @Override
    public void onTestStart(final ITestResult result) {
        log.info("============ TEST '{}' IS STARTING ============", TestngUtils.generateTestName(result));
    }

    @Override
    public void onTestSuccess(final ITestResult result) {
        log.info("============ TEST '{}' SUCCESSFULLY FINISHED ============", TestngUtils.generateTestName(result));
        onTestFinish();
    }

    @Override
    public void onTestFailure(final ITestResult result) {
        log.info("============ TEST '{}' FAILED ============", TestngUtils.generateTestName(result));
        onTestFinish();
    }

    @Override
    public void onTestSkipped(final ITestResult result) {
        log.info("============ TEST '{}' SKIPPED ============", TestngUtils.generateTestName(result));
        onTestFinish();
    }

    /**
     * Method drives test's lifecycle. It is easier to scale in future in case tests launch by test
     * method.
     */
    private void onTestFinish() {
        DriverManager.getInstance().getCurrentDriver().closeWebDriver();
    }
}
