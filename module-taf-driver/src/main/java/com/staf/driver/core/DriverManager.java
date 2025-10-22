package com.staf.driver.core;

import com.staf.common.exception.AqaException;
import com.staf.driver.config.DriverConfig;
import com.staf.driver.constant.DriverConstants;
import com.staf.driver.core.annotation.HolderType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

public final class DriverManager {
    private static DriverManager instance;
    private final Class<IDriver> activeDriverHolder;

    private DriverManager() {
        // search for all classes annotated with HolderType
        final Reflections reflections =
                new Reflections(new ConfigurationBuilder().forPackages(DriverConstants.DEFAULT_HOLDER_PACKAGE));
        final Set<Class<?>> holders = reflections.getTypesAnnotatedWith(HolderType.class);
        // TODO: change to Java Assert?
        Assertions.assertThat(holders)
                .as("There is no any driver holders annotated with HolderType.class")
                .isNotEmpty();
        // select holder class by property driver.holder
        activeDriverHolder = (Class<IDriver>) holders.stream()
                .filter(holderType -> holderType.getAnnotation(HolderType.class).type()
                        == DriverConfig.get().holderType())
                .findFirst()
                .orElseThrow(() -> new AqaException(
                        "Unable to find Holder class by type '%s'."
                                + " Add driver.holder property to"
                                + " configuration.properties in order"
                                + " to interact with a DriverManager",
                        DriverConfig.get().holderType().toString()));
    }

    public static synchronized DriverManager getInstance() {
        if (Objects.isNull(instance)) {
            instance = new DriverManager();
        }
        return instance;
    }

    public IDriver getCurrentDriver() {
        final Constructor<IDriver> iDriverConstructor;
        try {
            iDriverConstructor = activeDriverHolder.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new AqaException(
                    "Unable to find constructor w/o parameters for holder class '%s'", activeDriverHolder.getName());
        }
        try {
            return iDriverConstructor.newInstance();
        } catch (ExceptionInInitializerError
                | InstantiationException
                | IllegalAccessException
                | InvocationTargetException e) {
            e.printStackTrace();
            throw new AqaException(
                    "Unable to instantiate driver holder (%s): '%s'", activeDriverHolder.getName(), e.getMessage());
        }
    }
}
