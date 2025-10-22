package com.staf.driver.config.util;

import java.util.Map;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;

/** Utility class for working with driver capabilities */
@Slf4j
@UtilityClass
public class CapabilityUtils {
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static void logCapabilitiesMaps(final Map<String, Object>... capabilitiesMaps) {
        final int capacityForecast = 1000;
        final StringBuffer stringBuffer = new StringBuffer(capacityForecast);
        stringBuffer.append(
                "\n" + "============ The following capabilities and options will be loaded" + " ============\n\n");

        for (Map<String, Object> map : capabilitiesMaps) {
            map.forEach((k, v) -> {
                if (v instanceof Map) {
                    stringBuffer.append(String.format("%s=\n", k));
                    ((Map<String, Object>) v).forEach((k2, v2) -> {
                        stringBuffer.append(String.format("\t%s=%s\n", k2, v2));
                    });
                } else {
                    stringBuffer.append(String.format("%s=%s\n", k, v));
                }
            });
        }
        log.info(stringBuffer.toString());
    }

    /**
     * Return capability value with correct type
     *
     * @param valueConvert converted value
     * @return object
     */
    public static Object updateCapabilityValueType(final String valueConvert) {
        final Object valueType;
        if (NumberUtils.isParsable(valueConvert)) {
            valueType = Integer.parseInt(valueConvert);
        } else if (BooleanUtils.toBooleanObject(valueConvert) != null) {
            valueType = Boolean.parseBoolean(valueConvert);
        } else {
            valueType = valueConvert;
        }
        return valueType;
    }
}
