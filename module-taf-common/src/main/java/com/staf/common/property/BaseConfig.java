package com.staf.common.property;

import com.staf.common.constant.ConfigConstant;
import com.staf.common.exception.AqaException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.aeonbits.owner.Accessible;
import org.aeonbits.owner.Config;

public interface BaseConfig extends Accessible {
    /**
     * Method is used for extraction of encrypted properties passed as an environment variable or as
     * a system property and were not declared at class level
     *
     * <p>
     *
     * @param propertyName property name
     * @return decrypted value
     */
    default String getDecryptedProperty(final String propertyName) {
        final List<Method> existingMethods =
                Arrays.stream(Configuration.class.getDeclaredMethods()).collect(Collectors.toList());
        final Optional<Method> propertyMethod = existingMethods.stream()
                .filter(method -> method.isAnnotationPresent(Config.EncryptedValue.class)
                        && method.getAnnotation(Config.Key.class).value().equalsIgnoreCase(propertyName))
                .findFirst();
        final String propertyValue;
        try {
            if (propertyMethod.isEmpty()) {
                // property are not declared at class level
                final String property = getProperty(propertyName);
                if (property == null) {
                    propertyValue = getProperty(propertyName.toUpperCase());
                } else {
                    propertyValue = findClassFromProxy()
                            .getAnnotation(DecryptorClass.class)
                            .value()
                            .getDeclaredConstructor()
                            .newInstance()
                            .decrypt(property);
                }
            } else {
                propertyValue = propertyMethod.get().invoke(this).toString();
            }
            return propertyValue;
        } catch (InvocationTargetException
                | IllegalAccessException
                | NoSuchMethodException
                | InstantiationException e) {
            e.printStackTrace();
            throw new AqaException(
                    "Unable to extract encrypted property '%s'. Exception: %s. Check presence of"
                            + " Decryptor class with default constructor",
                    propertyName, e.getMessage());
        }
    }

    /**
     * Extracts class from a proxy
     *
     * @param <T> config class that extends BaseConfig
     * @return found class
     */
    default <T extends BaseConfig> Class<T> findClassFromProxy() {
        return (Class<T>) Stream.of(this.getClass().getInterfaces())
                .filter(clazz -> !ConfigConstant.DYNAMIC_BEAN_CLASS.equals(clazz.getName()))
                .findFirst()
                .orElseThrow(() -> new AqaException("Unable to identify correct interface name for"
                        + " extraction of encrypted value. Make"
                        + " getInterfaces() contains interface name of"
                        + " instantiated class"));
    }
}
