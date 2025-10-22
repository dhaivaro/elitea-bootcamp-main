package com.staf.kwd.locator.impl;

import com.staf.common.util.RegexpUtils;
import com.staf.kwd.locator.LocatorStrategy;
import lombok.Getter;
import org.openqa.selenium.By;

/**
 * Locator strategy using CSS selectors
 */
@Getter
public class CssLocatorStrategy implements LocatorStrategy {
    // $(.test) or $(".test")
    private final String strategyRegexp = "\\$\\((['\"]?)([^)]+)\\1\\)";

    @Override
    public By buildBy(final String locatorString) {
        return By.cssSelector(RegexpUtils.getStringByRegexp(locatorString, strategyRegexp, 2));
    }
}
