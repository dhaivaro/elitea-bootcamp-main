package com.staf.kwd.locator;

import com.staf.kwd.locator.impl.CssLocatorStrategy;
import com.staf.kwd.locator.impl.PageObjectLocatorStrategy;
import com.staf.kwd.locator.impl.TextLocatorStrategy;
import com.staf.kwd.locator.impl.XpathLocatorStrategy;
import lombok.NoArgsConstructor;
import org.openqa.selenium.By;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Factory for Locator's By creation using defined locator strategy
 */
@NoArgsConstructor
public class LocatorStrategyFactory {
    private static final List<LocatorStrategy> STRATEGIES = List.of(new XpathLocatorStrategy(), new CssLocatorStrategy(),
            new TextLocatorStrategy(), new PageObjectLocatorStrategy());

    /**
     * Build By object using @locatorString String value
     * @param locatorString String value of locator
     *
     * @return By object
     */
    public static By buildBy(final String locatorString) {
        final LocatorStrategy locatorStrategy = STRATEGIES.stream()
                .filter(strategy -> locatorString.matches(strategy.getStrategyRegexp()))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
        return locatorStrategy.buildBy(locatorString);
    }
}
