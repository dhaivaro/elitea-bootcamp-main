package com.staf.ui.core;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.staf.common.property.Configuration;
import com.staf.common.util.WaitUtils;
import com.staf.ui.core.exception.ElementInitializeException;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import lombok.extern.slf4j.Slf4j;

/**
 * Class for allocation of any required number of custom elements
 *
 * @param <E> custom element type
 * @param <P> page context
 */
@Slf4j
public final class ElementList<E extends AbstractElement, P extends AbstractPage> {
    private static final String ELEMENTS_WAIT_PREDICATE_MESSAGE = "Wait for elements by predicate";

    private final ElementsCollection context;

    private final Class<E> type;
    private final P pageContext;

    public ElementList(final ElementsCollection elementsList, final P pageContext, final Class<E> type) {
        this.context = elementsList;
        this.type = type;
        this.pageContext = pageContext;
    }

    /**
     * Returns list of custom elements
     *
     * @return custom elements' list
     */
    public List<E> getElements() {
        // re-init in case something appears on the page
        return fixedStream(false)
                .map(element -> {
                    try {
                        // standard constructor to initiate custom element
                        return type.getConstructor(SelenideElement.class, AbstractPage.class)
                                .newInstance(element, pageContext);
                    } catch (NoSuchMethodException
                            | SecurityException
                            | InstantiationException
                            | IllegalAccessException
                            | IllegalArgumentException
                            | InvocationTargetException e) {
                        throw new ElementInitializeException(
                                "Unable to create instance of element: %s", e.getMessage());
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Returns the list of elements per predicate
     *
     * @param predicate custom element's predicate
     * @return filtered elements
     */
    public List<E> getElements(final Predicate<E> predicate) {
        log.info("Search for elements by predicate: {}", predicate);
        return getElements().stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * Returns the list of elements when its number exceeds expected value
     *
     * @param expectedNumber expected number of custom elements
     * @return list of elements
     */
    public List<E> getElements(final int expectedNumber) {
        waitForElements(
                expectedNumber,
                Configuration.get().pageWait(),
                Configuration.get().elementPollWait());
        return getElements();
    }

    /**
     * Returns list of SelenideElements
     *
     * @return list of SelenideElements
     */
    public List<SelenideElement> getSelenideElements() {
        return fixedStream(false).collect(Collectors.toList());
    }

    /**
     * Verifies if any entry matches predefined predicate
     *
     * @param elementPredicate custom element's predicate
     * @param isAll if all entries should satisfy to the predicate
     * @return result of verification
     */
    public boolean is(final Predicate<E> elementPredicate, final boolean isAll) {
        return isAll
                ? getElements().stream().allMatch(elementPredicate)
                : getElements().stream().anyMatch(elementPredicate);
    }

    public P waitForElements(final int expectedNumber) {
        return waitForElements(
                expectedNumber,
                Configuration.get().pageWait(),
                Configuration.get().elementPollWait());
    }

    public P waitForElements(final int expectedNumber, final Duration timeout, final Duration pollInterval) {
        log.info("Wait for elements quantity become equal or more to: {}", expectedNumber);
        WaitUtils.waitFor(
                () -> getElements().size() >= expectedNumber, timeout, pollInterval, ELEMENTS_WAIT_PREDICATE_MESSAGE);
        return pageContext;
    }

    public P waitForElements(final Predicate<E> predicate) {
        return waitForElements(
                predicate, Configuration.get().pageWait(), Configuration.get().elementPollWait());
    }

    public P waitForElements(final Predicate<E> predicate, final Duration timeout, final Duration pollInterval) {
        log.info("Wait for elements that matches predicate: {}", predicate);
        WaitUtils.waitFor(() -> is(predicate, false), timeout, pollInterval, ELEMENTS_WAIT_PREDICATE_MESSAGE);
        return pageContext;
    }

    /**
     * Returns FIXED stream of actually found elements
     *
     * @param parallel if true then the returned stream is a parallel stream; if false the returned
     *     stream is a sequential stream
     * @return stream of SelenideElement
     */
    public Stream<SelenideElement> fixedStream(final boolean parallel) {
        return StreamSupport.stream(context.asFixedIterable().spliterator(), parallel);
    }

    /**
     * Returns DYNAMIC stream of actually found elements
     *
     * @param parallel if true then the returned stream is a parallel stream; if false the returned
     *     stream is a sequential stream
     * @return stream of SelenideElement
     */
    public Stream<SelenideElement> dynamicStream(final boolean parallel) {
        return StreamSupport.stream(context.asDynamicIterable().spliterator(), parallel);
    }
}
