package com.staf.kwd.library.steps;

import com.staf.kwd.library.wait.ConditionFactory;
import com.staf.kwd.library.wait.ConditionValidations;
import com.staf.kwd.locator.LocatorStrategyFactory;
import io.cucumber.java.en.Then;

import static com.staf.kwd.storage.ObjectsStorage.findInStorage;

public class Validations {
    @Then("^I expect '(.*)' to be (not |)visible$")
    public void expectElementVisible(final String alias, final String not) {
        ConditionFactory.executeElementCondition(ConditionValidations.VISIBLE, LocatorStrategyFactory.buildBy(alias), not.isEmpty());
    }

    @Then("^I expect '(.*)' to be (not |)clickable$")
    public void expectElementClickable(final String alias, final String not) {
        ConditionFactory.executeElementCondition(ConditionValidations.CLICKABLE, LocatorStrategyFactory.buildBy(alias), not.isEmpty());
    }

    @Then("^I expect '(.*)' to be (not |)present")
    public void expectElementPresent(final String alias, final String not) {
        ConditionFactory.executeElementCondition(ConditionValidations.PRESENT, LocatorStrategyFactory.buildBy(alias), not.isEmpty());
    }

    @Then("^I expect '(.*)' has (not |)css value (.*)$")
    public void expectElementHasCssValue(final String alias, final String not, final String value) {
        ConditionFactory.executeElementCondition(ConditionValidations.HAS_CSS_VALUE, LocatorStrategyFactory.buildBy(alias), not.isEmpty(), value);
    }

    @Then("^I expect '(.*)' has (not |)text value (.*)$")
    public void expectElementContainsTextValue(final String alias, final String not, final String value) {
        ConditionFactory.executeElementCondition(ConditionValidations.CONTAINS_TEXT, LocatorStrategyFactory.buildBy(alias), not.isEmpty(), value);
    }

    @Then("^I expect '(.*)' has (not |)exact text value '(.*)'$")
    public void expectElementHasTextValue(final String alias, final String not, final String value) {
        ConditionFactory.executeElementCondition(ConditionValidations.EXACT_TEXT, LocatorStrategyFactory.buildBy(alias), not.isEmpty(), value);
    }

    @Then("^I expect '(.*)' has (not |)(.*) attribute$")
    public void expectElementHasAttribute(final String alias, final String not) {
        ConditionFactory.executeElementCondition(ConditionValidations.HAS_ATTRIBUTE, LocatorStrategyFactory.buildBy(alias), not.isEmpty());
    }

    @Then("^I expect '(.*)' has (not |)(.*) attribute value (.*)$")
    public void expectElementHasAttributeValue(final String alias, final String not, final String value) {
        ConditionFactory.executeElementCondition(ConditionValidations.HAS_ATTRIBUTE_VALUE, LocatorStrategyFactory.buildBy(alias), not.isEmpty(), value);
    }

    @Then("^I expect '(.*)' has (not |)(.*) attribute value matched (.*)$")
    public void expectElementHasAttributeValueMatched(final String alias, final String not, final String value) {
        ConditionFactory.executeElementCondition(ConditionValidations.ATTRIBUTE_MATCHING, LocatorStrategyFactory.buildBy(alias), not.isEmpty(), value);
    }

    @Then("I expect url has {string}")
    public void expectURL(final String value) {
        ConditionFactory.executeDriverCondition(ConditionValidations.URL_EXACT, false, value);
    }

    @Then("I expect page title has {string}")
    public void expectPageTitle(final String value) {
        ConditionFactory.executeDriverCondition(ConditionValidations.PAGE_TITLE_EXACT, false, value);
    }

    @Then("^I expect number of elements in '(.*)' collection to be equal (.*)$")
    public void expectNumberOfElementsIsEqual(final String alias, final String value) {
        ConditionFactory.executeElementCondition(ConditionValidations.COUNT_OF_ELEMENTS_EQUAL, LocatorStrategyFactory.buildBy(alias), false, value);
    }

