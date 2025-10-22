package com.staf.driver.config.converter;

import com.staf.common.exception.AqaException;
import com.staf.driver.factory.IDriverFactory;
import com.staf.driver.factory.impl.DesktopDriverFactory;
import com.staf.driver.factory.impl.MobileDriverFactory;
import com.staf.driver.type.DriverType;
import java.lang.reflect.Method;

import org.aeonbits.owner.Converter;
import org.assertj.core.util.Arrays;

/** Property owner converter for IDriverFactory by passed driver type. */
public final class PlatformToFactoryConverter implements Converter<IDriverFactory> {
    @Override
    public IDriverFactory convert(final Method method, final String input) {
        switch (DriverType.getDriverType(input)) {
            case CHROME:
            case FIREFOX:
            case EDGE:
            case SAFARI:
                return new DesktopDriverFactory();
            case ANDROID_CHROME:
            case ANDROID_NATIVE:
            case IOS_SAFARI:
            case IOS_NATIVE:
                return new MobileDriverFactory();
            default:
                throw new AqaException(
                        "Current platform %s isn't supported. List of supported platforms is: %s",
                        input, Arrays.asList(DriverType.values()).toString());
        }
    }
}
