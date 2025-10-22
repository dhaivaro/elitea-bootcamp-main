package com.staf.examples.demo.bdd.runner;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CucumberOptions(
        features = "src/test/java/com/staf/examples/demo/bdd/features/EliteaTest.feature",
        glue = "com.staf.examples.demo.bdd.steps",
        plugin = { "pretty", "html:target/cucumber-reports/cucumber.html","json:target/cucumber-reports/Cucumber.json","junit:target/cucumber-reports/Cucumber.xml" },
        monochrome = false
)
public class Runner extends AbstractTestNGCucumberTests {
    @Before
    public void before(final Scenario scenario) {
        log.info("------------------------------");
        log.info("Starting - {}", scenario.getName());
        log.info("------------------------------");
    }

    @After
    public void getScenarioInfo(final Scenario scenario) {
        log.info(scenario.getId());
        log.info(scenario.getName());
        log.info(scenario.getStatus().toString());
        log.info("Status: {}", scenario.isFailed());
        log.info(scenario.getSourceTagNames().toString());
    }
}
