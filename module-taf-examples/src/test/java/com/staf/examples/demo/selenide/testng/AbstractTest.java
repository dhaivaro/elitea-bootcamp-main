package com.staf.examples.demo.selenide.testng;

import static com.staf.common.metadata.ModuleType.SELENIDE;
import static com.staf.common.metadata.ModuleType.TESTNG;

import com.staf.common.metadata.Toolset;
import com.staf.examples.demo.selenide.CommonTest;
import com.staf.testng.listener.SuiteListener;
import org.testng.annotations.Listeners;

@Toolset({SELENIDE, TESTNG})
@Listeners({SuiteListener.class})
public abstract class AbstractTest extends CommonTest {
    // some common logic for all tests
}
