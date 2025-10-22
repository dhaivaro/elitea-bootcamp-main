package com.staf.kwd.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = { "classpath:features/test-1.feature" },
        glue = {"com.staf.kwd.library.steps" })
public class CucumberTestRunner extends AbstractTestNGCucumberTests{
}
