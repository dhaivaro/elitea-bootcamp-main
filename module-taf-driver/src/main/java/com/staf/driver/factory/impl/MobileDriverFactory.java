package com.staf.driver.factory.impl;

import com.staf.common.exception.AqaException;
import com.staf.driver.config.DriverConfig;
import com.staf.driver.factory.IDriverFactory;
import com.staf.driver.runner.AppiumServerManager;
import com.staf.driver.type.DriverType;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;

import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

/** Factory implementation for WebDriver creation for Mobile Platform */
public final class MobileDriverFactory implements IDriverFactory {
    @Override
    public WebDriver instantiateDriver() {
        final DriverConfig driverConfig = DriverConfig.get();

        validateConfig();

        final MutableCapabilities capabilities =
                driverConfig.capabilityLoaderByBrowser().buildCapabilities(driverConfig);
        URL seleniumURL = driverConfig.seleniumServer();
        if (Objects.isNull(seleniumURL)) {
            // Appium server should be started for mobile local run since gridURL is undefined
            final AppiumDriverLocalService server = AppiumServerManager.startAppiumServer();
            seleniumURL = server.getUrl();
        }

        // TODO: if selenide disable browser size (think how to move out of driver module)
        //       put at selenide module to WebDriverRunner: Configuration.browserSize = null;
        switch (driverConfig.driverType()) {
            case ANDROID_NATIVE:
            case ANDROID_CHROME:
                return new AndroidDriver(seleniumURL, capabilities);
            case IOS_NATIVE:
            case IOS_SAFARI:
                return new IOSDriver(seleniumURL, capabilities);
            default:
                throw new AqaException(
                        "Driver type %s isn't supported yet. Supported types are: %s",
                        driverConfig.driverType().getDriverType(), Arrays.toString(DriverType.values()));
        }
    }
}
