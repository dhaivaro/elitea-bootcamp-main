package com.staf.driver.factory;

import com.staf.common.exception.AqaException;
import com.staf.driver.config.DriverConfig;
import com.staf.driver.core.Type;
import org.openqa.selenium.WebDriver;

/**
 * Factory interface for WebDriver creation
 */
public interface IDriverFactory {
    WebDriver instantiateDriver();

    default void validateConfig() {
        final Type type = DriverConfig.get().holderType();
        if (type == Type.NOT_DEFINED) {
            throw new AqaException(
                    "Property driver.holder is undefined in configuration.properties in order to interact with a DriverManager",
                    type.toString());
        }
    }

}
