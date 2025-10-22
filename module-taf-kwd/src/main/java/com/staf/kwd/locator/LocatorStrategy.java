package com.staf.kwd.locator;

import org.openqa.selenium.By;

public interface LocatorStrategy {
    By buildBy(String locatorString);

    String getStrategyRegexp();
}
