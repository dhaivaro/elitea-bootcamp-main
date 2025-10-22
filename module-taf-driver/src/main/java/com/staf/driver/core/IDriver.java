package com.staf.driver.core;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;

public interface IDriver {
    IDriver closeWebDriver();

    // Output type
    <O> O screenshot(OutputType<O> outputType);

    // Driver Wrapper
    <D> D getDriver();

    WebDriver getWebDriver();
}
