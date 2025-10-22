package com.staf.testng.listener;

import com.staf.common.property.Configuration;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Retry listener for TestNG configuration. Required to retry failed tests.
 * Uses configuration parameter 'retryCount' to set retry limit.
 */
@Slf4j
public class RetryListener implements IRetryAnalyzer, IAnnotationTransformer {

    private AtomicInteger counter = new AtomicInteger(0);
    private final int retryLimit = Configuration.get().retryCount();

    @Override
    public boolean retry(final ITestResult result) {
        if (!result.isSuccess()) {
            if (counter.getAndAdd(1) < retryLimit) {
                log.info("Test failure: ", Try.of(result::getThrowable).onFailure(Throwable::printStackTrace).get());
                log.info("Retrying test: {}", result.getName());
                result.setStatus(ITestResult.FAILURE);
                return true;
            } else {
                result.setStatus(ITestResult.FAILURE);
            }
        } else {
            result.setStatus(ITestResult.SUCCESS);
        }
        return false;
    }

    @Override
    public void transform(final ITestAnnotation annotation,
                          final Class testClass,
                          final Constructor testConstructor,
                          final Method testMethod) {
        annotation.setRetryAnalyzer(RetryListener.class);
    }
}
