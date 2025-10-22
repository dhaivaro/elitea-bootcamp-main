package com.staf.common.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ModuleType {
    JUNIT5("JUnit5", ToolGroup.TEST_FRAMEWORK),
    TESTNG("TestNG", ToolGroup.TEST_FRAMEWORK),

    RP("Report Portal", ToolGroup.REPORTING),
    ALLURE("Allure", ToolGroup.REPORTING),

    SELENIDE("Selenide", ToolGroup.DRIVER),

    REST_ASSURED("Rest Assured", ToolGroup.API);

    private final String name;
    private final ToolGroup group;
}
