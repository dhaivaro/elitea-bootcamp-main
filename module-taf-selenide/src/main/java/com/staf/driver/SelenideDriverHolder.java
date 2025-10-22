package com.staf.driver;

import com.codeborne.selenide.Driver;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.staf.driver.core.IDriver;
import com.staf.driver.core.Type;
import com.staf.driver.core.annotation.HolderType;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;

@HolderType(type = Type.SELENIDE)
public final class SelenideDriverHolder implements IDriver {
    @Override
    public IDriver closeWebDriver() {
        Selenide.closeWebDriver();
        return this;
    }

    @Override
    public <O> O screenshot(final OutputType<O> outputType) {
        return Selenide.screenshot(outputType);
    }

    @Override
    public WebDriver getWebDriver() {
        return getDriver().getWebDriver();
    }

    @Override
    public Driver getDriver() {
        return WebDriverRunner.driver();
    }
}
