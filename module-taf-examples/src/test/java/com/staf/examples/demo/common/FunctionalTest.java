package com.staf.examples.demo.common;

import com.epam.reportportal.testng.ReportPortalTestNGListener;
import com.staf.testng.listener.SuiteListener;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({SuiteListener.class, ReportPortalTestNGListener.class})
public class FunctionalTest {

    private static int counter = 0;

    @Test(description = "TestNG Retry test")
    public void retryTest() {
        Assertions.assertThat(counter++)
                .as("Incorrect counter value")
                .isEqualTo(1);
    }

}
