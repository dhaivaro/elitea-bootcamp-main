package com.staf.driver.config.converter;

import com.staf.driver.core.Type;
import java.lang.reflect.Method;

import org.aeonbits.owner.Converter;

public final class HolderTypeConverter implements Converter<Type> {
    @Override
    public Type convert(final Method method, final String type) {
        return Type.getHolderType(type);
    }
}
