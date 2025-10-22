package com.staf.kwd.library.wait;

import com.staf.common.exception.AqaException;
import com.staf.driver.config.DriverConfig;
import com.staf.kwd.library.wait.impl.SelenideValidations;
import lombok.NoArgsConstructor;
import org.openqa.selenium.By;

@NoArgsConstructor
public class ConditionFactory {
    private static IValidations getValidations() {
        switch (DriverConfig.get().holderType()) {
            case SERENITY:
                throw new AqaException("Validation isn't implemented for 'Serenity' driver holder");
            case SELENIDE:
            default:
                return new SelenideValidations();
        }
    }

    //CHECKSTYLE:OFF
    // TODO: redesign according to https://checkstyle.sourceforge.io/checks/metrics/javancss.html
    // TODO: fix hack with not
    public static void executeElementCondition(final ConditionValidations validationType, final By locator, final boolean notEmpty, final String... args) {
        final IValidations validations = getValidations();
        final boolean reverse = !notEmpty;
        switch (validationType) {
            case VISIBLE:
                validations.expectToBeVisible(locator, reverse);
                break;
            case HAS_ATTRIBUTE:
                validations.expectToHaveAttribute(locator, reverse, args[0]);
                break;
            case HAS_CSS_VALUE:
                validations.expectToHaveCssValue(locator, reverse, args[0], args[1]);
                break;
            case HAS_ATTRIBUTE_VALUE:
                validations.expectToHaveAttributeValue(locator, reverse, args[0], args[1]);
                break;
            case ATTRIBUTE_MATCHING:
                validations.expectToHaveAttributeValueMatched(locator, reverse, args[0], args[1]);
                break;
            case URL_EXACT:
                validations.expectURL(reverse, args[0]);
                break;
            case PAGE_TITLE_EXACT:
                validations.expectPageTitle(reverse, args[0]);
                break;
            case EXACT_TEXT:
                validations.expectToHaveExactText(locator, reverse, args[0]);
                break;
            case CONTAINS_TEXT:
                validations.expectToContainsText(locator, reverse, args[0]);
                break;
            case CLICKABLE:
                validations.expectToBeClickable(locator, reverse);
                break;
            case PRESENT:
                validations.expectToBePresent(locator, reverse);
                break;
            case ELEMENTS_VISIBLE:
                validations.expectElementsVisible(locator, reverse);
                break;
            case ELEMENTS_CLICKABLE:
                validations.expectElementsClickable(locator, reverse);
                break;
            case ELEMENTS_PRESENT:
                validations.expectElementsPresent(locator, reverse);
                break;
            case ELEMENTS_HAVE_CSS_VALUE:
                validations.expectElementsHaveCssValue(locator, reverse, args[0], args[1]);
                break;
            case ELEMENTS_HAVE_ATTRIBUTE:
                validations.expectElementsHaveAttribute(locator, reverse, args[0]);
                break;
            case ELEMENTS_HAVE_ATTRIBUTE_VALUE:
                validations.expectElementsHaveAttributeValue(locator, reverse, args[0], args[1]);
                break;
            case ELEMENTS_ATTRIBUTE_MATCHING:
                validations.expectElementsHaveAttributeValueMatched(locator, reverse, args[0], args[1]);
                break;
            case ELEMENTS_EXACT_TEXT:
                validations.expectElementsHaveExactText(locator, reverse, args[0]);
                break;
            case ELEMENTS_CONTAINS_TEXT:
                validations.expectElementsContainsText(locator, reverse, args[0]);
                break;
            case COUNT_OF_ELEMENTS_EQUAL:
                validations.expectCountOfElementsEqual(locator, args[0]);
                break;
            case COUNT_OF_ELEMENTS_ABOVE:
                validations.expectCountOfElementsAbove(locator, args[0]);
                break;
            case COUNT_OF_ELEMENTS_BELOW:
                validations.expectCountOfElementsBelow(locator, args[0]);
                break;
            default:
                throw new AqaException("Condition not implemented.");
        }
    }

    public static void executeDriverCondition(final ConditionValidations validationType, final boolean reverse, final String... args) {
        executeElementCondition(validationType, null, reverse, args);
    }
}
