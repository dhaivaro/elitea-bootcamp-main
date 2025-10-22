package com.staf.driver.loader.impl;

import com.staf.driver.config.DriverConfig;
import com.staf.driver.config.util.CapabilityUtils;
import com.staf.driver.loader.ICapabilityLoader;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariOptions;

import java.util.Objects;

/**
 * Class for building DesiredCapabilities for Safari browser from configuration.properties
 */
public final class SafariCapabilityLoader implements ICapabilityLoader {

    @Override
    public MutableCapabilities buildCapabilities(final DriverConfig driverConfig) {
        final SafariOptions options = new SafariOptions();
        if (Objects.nonNull(driverConfig.capabilities())) {
            options.merge(new DesiredCapabilities(driverConfig.capabilities()));
        }
        CapabilityUtils.logCapabilitiesMaps(options.asMap());
        return options;
    }
}
