package com.staf.driver.loader.impl;

import com.staf.common.exception.AqaException;
import com.staf.driver.config.DriverConfig;
import com.staf.driver.config.util.CapabilityUtils;
import com.staf.driver.constant.CapabilityConstants;
import com.staf.driver.loader.ICapabilityLoader;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class for building DesiredCapabilities for Mobile Native (Android or iOS) from configuration.properties
 */
public final class MobileNativeCapabilityLoader implements ICapabilityLoader {
    @Override
    public MutableCapabilities buildCapabilities(final DriverConfig driverConfig) {
        final Map<String, Object> map = new ConcurrentHashMap<>();
        if (Objects.nonNull(driverConfig.capabilities())) {
            map.putAll(driverConfig.capabilities().asMap());
        }
        if (StringUtils.isEmpty(driverConfig.applicationPath())) {
            throw new AqaException("It is necessary to add property driver.capability.app in order to declare path"
                    + " to the application");
        }
        map.put(CapabilityConstants.APP_CAPABILITY, driverConfig.applicationPath());

        // Set Sauce Labs options if it is required
        final MutableCapabilities capabilities = new DesiredCapabilities(map).merge(loadSauceLabsCapabilities(driverConfig));

        CapabilityUtils.logCapabilitiesMaps(capabilities.asMap());

        return capabilities;
    }
}
