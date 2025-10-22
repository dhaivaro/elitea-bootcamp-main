package com.staf.kwd.locator.impl;

import com.staf.common.util.RegexpUtils;
import com.staf.kwd.locator.LocatorStrategy;
import lombok.Getter;
import org.openqa.selenium.By;

/**
 * Locator strategy using Xpath selectors
 */
@Getter
public class XpathLocatorStrategy implements LocatorStrategy {
    // $x(//*[@class='test']) or $x("//*[@class='test']")
    private final String strategyRegexp = "\\$x\\(\\\"?(.*)\\\"?\\)";

    @Override
    public By buildBy(final String locatorString) {
        return By.xpath(RegexpUtils.getStringByRegexp(locatorString, strategyRegexp, 1));
    }
}
