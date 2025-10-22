package com.staf.driver.config.converter;

import com.staf.driver.type.DriverType;
import java.lang.reflect.Method;
import org.aeonbits.owner.Converter;

public final class DriverTypeConverter implements Converter<DriverType> {
    @Override
    public DriverType convert(final Method method, final String type) {
        return DriverType.getDriverType(type);
    }
}
