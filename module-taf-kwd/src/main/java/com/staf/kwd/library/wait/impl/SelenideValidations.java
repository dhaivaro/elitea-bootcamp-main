package com.staf.kwd.library.wait.impl;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverConditions;
import com.staf.kwd.library.wait.IValidations;
import lombok.Getter;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Getter
public class SelenideValidations implements IValidations {
    private static final String TEXT_CONTAINS_PATTERN = ".*%s.*";
    private static final String CLICKABLE_CONDITION = "Clickable";

    @Override
    public void expectToBeVisible(final By locator, final boolean reverse) {
        performElementValidation(locator, reverse, Condition.visible);
    }

    @Override
    public void expectToBeClickable(final By locator, final boolean reverse) {
        performElementValidation(locator, reverse, Condition.and(CLICKABLE_CONDITION, Condition.visible, Condition.enabled));
    }

    @Override
    public void expectToBePresent(final By locator, final boolean reverse) {
        performElementValidation(locator, reverse, Condition.exist);
    }

    @Override
    public void expectToHaveCssValue(final By locator, final boolean reverse, final String cssName, final String expectedCssValue) {
        final Condition condition = Condition.cssValue(cssName, expectedCssValue);
        performElementValidation(locator, reverse, condition);
    }

    @Override
    public void expectToHaveAttributeValue(final By locator, final boolean reverse, final String attributeName, final String expectedAttributeValue) {
        final Condition condition = Condition.attribute(attributeName, expectedAttributeValue);
        performElementValidation(locator, reverse, condition);
    }

    @Override
    public void expectToHaveAttribute(final By locator, final boolean reverse, final String attributeName) {
        final Condition condition = Condition.attribute(attributeName);
        performElementValidation(locator, reverse, condition);
    }

    @Override
    public void expectToHaveAttributeValueMatched(final By locator, final boolean reverse, final String attributeName, final String attributeRegexp) {
        final Condition condition = Condition.attributeMatching(attributeName, attributeRegexp);
        performElementValidation(locator, reverse, condition);
    }

    @Override
    public void expectToHaveExactText(final By locator, final boolean reverse, final String text) {
        final Condition condition = Condition.exactText(text);
        performElementValidation(locator, reverse, condition);
    }

    @Override
    public void expectToContainsText(final By locator, final boolean reverse, final String text) {
        final Condition condition = Condition.matchText(String.format(TEXT_CONTAINS_PATTERN, text));
        performElementValidation(locator, reverse, condition);
    }

    @Override
    public void expectURL(final boolean reverse, final String url) {
        if (reverse) {
            Selenide.webdriver().shouldNotHave(WebDriverConditions.url(url));
        } else {
            Selenide.webdriver().shouldHave(WebDriverConditions.url(url));
        }
    }

    @Override
    public void expectPageTitle(final boolean reverse, final String url) {
        if (reverse) {
            Selenide.webdriver().shouldNotHave(WebDriverConditions.title(url));
        } else {
            Selenide.webdriver().shouldHave(WebDriverConditions.title(url));
        }
    }

    @Override
    public void expectCountOfElementsEqual(final By locator, final String count) {
        performElementsValidation(locator, CollectionCondition.size(Integer.parseInt(count)));
    }

    @Override
    public void expectCountOfElementsAbove(final By locator, final String count) {
        performElementsValidation(locator, CollectionCondition.sizeGreaterThan(Integer.parseInt(count)));
    }

    @Override
    public void expectCountOfElementsBelow(final By locator, final String count) {
        performElementsValidation(locator, CollectionCondition.sizeLessThan(Integer.parseInt(count)));
    }

    @Override
    public void expectElementsHaveExactText(final By locator, final boolean reverse, final String text) {
        final Condition condition = Condition.exactText(text);
        $$(locator).forEach(item -> performValidationForSelenideElement(item, reverse, condition));
    }

    @Override
    public void expectElementsContainsText(final By locator, final boolean reverse, final String text) {
        final Condition condition = Condition.matchText(String.format(TEXT_CONTAINS_PATTERN, text));
        $$(locator).forEach(item -> performValidationForSelenideElement(item, reverse, condition));
    }

    @Override
    public void expectElementsVisible(final By locator, final boolean reverse) {
        $$(locator).forEach(item -> performValidationForSelenideElement(item, reverse, Condition.visible));
    }

    @Override
    public void expectElementsClickable(final By locator, final boolean reverse) {
        $$(locator).forEach(item -> performValidationForSelenideElement(item, reverse,
                Condition.and(CLICKABLE_CONDITION, Condition.visible, Condition.enabled)));
    }

    @Override
    public void expectElementsPresent(final By locator, final boolean reverse) {
        $$(locator).forEach(item -> performValidationForSelenideElement(item, reverse, Condition.exist));
    }

    @Override
    public void expectElementsHaveCssValue(final By locator, final boolean reverse, final String cssName,
            final String expectedCssValue) {
        final Condition condition = Condition.cssValue(cssName, expectedCssValue);
        $$(locator).forEach(item -> performValidationForSelenideElement(item, reverse, condition));
    }

    @Override
    public void expectElementsHaveAttribute(final By locator, final boolean reverse, final String attributeName) {
        final Condition condition = Condition.attribute(attributeName);
        $$(locator).forEach(item -> performValidationForSelenideElement(item, reverse, condition));
    }

    @Override
    public void expectElementsHaveAttributeValue(final By locator, final boolean reverse,
            final String attributeName, final String expectedAttributeValue) {
        final Condition condition = Condition.attribute(attributeName, expectedAttributeValue);
        $$(locator).forEach(item -> performValidationForSelenideElement(item, reverse, condition));
    }

    @Override
    public void expectElementsHaveAttributeValueMatched(final By locator, final boolean reverse,
            final String attributeName, final String attributeRegexp) {
        final Condition condition = Condition.attributeMatching(attributeName, attributeRegexp);
        $$(locator).forEach(item -> performValidationForSelenideElement(item, reverse, condition));
    }

    private void performElementValidation(final By locator, final boolean reverse, final Condition condition) {
        performValidationForSelenideElement($(locator), reverse, condition);
    }

    private void performElementsValidation(final By locator, final CollectionCondition condition) {
        $$(locator).shouldBe(condition);
    }

    private void performValidationForSelenideElement(final SelenideElement element, final boolean reverse, final Condition condition) {
        element.shouldBe(reverse ? Condition.not(condition) : condition);
    }
}
