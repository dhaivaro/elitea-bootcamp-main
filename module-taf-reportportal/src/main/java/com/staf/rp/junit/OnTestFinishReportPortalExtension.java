package com.staf.rp.junit;

import com.epam.reportportal.junit5.ReportPortalExtension;
import com.epam.reportportal.listeners.ItemStatus;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.staf.common.exception.AqaException;
import com.staf.driver.core.DriverManager;
import com.staf.rp.util.ReportPortalUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/** Custom extension for Report Portal: adjust logging, screenshot logic on result item upload */
@Slf4j
public final class OnTestFinishReportPortalExtension extends ReportPortalExtension {
    private static final List<String> EXECUTED_TESTS = new ArrayList<>();

    @Override
    public void finishTestItem(@Nonnull final ExtensionContext context, @Nonnull final FinishTestItemRQ rq) {
        // take screenshot on failure
        if (rq.getStatus() != null && rq.getStatus().equalsIgnoreCase(ItemStatus.FAILED.toString())) {
            takeScreenshot("Test failure");
        }

        // TODO: move to separate extension and check if it works with multiple extensions
        // added to the tests
        // add rerun attribute to test if rerun
        if (EXECUTED_TESTS.contains(context.getDisplayName())) {
            final String description;
            if (rq.getDescription() == null) {
                description = "";
            } else {
                description = rq.getDescription();
            }
            rq.setDescription("RERUN ".concat(description));
        } else {
            EXECUTED_TESTS.add(context.getDisplayName());
        }

        // attachVideo();
        super.finishTestItem(context, rq);
    }

    private void takeScreenshot() {
        takeScreenshot("Take screenshot on test finish");
    }

    private void takeScreenshot(final String message) {
        try {
            ReportPortalUtils.log(DriverManager.getInstance().getCurrentDriver().screenshot(OutputType.BYTES), message);
        } catch (AqaException e) {
            log.error("Unable to take screenshot: {}", e.getMessage());
        }
    }

    //    TODO: review logic and introduce following:
    //     1) introduction of new properties: enableVideo
    //     2) video recording publishing and attachment to RP launch;
    //    /**
    //     * Attaches video to RP launch (only for selenoid based infra)
    //     * <p>
    //     * NOTE: capabilities.enableVideo = true && selenide.selenoid.ui is not empty
    //     */
    //    private void attachVideo() {
    //        try {
    //            final String selenoidUrl = Configuration.remote;
    //            if (DriverProperties.get().enableVideo() && StringUtils.isNotEmpty(selenoidUrl)) {
    //                final String videoPath = String.format("%s/video/%s.mp4",
    // selenoidUrl.replace("wd/hub", ""),
    //                        ((RemoteWebDriver)
    // WebDriverRunner.getWebDriver()).getSessionId().toString());
    //                log.info("<a href=\"{}\" target=\"_blank\">Video</a>", videoPath);
    //                // close driver and finalize video recording
    //                WebDriverRunner.driver().close();
    //            }
    //        } catch (Exception e) {
    //            log.error("Unable to attach video: {}", e.getMessage());
    //        }
    //
    //    }
}
