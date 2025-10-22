package com.staf.kwd.storage;

import com.staf.kwd.storage.action.Dictionary;
import com.staf.kwd.storage.constants.StorageConstants;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.staf.kwd.storage.constants.StorageConstants.VARIABLE_ARRAY_REGEX;

@Slf4j
@UtilityClass
public class ObjectsStorage {
    private static final Map<Object, Object> KEY_TO_VALUE = new ConcurrentHashMap<>();

    /**
     * Put entity into the global storage
     */
    public static <T, V> void putEntity(final T key, final V value) {
        log.info("Attempt to put entity in storage  with name '{}', value '{}'.", key.toString(), value.toString());
        KEY_TO_VALUE.put(key, value);
        log.info("Entity was saved in local storage with name '{}', value '{}'.", key.toString(), value.toString());
    }

    @SuppressWarnings("unchecked")
    private static <T, V> V handleNull(final T key) {
        final V value = (V) KEY_TO_VALUE.get(key);
        if (value == null) {
            throw new RuntimeException(String.format("Object with key '%s' is not found.", key.toString()));
        }
        return value;
    }

    /**
     * Directly retrieve entity from the global storage
     */
    public static <T, V> V getEntity(final T key) {
        final V value = handleNull(key);
        log.info("Found key '{}' with value '{}' of instance '{}'.", key, value, value.getClass());
        return value;
    }

    /**
     * Verify that key is a variable and retrieve value from the global storage. Returns passed
     * key itself as a value in case it is not recognized as a variable.
     * <p>
     * Useful for case when both values and variables can be processed by the same methods.
     */
    @SuppressWarnings("unchecked")
    public static <T, V> V findInStorage(final T key) {
        log.info("Text from the storage will be extracted by placeholder '{}'.", key);
        V value = null;
        if (Identifier.isVariable(key)) {
            value = getVariable(key.toString());
        } else if (Identifier.isVariableArray(key)) {
            value = (V) getStringArrayByIndex(key.toString());
        } else if (KEY_TO_VALUE.containsKey(key)) {
            value = handleNull(key);
        } else {
            value = (V) key;
        }
        log.info("Value '{}' will be returned.", value);
        return value;
    }

    /**
     * Provides text based on requirements. Text from the storage will be selected
     * in case of syntax '${var_name}'
     */
    private static <V> V getVariable(final String key) {
        final V result;
        if (Dictionary.contains(key)) {
            result = Dictionary.execute(key);
        } else {
            result = handleNull(key);
        }
        return result;
    }

    /**
     * returns item from the storage by index.
     * Example:
     * Storage contains pair '"${some_array2} -> value1;value2"'
     * getByArrayIndex(2) returns "value2".
     * Important: index starts from 1 since it is more convenient for business user
     */
    private static String getStringArrayByIndex(final String key) {
        final Pattern pattern = Pattern.compile(VARIABLE_ARRAY_REGEX);
        final Matcher matcher = pattern.matcher(key);
        final String arrayStorageKey;
        final int index;
        if (matcher.find()) {
            arrayStorageKey = matcher.group(1);
            index = Integer.parseInt(matcher.group(2));
        } else {
            throw new RuntimeException(String.format("Key '{}' doesn't match on array group.", key));
        }
        final String objectValue = handleNull(arrayStorageKey);
        if (arrayStorageKey.equals(objectValue)) {
            throw new RuntimeException(String.format("Unable to identify element with key {} in the storage.", arrayStorageKey));
        }
        final String[] values = objectValue.split(StorageConstants.REGEXP_SPLITTER);
        if (values.length < index) {
            throw new RuntimeException(String.format("Incorrect index ({}) has been passed. Array length{}'.", index, values.length));
        }
        return values[index - 1];
    }
}
