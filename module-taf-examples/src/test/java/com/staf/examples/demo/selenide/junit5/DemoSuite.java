package com.staf.examples.demo.selenide.junit5;

import com.staf.common.metadata.ModuleType;
import com.staf.common.metadata.Toolset;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Demo smoke suite: JUnit5 + Selenide")
@SelectClasses({LoginTest.class})
@Toolset({ModuleType.SELENIDE, ModuleType.JUNIT5})
public class DemoSuite {}
