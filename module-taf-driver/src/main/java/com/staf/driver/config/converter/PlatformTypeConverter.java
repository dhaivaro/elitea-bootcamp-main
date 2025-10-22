package com.staf.driver.config.converter;

import com.staf.driver.type.PlatformTypes;
import java.lang.reflect.Method;
import org.aeonbits.owner.Converter;

public final class PlatformTypeConverter implements Converter<PlatformTypes> {
    @Override
    public PlatformTypes convert(final Method method, final String input) {
        return PlatformTypes.getPlatform(input);
    }
}
