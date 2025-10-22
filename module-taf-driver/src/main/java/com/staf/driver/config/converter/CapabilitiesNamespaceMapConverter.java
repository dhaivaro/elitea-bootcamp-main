package com.staf.driver.config.converter;

import static com.staf.driver.constant.CapabilityConstants.KEY_VALUE_SEPARATOR;
import static com.staf.driver.constant.CapabilityConstants.OPTIONS_SEPARATOR;

import com.staf.driver.config.DriverConfig;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.Converter;
import org.openqa.selenium.remote.DesiredCapabilities;

/** Property owner converter for capabilities extraction by namespace to Map */
@Slf4j
public final class CapabilitiesNamespaceMapConverter implements Converter<DesiredCapabilities> {
    @Override
    public DesiredCapabilities convert(final Method method, final String input) {
        final DriverConfig driverConfig = DriverConfig.get();
        final String keyNamespace = method.getAnnotation(Config.Key.class).value();
        final Set<String> declaredCapabilityProperties = driverConfig.propertyNames().stream()
                .filter(property -> (property.matches(keyNamespace)) && !property.equalsIgnoreCase(keyNamespace))
                .collect(Collectors.toSet());
        log.info("Declared properties: {}", declaredCapabilityProperties);
        final Map<String, Object> capabilities = new ConcurrentHashMap<>();
        if (!declaredCapabilityProperties.isEmpty()) {
            capabilities.putAll(declaredCapabilityProperties.stream()
                    .collect(Collectors.toMap(
                            capabilityProperty -> {
                                final String[] parsedPropertyName = capabilityProperty.split("\\.");
                                if (parsedPropertyName.length == 1) {
                                    return parsedPropertyName[0];
                                } else {
                                    return parsedPropertyName[parsedPropertyName.length - 1];
                                }
                            },
                            capabilityProperty -> parseProperty(driverConfig.getProperty(capabilityProperty)))));
        }
        return new DesiredCapabilities(capabilities);
    }

    /**
     * Parses property value and identifies if it is map
     * @param value value for identification
     * @return parsed value
     */
    private Object parseProperty(final String value) {
        return value.contains(KEY_VALUE_SEPARATOR)
                ? Arrays.stream(value.split(OPTIONS_SEPARATOR))
                        .collect(Collectors.toMap(entry -> entry.split(KEY_VALUE_SEPARATOR)[0], entry -> entry.split(
                                        KEY_VALUE_SEPARATOR)[1]))
                : value;
    }
}
