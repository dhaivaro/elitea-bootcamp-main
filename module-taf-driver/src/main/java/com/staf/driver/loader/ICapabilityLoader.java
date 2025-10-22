package com.staf.driver.loader;

import com.staf.driver.config.DriverConfig;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;
import java.util.Objects;

/**
 * Interface for building DesiredCapabilities for pre-defined browser from the configuration file
 * (configuration.properties)
 */
public interface ICapabilityLoader {
    /**
     * Build DesiredCapabilities for browser based on BrowserConfig
     *
     * @param driverConfig driver configuration interface
     * @return capabilities built capabilities
     */
    MutableCapabilities buildCapabilities(DriverConfig driverConfig);

    default MutableCapabilities loadSauceLabsCapabilities(final DriverConfig driverConfig) {
        final Map<String, Object> sauceOptions = driverConfig.sauceOptions();
        final MutableCapabilities capabilities = new DesiredCapabilities();
        if (Objects.nonNull(sauceOptions) && !sauceOptions.isEmpty()) {
            capabilities.setCapability("sauce:options", sauceOptions);
        }
        return capabilities;
    }
}
