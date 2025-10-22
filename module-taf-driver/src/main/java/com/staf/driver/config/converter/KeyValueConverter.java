package com.staf.driver.config.converter;

import com.staf.driver.constant.CapabilityConstants;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.aeonbits.owner.Converter;
import org.apache.commons.lang3.StringUtils;

/**
 * Property owner converter for key-value data. Data represents as a key-value pairs divided by ';;'
 * symbol e.g. "key1=value1;;key2=value2"
 */
public final class KeyValueConverter implements Converter<Map<String, Object>> {
    @Override
    public Map<String, Object> convert(final Method method, final String input) {
        final Map<String, Object> map = new ConcurrentHashMap<>();
        final Supplier<Stream<String>> streamSupplier =
                () -> Pattern.compile(CapabilityConstants.OPTIONS_SEPARATOR).splitAsStream(input);
        if (!StringUtils.isEmpty(input) && streamSupplier.get().count() > 0) {
            map.putAll(streamSupplier
                    .get()
                    .map(s -> s.split(CapabilityConstants.KEY_VALUE_SEPARATOR, 2))
                    .collect(Collectors.toMap(argument -> argument[0], argument -> {
                        if (argument.length > 1) {
                            return argument[1];
                        } else {
                            return "";
                        }
                    })));
        }
        return map;
    }
}