    @Then("^I expect number of elements in '(.*)' collection to be above (.*)$")
    public void expectNumberOfElementsIsAbove(final String alias, final String value) {
        ConditionFactory.executeElementCondition(ConditionValidations.COUNT_OF_ELEMENTS_ABOVE, LocatorStrategyFactory.buildBy(alias), false, value);
    }

    @Then("^I expect number of elements in '(.*)' collection to be below (.*)$")
    public void expectNumberOfElementsIsBelow(final String alias, final String value) {
        ConditionFactory.executeElementCondition(ConditionValidations.COUNT_OF_ELEMENTS_BELOW, LocatorStrategyFactory.buildBy(alias), false, value);
    }

    @Then("^I expect text of elements in '(.*)' collection (not |)equals to '(.*)'$")
    public void expectElementsHaveText(final String alias, final String not, final String values) {
        final String resolvedValues = findInStorage(values);
        final String[] expectedValues = resolvedValues.split(",\\s*"); // Split by comma and trim spaces

        ConditionFactory.executeElementCondition(ConditionValidations.ELEMENTS_EXACT_TEXT, LocatorStrategyFactory.buildBy(alias), not.isEmpty(), expectedValues);
    }

    @Then("^I expect text of elements in '(.*)' collection (not |)contains '(.*)'$")
    public void expectElementsContainsText(final String alias, final String not, final String value) {
        ConditionFactory.executeElementCondition(ConditionValidations.ELEMENTS_CONTAINS_TEXT, LocatorStrategyFactory.buildBy(alias), not.isEmpty(), value);
    }

    @Then("^I expect every element in '(.*)'collection to be (not |)visible$")
    public void expectElementsVisible(final String alias, final String not) {
        ConditionFactory.executeElementCondition(ConditionValidations.ELEMENTS_VISIBLE, LocatorStrategyFactory.buildBy(alias), not.isEmpty());
    }

    @Then("^I expect every element in '(.*)' collection to be (not |)clickable$")
    public void expectElementsClickable(final String alias, final String not) {
        ConditionFactory.executeElementCondition(ConditionValidations.ELEMENTS_CLICKABLE, LocatorStrategyFactory.buildBy(alias), not.isEmpty());
    }

    @Then("^I expect every element in '(.*)' collection to be (not |)present")
    public void expectElementsPresent(final String alias, final String not) {
        ConditionFactory.executeElementCondition(ConditionValidations.ELEMENTS_PRESENT, LocatorStrategyFactory.buildBy(alias), not.isEmpty());
    }

    @Then("^I expect every element in '(.*)' collection has (not |)css value (.*)$")
    public void expectElementsHaveCssValue(final String alias, final String not, final String value) {
        ConditionFactory.executeElementCondition(ConditionValidations.ELEMENTS_HAVE_CSS_VALUE, LocatorStrategyFactory.buildBy(alias), not.isEmpty(), value);
    }

    @Then("^I expect every element in '(.*)' collection has (not |)(.*) attribute$")
    public void expectElementsHaveAttribute(final String alias, final String not) {
        ConditionFactory.executeElementCondition(ConditionValidations.ELEMENTS_HAVE_ATTRIBUTE, LocatorStrategyFactory.buildBy(alias), not.isEmpty());
    }

    @Then("^I expect every element in '(.*)' collection has (not |)(.*) attribute value (.*)$")
    public void expectElementsHaveAttributeValue(final String alias, final String not, final String value) {
        ConditionFactory.executeElementCondition(ConditionValidations.ELEMENTS_HAVE_ATTRIBUTE_VALUE,
                LocatorStrategyFactory.buildBy(alias), not.isEmpty(), value);
    }

    @Then("^I expect every element in '(.*)' collection has (not |)(.*) attribute value matched (.*)$")
    public void expectElementsHaveAttributeValueMatched(final String alias, final String not, final String value) {
        ConditionFactory.executeElementCondition(ConditionValidations.ATTRIBUTE_MATCHING, LocatorStrategyFactory.buildBy(alias), not.isEmpty(), value);
    }
}
