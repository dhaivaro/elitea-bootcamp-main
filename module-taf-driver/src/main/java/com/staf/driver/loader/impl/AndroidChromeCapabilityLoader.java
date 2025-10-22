package com.staf.driver.loader.impl;

import com.staf.driver.config.DriverConfig;
import com.staf.driver.config.util.CapabilityUtils;
import com.staf.driver.loader.ICapabilityLoader;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.MutableCapabilities;

/**
 * Class for building DesiredCapabilities for Android chrome browser from configuration.properties
 */
public final class AndroidChromeCapabilityLoader implements ICapabilityLoader {
    @Override
    public MutableCapabilities buildCapabilities(final DriverConfig driverConfig) {
        final MutableCapabilities mutableCapabilities = driverConfig.capabilities();
        // default value for this loader
        mutableCapabilities.setCapability("browserName", "Chrome");

        final UiAutomator2Options options = new UiAutomator2Options().merge(mutableCapabilities);

        // Set Sauce Labs options if it is required
        options.merge(loadSauceLabsCapabilities(driverConfig));
        CapabilityUtils.logCapabilitiesMaps(options.asMap());
        return options;
    }
}
