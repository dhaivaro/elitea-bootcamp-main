package com.staf.driver.loader.impl;

import com.staf.driver.config.DriverConfig;
import com.staf.driver.config.util.CapabilityUtils;
import com.staf.driver.config.util.FirefoxProfileUtils;
import com.staf.driver.loader.ICapabilityLoader;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.util.Map;

/** Class for building DesiredCapabilities for Firefox browser from configuration.properties */
public final class FirefoxCapabilityLoader implements ICapabilityLoader {
    @Override
    public MutableCapabilities buildCapabilities(final DriverConfig driverConfig) {
        final Map<String, Object> profileMap = driverConfig.firefoxProfile();

        final FirefoxProfile profile = new FirefoxProfile();
        profileMap.forEach((key, value) -> FirefoxProfileUtils.updateProfileByValue(profile, key, (String) value));

        final FirefoxOptions options = new FirefoxOptions();
        options.setProfile(profile);

        final FirefoxOptions mergedOptions = options.merge(driverConfig.capabilities());
        // Set Sauce Labs options if it is required
        options.merge(loadSauceLabsCapabilities(driverConfig));

        CapabilityUtils.logCapabilitiesMaps(mergedOptions.asMap());
        return mergedOptions;
    }
}
