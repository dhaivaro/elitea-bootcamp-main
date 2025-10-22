package com.staf.driver.loader.impl;

import com.staf.driver.config.DriverConfig;
import com.staf.driver.config.util.CapabilityUtils;
import com.staf.driver.loader.ICapabilityLoader;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Objects;

/** Class for building DesiredCapabilities for Edge browser from configuration.properties */
public final class EdgeCapabilityLoader implements ICapabilityLoader {
    @Override
    public MutableCapabilities buildCapabilities(final DriverConfig driverConfig) {
        final EdgeOptions options;
        if (Objects.nonNull(driverConfig.capabilities())) {
            options = new EdgeOptions().merge(new DesiredCapabilities(driverConfig.capabilities()));
        } else {
            options = new EdgeOptions();
        }
        // Set Sauce Labs options if it is required
        final MutableCapabilities mutableCapabilities = loadSauceLabsCapabilities(driverConfig).merge(options);

        CapabilityUtils.logCapabilitiesMaps(mutableCapabilities.asMap());
        return mutableCapabilities;
    }
}
