package com.staf.examples.demo.selenide.junit5.rp.suite;

import com.staf.common.metadata.ModuleType;
import com.staf.common.metadata.Toolset;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Demo smoke suite: JUnit5 + Report Portal + Selenide")
@SelectPackages({"com.staf.examples.demo.selenide.junit5.rp"})
@Toolset({ModuleType.SELENIDE, ModuleType.RP, ModuleType.JUNIT5})
public class DemoSuite {}
