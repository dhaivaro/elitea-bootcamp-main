package com.staf.driver.factory.impl;

import com.staf.driver.config.DriverConfig;
import com.staf.driver.factory.IDriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

/** Factory implementation for WebDriver creation for Desktop Platform */
public final class DesktopDriverFactory implements IDriverFactory {
    @Override
    public WebDriver instantiateDriver() {
        final DriverConfig driverConfig = DriverConfig.get();

        validateConfig();
        return new RemoteWebDriver(
                driverConfig.seleniumServer(),
                driverConfig.capabilityLoaderByBrowser().buildCapabilities(driverConfig));
    }
}
