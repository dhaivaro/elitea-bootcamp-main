package com.staf.driver.config.converter;

import com.staf.driver.loader.ICapabilityLoader;
import com.staf.driver.type.DriverType;
import java.lang.reflect.Method;
import org.aeonbits.owner.Converter;

/** Property owner converter for getting CapabilityLoader by DriverType */
public final class BrowserToCapabilityLoaderConverter implements Converter<ICapabilityLoader> {
    @Override
    public ICapabilityLoader convert(final Method method, final String input) {
        return DriverType.getDriverType(input).getCapabilityLoader();
    }
}
