package com.staf.driver.core;

import com.staf.common.exception.AqaException;
import java.util.stream.Stream;

public enum Type {
    SELENIDE,
    SERENITY,
    NOT_DEFINED;

    public static Type getHolderType(final String holderType) {
        return Stream.of(Type.values())
                .filter(value -> value.toString().equalsIgnoreCase(holderType))
                .findFirst()
                .orElseThrow(() -> new AqaException(
                        "Incorrect driver's holder type '%s' is defined in"
                                + " configuration.properties. Put any from [%s] as a"
                                + " property driver.holder",
                        holderType, Type.values().toString()));
    }
}
