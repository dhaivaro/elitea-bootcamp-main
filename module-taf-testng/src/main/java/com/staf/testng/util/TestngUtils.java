package com.staf.testng.util;

import lombok.experimental.UtilityClass;
import org.testng.ITestResult;

@UtilityClass
public class TestngUtils {
    /**
     * Generate name for test based on information from ITestResult
     *
     * @param testResult TestNg test result
     * @return test name
     */
    public static String generateTestName(final ITestResult testResult) {
        return String.format(
                "%s: %s (%s.%s)",
                testResult.getTestContext().getSuite().getName(),
                testResult.getTestContext().getCurrentXmlTest().getName(),
                testResult.getTestClass().getName(),
                testResult.getMethod().getMethodName());
    }
}
