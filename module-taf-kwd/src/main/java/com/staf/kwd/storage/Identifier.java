package com.staf.kwd.storage;

import lombok.experimental.UtilityClass;

import java.util.Map;

import static com.staf.kwd.storage.constants.StorageConstants.VARIABLE_ARRAY_REGEX;
import static com.staf.kwd.storage.constants.StorageConstants.VARIABLE_REGEX;

@UtilityClass
class Identifier {
    @SuppressWarnings("unchecked")
    public static <T, V> V handleNull(final Map<Object, Object> keyToValue, final T key) {
        final V value = (V) keyToValue.get(key);
        if (value == null) {
            throw new RuntimeException(String.format("Object with key '%s' is not found.", key.toString()));
        }
        return value;
    }

    static <T> boolean isVariable(final T key) {
        return key instanceof String && key.toString().matches(VARIABLE_REGEX);
    }

    static <T> boolean isVariableArray(final T key) {
        return key instanceof String && key.toString().matches(VARIABLE_ARRAY_REGEX);
    }
}
