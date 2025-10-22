package com.staf.junit.extension;

import com.codeborne.selenide.logevents.SelenideLogger;
import com.staf.driver.core.DriverManager;
import io.qameta.allure.Attachment;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;

/** Custom extension for Allure */
@Slf4j
public final class AllureReportExtension implements AfterTestExecutionCallback, BeforeAllCallback {
    //    TODO: REVIEW USAGE of AllureSelenide
    @Override
    public void beforeAll(final ExtensionContext context) {
        log.info("Adding 'AllureSelenide' listener");
        SelenideLogger.addListener(
                "AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));
    }

    @Attachment(type = "image/png")
    public byte[] screenshot() {
        return DriverManager.getInstance().getCurrentDriver().screenshot(OutputType.BYTES);
    }

    @Override
    public void afterTestExecution(final ExtensionContext context) {
        final boolean isFailed = context.getExecutionException().isPresent();
        if (isFailed) {
            screenshot();
        }
    }
}
