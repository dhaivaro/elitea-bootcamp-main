package com.staf.kwd.locator.impl;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.impl.CollectionSource;
import com.staf.common.exception.AqaException;
import com.staf.common.util.ReflectionsUtils;
import com.staf.common.util.RegexpUtils;
import com.staf.kwd.locator.LocatorStrategy;
import com.staf.kwd.locator.impl.exception.PageObjectClassInstantiationException;
import com.staf.ui.core.ElementList;
import lombok.Getter;
import org.openqa.selenium.By;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.List;

import static com.codeborne.selenide.Selenide.page;

/**
 * Locator strategy using Page Object elements
 */
@Getter
public class PageObjectLocatorStrategy implements LocatorStrategy {
    // pageObject -> element
    private final String strategyRegexp = "(.*) -> (.*)";

    @Override
    public By buildBy(final String locatorString) {
        final String pageObject = RegexpUtils.getStringByRegexp(locatorString, strategyRegexp, 1);
        final String elementName = RegexpUtils.getStringByRegexp(locatorString, strategyRegexp, 2);
        final List<String> classes = ReflectionsUtils.getFullClassNames(pageObject);
        if (classes.size() > 1) {
            throw new AqaException("More than 1 class is found for %s classname", pageObject);
        }

        try {
            final Class<?> pageObjectClass = Class.forName(classes.get(0));
            final Object instance = page(pageObjectClass);
            final Field elementField = pageObjectClass.getDeclaredField(elementName);
            elementField.setAccessible(true);
            // Process ElementsCollection by Selenide
            if (ElementsCollection.class.isAssignableFrom(elementField.getType())){
                final ElementsCollection collection = (ElementsCollection) elementField.get(instance);

                Field collectionField = ElementsCollection.class.getDeclaredField("collection");
                collectionField.setAccessible(true);
                final CollectionSource collectionSource = (CollectionSource) collectionField.get(collection);

                // Get the original selector before indexing
                final String originalSelector = collectionSource.getSearchCriteria();
                // Remove the [number] suffix that Selenide adds
                final String searchCriteria = originalSelector.replaceAll("\\[\\d+\\]$", "");

                // Create temporary wrapper with proxy
                final SelenideElement cleanElement = (SelenideElement) Proxy.newProxyInstance(
                    SelenideElement.class.getClassLoader(),
                    new Class[]{SelenideElement.class},
                    (proxy, method, args) -> {
                        // pass the cleaned search criteria
                        if ("getSearchCriteria".equals(method.getName())) {
                            return searchCriteria;
                        }
                        // delegate the rest invocations to the original element
                        // TODO try first()
                        return method.invoke(collection.first(), args);
                    }
                );

                return getByFromSelenideElement(cleanElement);
            } else if (ElementList.class.isAssignableFrom(elementField.getType())) {
                ElementList elementList = (ElementList) elementField.get(instance);
                elementList.waitForElements(1);
                List selenideElements = elementList.getSelenideElements();
                SelenideElement firstElement = (SelenideElement) selenideElements.get(0);
                String searchCriteria = firstElement.getSearchCriteria();
                // Remove Selenide metadata
                searchCriteria = searchCriteria.replaceAll("(\\.snapshot\\([^)]*\\)|\\[\\d+\\]|\\.cached)", "");

                return parseSearchCriteriaToBy(searchCriteria);
            } else {
                return getByFromSelenideElement((SelenideElement) elementField.get(instance));
            }
        } catch (ClassNotFoundException e) {
            throw new PageObjectClassInstantiationException("Page object class not found: " + classes.get(0), e);
        } catch (NoSuchFieldException e) {
            throw new PageObjectClassInstantiationException(String.format("Element field '%s' not found in %s", elementName, pageObject), e);
        } catch (IllegalAccessException e) {
            throw new PageObjectClassInstantiationException(String.format("Cannot access field '%s' in %s", elementName, pageObject), e);
        }
    }

    private By parseSearchCriteriaToBy(final String searchCriteria) {
        // Extract the locator type and value from the search criteria
        if (searchCriteria.matches(".*: .*")) {
            final String[] parts = searchCriteria.split(": ", 2);
            final String locatorType = parts[0].trim();
            final String locatorValue = parts[1].trim();

            // Create a By object based on the locator type and value
            final By by;
            switch (locatorType.toLowerCase()) {
                case "by.id":
                    by = By.id(locatorValue);
                    break;
                case "by.name":
                    by = By.name(locatorValue);
                    break;
                case "by.class":
                case "by.classname":
                    by = By.className(locatorValue);
                    break;
                case "by.xpath":
                    by = By.xpath(locatorValue);
                    break;
                case "by.linktext":
                    by = By.linkText(locatorValue);
                    break;
                case "by.partiallinktext":
                    by = By.partialLinkText(locatorValue);
                    break;
                case "by.tagname":
                    by = By.tagName(locatorValue);
                    break;
                case "by.cssselector":
                    by = By.cssSelector(locatorValue);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported locator type: " + locatorType);
            }
            return by;
        } else {
            // If no explicit type is found, treat it as a CSS selector
            return By.cssSelector(searchCriteria);
        }
    }

    private By getByFromSelenideElement(final SelenideElement selenideElement) {
        String searchCriteria = selenideElement.getSearchCriteria();
        // Remove array index at the end of the selector (e.g., [0]) if present
        searchCriteria = searchCriteria.replaceAll("\\[\\d+\\]$", "");
        return parseSearchCriteriaToBy(searchCriteria);
    }
}
