package com.staf.common.util;

import com.staf.common.property.Configuration;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionFactory;
import org.awaitility.core.ConditionTimeoutException;

@UtilityClass
@Slf4j
public class WaitUtils {
    public String DEFAULT_NAME = "undefined";

    public <T> T waitFor(
            final Callable<T> task,
            final Predicate<T> predicate,
            final Duration timeout,
            final Duration pollingInterval,
            final String... name) {
        return waitFor(
                Awaitility.await(name.length == 0 ? DEFAULT_NAME : name[0])
                        .pollInSameThread()
                        .pollDelay(Duration.ZERO)
                        .pollInterval(pollingInterval)
                        .timeout(timeout),
                task,
                predicate);
    }

    public <T> T waitFor(
            final Callable<T> task, final Predicate<T> predicate, final Duration timeout, final String... name) {
        return waitFor(task, predicate, timeout, Configuration.get().elementPollWait(), name);
    }

    public <T> T waitFor(
            final ConditionFactory conditionFactory, final Callable<T> task, final Predicate<T> predicate) {
        return conditionFactory.until(task, predicate);
    }

    public <T> T waitFor(final Callable<T> task, final Predicate<T> predicate, final String... name) {
        return waitFor(
                task,
                predicate,
                Configuration.get().pageWait(),
                Configuration.get().elementPollWait(),
                name);
    }

    public void waitFor(
            final Callable<Boolean> task,
            final Duration timeout,
            final Duration pollingInterval,
            final String... name) {
        Awaitility.await(name.length == 0 ? DEFAULT_NAME : name[0])
                .pollInSameThread()
                .pollDelay(Duration.ZERO)
                .pollInterval(pollingInterval)
                .timeout(timeout)
                .until(task);
    }

    public void waitFor(final Callable<Boolean> task, final String... name) {
        waitFor(task, false, name);
    }

    public void waitFor(
            final Callable<Boolean> task, final Boolean skipConditionTimeoutException, final String... name) {
        if (skipConditionTimeoutException) {
            try {
                waitFor(
                        task,
                        Configuration.get().elementWait(),
                        Configuration.get().elementPollWait(),
                        name);
            } catch (ConditionTimeoutException e) {
                log.info("Condition hasn't been achieved. Safe-wait completed.");
            }
        } else {
            waitFor(task, Configuration.get().elementWait(), Configuration.get().elementPollWait(), name);
        }
    }

    /**
     * Waiting for some time without any condition. Replacement for Thread.sleep().
     *
     * @param timeout - timeout duration
     * @param name - wait name, not obligatory
     */
    public void waitFor(final Duration timeout, final String... name) {
        Awaitility.await(name.length == 0 ? DEFAULT_NAME : name[0])
                .timeout(timeout.plus(Duration.ofSeconds(1)))
                .pollDelay(timeout)
                .pollInSameThread()
                .until(() -> true);
    }

    public boolean waitForCondition(
            final Callable<Boolean> task, final Boolean skipConditionTimeoutException, final String... name) {
        boolean result = true;
        if (skipConditionTimeoutException) {
            try {
                waitFor(
                        task,
                        Configuration.get().elementWait(),
                        Configuration.get().elementPollWait(),
                        name);
            } catch (ConditionTimeoutException e) {
                e.printStackTrace();
                result = false;
            }
        } else {
            waitFor(task, Configuration.get().elementWait(), Configuration.get().elementPollWait(), name);
        }
        return result;
    }
}
