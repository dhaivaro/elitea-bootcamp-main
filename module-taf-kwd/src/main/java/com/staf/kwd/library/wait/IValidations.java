package com.staf.kwd.library.wait;

import org.openqa.selenium.By;

// TODO: from Pavel: split this interface to few simple, keeping in mind I from SOLID
public interface IValidations {
    void expectToBeVisible(By locator, boolean reverse);

    void expectToBeClickable(By locator, boolean reverse);

    void expectToBePresent(By locator, boolean reverse);

    void expectToHaveCssValue(By locator, boolean reverse, String cssName, String expectedCssValue);

    void expectToHaveAttributeValue(By locator, boolean reverse, String attributeName, String expectedAttributeValue);

    void expectToHaveAttribute(By locator, boolean reverse, String attributeName);

    void expectToHaveAttributeValueMatched(By locator, boolean reverse, String attributeName, String attributeRegexp);

    void expectToHaveExactText(By locator, boolean reverse, String text);

    void expectToContainsText(By locator, boolean reverse, String text);

    void expectURL(boolean reverse, String url);

    void expectPageTitle(boolean reverse, String url);

    void expectCountOfElementsEqual(By locator, String count);

    void expectCountOfElementsAbove(By locator, String count);

    void expectCountOfElementsBelow(By locator, String count);

    void expectElementsHaveExactText(By locator, boolean reverse, String text);

    void expectElementsContainsText(By locator, boolean reverse, String text);

    void expectElementsVisible(By locator, boolean reverse);

    void expectElementsClickable(By locator, boolean reverse);

    void expectElementsPresent(By locator, boolean reverse);

    void expectElementsHaveCssValue(By locator, boolean reverse, String cssName, String expectedCssValue);

    void expectElementsHaveAttribute(By locator, boolean reverse, String attributeName);

    void expectElementsHaveAttributeValue(By locator, boolean reverse, String attributeName, String expectedAttributeValue);

    void expectElementsHaveAttributeValueMatched(By locator, boolean reverse, String attributeName, String attributeRegexp);
}
