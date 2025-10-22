package com.staf.ui.core;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.staf.common.exception.AqaException;
import com.staf.common.property.Configuration;
import com.staf.common.util.WaitUtils;
import com.staf.ui.core.exception.ElementInitializeException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.function.Predicate;


/**
 * Core abstract element used in order to create new custom elements.
 * It has some core common logic that can be used by all custom elements.
 * Also, it contains page instance it is relevant to.
 */
@Slf4j
public abstract class AbstractElement<P extends AbstractPage, T extends AbstractElement<P, T>> {
    // pageContext is a page instance where custom element is located
    protected P pageContext;
    // element is a context (main) element of the custom element
    protected SelenideElement element;

    /**
     * Constructor for initialization of the element that introduces page instance and context element
     *
     * @param element context element
     * @param page    page instance
     */
    public AbstractElement(final SelenideElement element, final P page) {
        this.pageContext = page;
        // can be re-declared on custom element level if needed
        this.element = element;
    }

    @SuppressWarnings("unchecked")
    public static <E extends AbstractElement<P, E>, P extends AbstractPage> E init(
            final SelenideElement element, final P page, final Class<E> elementType) {
        try {
            // find required constructor: looking for constructor with 2 parameters:
            // SelenideElement and assignable from
            // AbstractPage.class
            final Constructor<?> constructor = Arrays.stream(elementType.getDeclaredConstructors())
                    .filter(ctor -> {
                        final Class<?>[] parameters = ctor.getParameterTypes();
                        return parameters.length == 2
                                && parameters[0] == SelenideElement.class
                                && parameters[1].isAssignableFrom(AbstractPage.class);
                    })
                    .findFirst()
                    .orElseThrow(() -> new AqaException(
                            "Unable to find required constructor. Make sure it extends AbstractElement and "
                                    + " it has a constructor (SelenideElement, AbstractPage)"));

            return (E) constructor.newInstance(element, page);
        } catch (ReflectiveOperationException | IllegalArgumentException e) {
            log.error(e.getMessage());
            throw new ElementInitializeException("Unable to initialize a custom element "
                    + elementType.getName() + ".", e);
        }
    }

    /**
     * Verifies is context element is exist and visible
     *
     * @return false if element is not visible
     */
    public boolean isPresent() {
        return this.element.isDisplayed();
    }

    /**
     * Method allows to validate context element against selenide condition
     *
     * @param condition selenide condition
     * @return false if condition doesn't pass
     */
    public final boolean is(final Condition condition) {
        return this.element.is(condition);
    }

    /**
     * Extracts context element
     *
     * @return context element
     */
    public final SelenideElement getElement() {
        return this.element;
    }

    /**
     * Extracts page context
     *
     * @return page instance element refers to
     */
    public final P getPage() {
        return this.pageContext;
    }

    /**
     * Returns custom element instance
     *
     * @return custom element
     */
    public T get() {
        return (T) this;
    }

    /**
     * Wait for the presence of the custom element
     *
     * @return custom element
     */
    public T waitForElement() {
        return waitForElement(Configuration.get().elementWait());
    }

    /**
     * Wait for the presence of the custom element
     *
     * @param duration timeout
     * @return custom element
     */
    public T waitForElement(final Duration duration) {
        final Callable<T> callable = this::get;
        final Predicate<T> elementPredicate = AbstractElement::isPresent;
        return WaitUtils.waitFor(callable, elementPredicate, duration, "element's presence");
    }
}
