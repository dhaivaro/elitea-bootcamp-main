package com.staf.driver;

import com.codeborne.selenide.WebDriverProvider;
import com.staf.driver.config.DriverConfig;
import javax.annotation.Nonnull;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

/**
 * Implementation of selenide WebDriveProvider using capabilities in the configuration.properties
 */
public final class DriverProvider implements WebDriverProvider {
    @Nonnull
    @Override
    public WebDriver createDriver(@Nonnull final Capabilities capabilities) {
        return DriverConfig.get().driverFactoryByPlatform().instantiateDriver();
    }
}
