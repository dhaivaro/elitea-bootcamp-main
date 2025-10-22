package com.staf.examples.demo.selenide.junit5.rp;

import static com.staf.common.metadata.ModuleType.JUNIT5;
import static com.staf.common.metadata.ModuleType.RP;
import static com.staf.common.metadata.ModuleType.SELENIDE;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import com.codeborne.selenide.junit5.BrowserPerTestStrategyExtension;
import com.staf.common.metadata.Toolset;
import com.staf.examples.demo.selenide.CommonTest;
import com.staf.junit.extension.OnTestStartExtension;
import com.staf.rp.junit.OnTestFinishReportPortalExtension;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(PER_CLASS)
@ExtendWith({OnTestFinishReportPortalExtension.class, OnTestStartExtension.class, BrowserPerTestStrategyExtension.class
})
@Toolset({SELENIDE, JUNIT5, RP})
public abstract class AbstractTest extends CommonTest {
    // some common logic for all tests
}
