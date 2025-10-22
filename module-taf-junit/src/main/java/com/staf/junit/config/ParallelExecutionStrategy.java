package com.staf.junit.config;

import com.staf.common.constant.ConfigConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfiguration;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfigurationStrategy;

@Slf4j
/**
 * Strategy for parallelization configuration Based on passed parameter "thread.count"
 *
 * <p>Details:
 * https://junit.org/junit5/docs/current/user-guide/#writing-tests-parallel-execution-config
 *
 * <p>Example: junit.jupiter.execution.parallel.enabled=true
 * junit.jupiter.execution.parallel.mode.default=same_thread
 * junit.jupiter.execution.parallel.mode.classes.default=concurrent
 * junit.jupiter.execution.parallel.config.strategy=custom
 * junit.jupiter.execution.parallel.config.custom.class=com.epam.junit.config.ParallelExecutionStrategy
 *
 * <p>Useful notes: - refer to
 * https://junit.org/junit5/docs/5.8.0/api/org.junit.platform.engine/org/junit/platform/engine/support/hierarchical/ParallelExecutionConfiguration.html
 * to get configuration details
 */
public final class ParallelExecutionStrategy
        implements ParallelExecutionConfiguration, ParallelExecutionConfigurationStrategy {

    private final String threadCountString = System.getProperty(ConfigConstant.THREAD_COUNT_VAR);
    private final int threadCount = StringUtils.isEmpty(threadCountString) ? 1 : Integer.parseInt(threadCountString);

    @Override
    public int getParallelism() {
        log.info("<Multithreading>: {}", threadCount);
        return threadCount;
    }

    @Override
    public int getMinimumRunnable() {
        return 1;
    }

    @Override
    public int getMaxPoolSize() {
        return threadCount;
    }

    @Override
    public int getCorePoolSize() {
        return threadCount;
    }

    @Override
    public int getKeepAliveSeconds() {
        return threadCount;
    }

    @Override
    public ParallelExecutionConfiguration createConfiguration(final ConfigurationParameters configurationParameters) {
        return this;
    }
}
