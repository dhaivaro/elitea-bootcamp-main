package com.staf.examples.demo.kwd.runner;

import com.staf.kwd.runner.CucumberTestRunner;
import org.testng.annotations.AfterMethod;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;

public class KwdRunner extends CucumberTestRunner {
    @AfterMethod
    public void quitBrowser() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            // Cleanup in case session persist in the same storage between test runs
            Selenide.clearBrowserCookies();
            Selenide.clearBrowserLocalStorage();
            Selenide.executeJavaScript("sessionStorage.clear();");
            // Completely quit the browser
            Selenide.closeWebDriver();
        }
    }
}
