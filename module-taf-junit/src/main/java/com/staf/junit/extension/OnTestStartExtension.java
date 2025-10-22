package com.staf.junit.extension;

import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * JUnit Extension for interaction with entities on BeforeAll actions. Put here configuration,
 * global preconditions etc
 *
 * <p>Details: https://junit.org/junit5/docs/current/user-guide/#extensions-execution-order-overview
 */
@Slf4j
public final class OnTestStartExtension implements BeforeAllCallback {
    private static final AtomicBoolean IS_STARTED = new AtomicBoolean(false);

    @Override
    public void beforeAll(final ExtensionContext context) {
        // execute only once (used as a global precondition)
        if (IS_STARTED.compareAndSet(false, true)) {
            log.info("Global preconditions...");
            //            put some global precondition here
        }
    }
}
