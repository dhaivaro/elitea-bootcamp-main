package com.staf.common.property;

import java.util.Collections;
import java.util.Map;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;

@Slf4j
@UtilityClass
public class PropertyUtil {
    private static final String LOGGER_PATTERN_CONFIG = "Source map for initialization: {}. Class: {}";

    public static synchronized <T extends Config> T initConfig(final Class<T> clazz) {
        return initConfig(clazz, Collections.emptyMap());
    }

    /**
     * Initializes properties interface with the data from properties files passed as Sources
     *
     * @param clazz initialized interface
     * @param sourceMap source map in Source is parametrized
     * @param <T> class
     * @return property storage
     */
    public static synchronized <T extends Config> T initConfig(
            final Class<T> clazz, final Map<String, String> sourceMap) {
        log.debug(LOGGER_PATTERN_CONFIG, sourceMap, clazz.getSimpleName());
        sourceMap.forEach(ConfigFactory::setProperty);
        return ConfigFactory.create(clazz, System.getProperties(), System.getenv());
    }

    /**
     * * Provides already initialized properties or initialize new properties if nothing is cached
     *
     * @param clazz initialized interface
     * @param <T> properties interface
     * @return property storage
     */
    public static synchronized <T extends Config> T getConfig(final Class<T> clazz) {
        return initConfig(clazz, Collections.emptyMap());
    }

    /**
     * Provides already initialized properties or initialize new properties if nothing is cached
     *
     * @param clazz initialized interface
     * @param sourceMap source map in Source is parametrized
     * @param <T> properties interface
     * @return property storage
     */
    public static synchronized <T extends Config> T getConfig(
            final Class<T> clazz, final Map<String, String> sourceMap) {
        log.debug(LOGGER_PATTERN_CONFIG, sourceMap, clazz.getSimpleName());
        sourceMap.forEach(ConfigFactory::setProperty);
        return ConfigFactory.create(clazz, System.getProperties(), System.getenv());
    }
}
