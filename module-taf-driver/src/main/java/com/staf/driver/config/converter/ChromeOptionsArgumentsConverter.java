package com.staf.driver.config.converter;

import com.staf.driver.constant.CapabilityConstants;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.aeonbits.owner.Converter;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Property owner converter for Chrome Options. Options represent as values or key-values pairs
 * divided by ';;' symbol e.g. "--value1;;--key2=value2"
 */
public final class ChromeOptionsArgumentsConverter implements Converter<ChromeOptions> {
    @Override
    public ChromeOptions convert(final Method method, final String input) {
        final ChromeOptions options = new ChromeOptions();
        final List<String> argumentsList = Pattern.compile(CapabilityConstants.OPTIONS_SEPARATOR)
                .splitAsStream(input)
                .collect(Collectors.toList());
        if (!argumentsList.isEmpty()) {
            options.addArguments(argumentsList);
        }
        return options;
    }
}
