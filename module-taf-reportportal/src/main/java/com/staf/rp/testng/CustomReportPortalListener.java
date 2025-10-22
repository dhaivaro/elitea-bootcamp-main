package com.staf.rp.testng;

import com.epam.reportportal.listeners.ItemStatus;
import com.epam.reportportal.service.Launch;
import com.epam.reportportal.service.ReportPortalClient;
import com.epam.reportportal.service.tree.ItemTreeReporter;
import com.epam.reportportal.service.tree.TestItemTree;
import com.epam.reportportal.testng.ReportPortalTestNGListener;
import com.epam.reportportal.testng.util.ItemTreeUtils;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.attribute.ItemAttributesRQ;
import com.staf.driver.core.DriverManager;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.testng.ITestResult;

import java.util.Calendar;
import java.util.Set;

import static com.epam.reportportal.testng.TestNGService.ITEM_TREE;
import static com.staf.rp.util.ReportPortalUtils.log;
import static java.util.Optional.ofNullable;

@Slf4j
public class CustomReportPortalListener extends ReportPortalTestNGListener {
    private static final String ERROR_MESSAGE_TEXT = "Test failure";

    @Override
    public void onTestFailure(final ITestResult testResult) {
        log.info(ERROR_MESSAGE_TEXT);
        Try.runRunnable(() -> log(DriverManager.getInstance().getCurrentDriver().screenshot(OutputType.BYTES), ERROR_MESSAGE_TEXT));
        onTestFinish(testResult);
        super.onTestFailure(testResult);
    }

    @Override
    public void onTestSuccess(final ITestResult testResult) {
        onTestFinish(testResult);
        super.onTestSuccess(testResult);
    }

    @Override
    public void onTestSkipped(final ITestResult testResult) {
        onTestFinish(testResult);
        super.onTestSkipped(testResult);
    }

    /**
     * Close driver and add SLID attribute to the ReportPortal test result item
     *
     * @param testResult test result
     */
    private void onTestFinish(final ITestResult testResult) {
        final Try<SessionId> sessionId = Try.of(this::getSessionId);
        sessionId
                .onSuccess(extractedSession -> {
                    log.info("Session id: {}", extractedSession);
                    DriverManager.getInstance().getCurrentDriver().closeWebDriver();
                    Try.runRunnable(() -> ItemTreeUtils.retrieveLeaf(testResult, ITEM_TREE)
                                    .ifPresent(testResultLeaf -> addSLID(
                                            testResultLeaf,
                                            testResult,
                                            ofNullable(extractedSession).map(SessionId::toString).orElse(""))))
                            .onFailure(failure -> log.error("Unable to add SLID to RP run: {}", failure.getMessage()));
                })
                .onFailure(failure -> log.info("Session id cannot be extracted: {}", failure.getMessage()));
    }

    /**
     * Returns Report Portal client
     *
     * @return Report Portal client
     */
    private static ReportPortalClient getClient() {
        return ofNullable(Launch.currentLaunch())
                .map(Launch::getClient)
                .orElseThrow(() -> new IllegalStateException("Unable to get Report Portal Client"));
    }

    /**
     * Extracts session id from the current driver
     *
     * @return session id
     */
    private SessionId getSessionId() {
        return ((RemoteWebDriver) DriverManager.getInstance().getCurrentDriver().getWebDriver()).getSessionId();
    }

    /**
     * Adds SLID attribute to the ReportPortal test result item
     *
     * @param testResultLeaf report portal result item
     * @param testResult     test result
     * @param sessionID      sauce labs job id
     */
    private void addSLID(
            final TestItemTree.TestItemLeaf testResultLeaf, final ITestResult testResult, final String sessionID) {
        final FinishTestItemRQ finishTestItemRQ = new FinishTestItemRQ();
        finishTestItemRQ.setStatus(testResult.isSuccess() ? ItemStatus.PASSED.name() : ItemStatus.FAILED.name());
        finishTestItemRQ.setEndTime(Calendar.getInstance().getTime());
        finishTestItemRQ.setAttributes(Set.of(new ItemAttributesRQ("SLID", sessionID)));
        ItemTreeReporter.finishItem(getClient(), finishTestItemRQ, ITEM_TREE.getLaunchId(), testResultLeaf)
                .cache()
                .blockingGet();
    }
}
