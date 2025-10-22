package com.staf.examples.demo.selenide.testng.rp;

import static com.staf.common.metadata.ModuleType.RP;
import static com.staf.common.metadata.ModuleType.SELENIDE;
import static com.staf.common.metadata.ModuleType.TESTNG;

import com.staf.common.metadata.Toolset;
import com.staf.examples.demo.selenide.CommonTest;
import com.staf.rp.testng.CustomReportPortalListener;
import org.testng.annotations.Listeners;

@Listeners(CustomReportPortalListener.class)
@Toolset({SELENIDE, RP, TESTNG})
public abstract class AbstractTest extends CommonTest {
    // some common logic for all tests
}
