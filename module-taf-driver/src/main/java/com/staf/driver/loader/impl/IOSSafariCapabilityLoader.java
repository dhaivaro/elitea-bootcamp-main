package com.staf.driver.loader.impl;

import com.staf.driver.config.DriverConfig;
import com.staf.driver.config.util.CapabilityUtils;
import com.staf.driver.loader.ICapabilityLoader;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.openqa.selenium.MutableCapabilities;

/** Class for building DesiredCapabilities for iOS Safari browser from configuration.properties */
public final class IOSSafariCapabilityLoader implements ICapabilityLoader {
    @Override
    public MutableCapabilities buildCapabilities(final DriverConfig driverConfig) {
        final MutableCapabilities mutableCapabilities = driverConfig.capabilities();
        // default value for this loader
        mutableCapabilities.setCapability("browserName", "Safari");

        final XCUITestOptions options = new XCUITestOptions().merge(mutableCapabilities);

        // Set Sauce Labs options if it is required
        options.merge(loadSauceLabsCapabilities(driverConfig));
        CapabilityUtils.logCapabilitiesMaps(options.asMap());
        return options;
    }
}
