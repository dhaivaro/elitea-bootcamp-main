package com.staf.kwd.locator.impl;

import com.staf.kwd.locator.LocatorStrategy;
import lombok.Getter;
import org.openqa.selenium.By;

/**
 * Locator strategy using text selectors
 */
@Getter
public class TextLocatorStrategy implements LocatorStrategy {
    // John Doe
    private final String strategyRegexp = "^(?!\\$\\(|\\$x\\(|.* -> .*).*";

    @Override
    public By buildBy(final String locatorString) {
        return By.linkText(locatorString);
    }
}
