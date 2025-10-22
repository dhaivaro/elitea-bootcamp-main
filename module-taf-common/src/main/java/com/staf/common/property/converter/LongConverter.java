package com.staf.common.property.converter;

import java.lang.reflect.Method;
import org.aeonbits.owner.Converter;

public final class LongConverter implements Converter<Long> {
    @Override
    public Long convert(final Method method, final String stringLong) {
        return Long.parseLong(stringLong.replace("_", ""));
    }
}
