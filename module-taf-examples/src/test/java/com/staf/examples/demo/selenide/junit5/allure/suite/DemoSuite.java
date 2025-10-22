package com.staf.examples.demo.selenide.junit5.allure.suite;

import com.staf.common.metadata.ModuleType;
import com.staf.common.metadata.Toolset;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Demo smoke suite: JUnit5 + Allure + Selenide")
@SelectPackages({"com.staf.examples.demo.selenide.junit5.allure"})
@Toolset({ModuleType.SELENIDE, ModuleType.ALLURE, ModuleType.JUNIT5})
public class DemoSuite {}
