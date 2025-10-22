package com.staf.driver.config.converter;

import com.staf.driver.type.PlatformTypes;
import java.lang.reflect.Method;

import org.aeonbits.owner.Converter;

public final class MobilePlatformCheckerConverter implements Converter<Boolean> {
    @Override
    public Boolean convert(final Method method, final String input) {
        return PlatformTypes.getPlatform(input).equals(PlatformTypes.MOBILE);
    }
}
