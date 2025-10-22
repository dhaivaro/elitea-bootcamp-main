package com.staf.driver.loader.impl;

import com.staf.driver.config.DriverConfig;
import com.staf.driver.config.util.CapabilityUtils;
import com.staf.driver.constant.CapabilityConstants;
import com.staf.driver.loader.ICapabilityLoader;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Map;
import java.util.Objects;

/** Class for building DesiredCapabilities for Chrome browser from configuration.properties */
public final class ChromeCapabilityLoader implements ICapabilityLoader {
    @Override
    public MutableCapabilities buildCapabilities(final DriverConfig driverConfig) {
        final ChromeOptions initialOptions;
        if (driverConfig.chromeOptions() == null) {
            initialOptions = new ChromeOptions();
        } else {
            initialOptions = driverConfig.chromeOptions();
        }
        final ChromeOptions options = initialOptions.merge(driverConfig.capabilities());

        final Map<String, Object> prefsMap = driverConfig.chromePrefs();
        final Map<String, Object> experimentalCapabilitiesMap = driverConfig.chromeExperimentalOptions();

        if (Objects.nonNull(prefsMap) && !prefsMap.isEmpty()) {
            options.setExperimentalOption(CapabilityConstants.CHROME_OPTIONS_PREFERENCES_KEY, prefsMap);
        }
        if (Objects.nonNull(experimentalCapabilitiesMap) && !experimentalCapabilitiesMap.isEmpty()) {
            driverConfig.chromeExperimentalOptions().forEach(options::setExperimentalOption);
        }
        // Set Sauce Labs options if it is required
        final MutableCapabilities mutableCapabilities = loadSauceLabsCapabilities(driverConfig).merge(options);

        CapabilityUtils.logCapabilitiesMaps(mutableCapabilities.asMap());
        return mutableCapabilities;
    }
}
