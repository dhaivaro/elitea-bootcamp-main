package com.staf.kwd.library.steps;

import com.codeborne.selenide.Selenide;
import com.staf.common.exception.AqaException;
import com.staf.common.util.RegexpUtils;
import com.staf.driver.core.DriverManager;
import com.staf.kwd.locator.LocatorStrategyFactory;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.codeborne.selenide.Condition.interactable;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.staf.kwd.storage.ObjectsStorage.putEntity;

@Slf4j
public class Actions {
    private final String dimensionRegex = "(\\d+)x(\\d+)";
    private final String coordinatesExceptionMessage = "Incorrect coordinates value. Pattern should be corresponded pattern: \\d+x\\d+";

    @When("I debug current page state")
    public void debugCurrentPageState() {
        String filename = "debug_" + ZonedDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS"));

        // Take screenshot
        Selenide.screenshot(filename);

        // Log debug information
        log.info("=== DEBUG INFO ===");
        log.info("Timestamp: " + Instant.now());
        log.info("Current URL: " + Selenide.webdriver().driver().url());
        log.info("Page title: " + DriverManager.getInstance().getCurrentDriver().getWebDriver().getTitle());
        log.info("Window size: " + DriverManager.getInstance().getCurrentDriver().getWebDriver().manage().window().getSize());

        // Check if specific elements exist
        log.info("Body element exists: " + $("body").exists());

        log.info("=== END DEBUG INFO ===");
    }

    /**
     * Opens provided url
     *
     * @param url - url to navigate
     * @example I open 'https://google.com'
     */
    @When("I open {string} url")
    public void openUrl(final String url) {
//        Configuration.timeout = 30000;
//        Configuration.pageLoadTimeout = 60000;
//        Configuration.pollingInterval = 500;

        open(url);
        DriverManager.getInstance().getCurrentDriver().getWebDriver().manage().timeouts().implicitlyWait(Duration.ofMillis(10000));

        Selenide.Wait().withTimeout(Duration.ofSeconds(30))
            .until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }

    @When("I save {string} text to {string} variable")
    public void saveText(final String elementLocator, final String variableName) {
        saveTextCollection(1, elementLocator, variableName);
    }

    @When("^I save '(\\d+)(?:st|nd|rd|th)' element text of '([^']*)' collection to '([^']*)' variable$")
    public void saveTextCollection(final int optionNumber, final String elementLocator, final String variableName) {
        final String extractedText = DriverManager.getInstance().getCurrentDriver().getWebDriver()
                .findElements(LocatorStrategyFactory.buildBy(elementLocator)).get(optionNumber - 1).getText();
        log.info("Following text '{}' has been extracted from the element '{}'", extractedText, elementLocator);
        putEntity(variableName, extractedText);
    }

    /**
     * Click on element by locator
     *
     * @param elementLocator locator of element
     */
    @When("I click {string}")
    public void clickElement(final String elementLocator) {
        final By resolvedLocator = LocatorStrategyFactory.buildBy(elementLocator);
        log.info("I click {}", resolvedLocator.toString());

//        WebDriverWait wait = new WebDriverWait(DriverManager.getInstance().getCurrentDriver().getWebDriver(), Duration.ofSeconds(10));
//        wait.until(ExpectedConditions.visibilityOfElementLocated(resolvedLocator));
        $(resolvedLocator).shouldBe(visible);
        $(resolvedLocator).shouldBe(interactable);

        DriverManager.getInstance().getCurrentDriver().getWebDriver().findElement(resolvedLocator).click();
    }

    @When("^I wait (\\d+) seconds?$")
    public void waitForSeconds(final int seconds) {
        try {
            log.info("Waiting for {} seconds", seconds);
            WebDriverWait wait = new WebDriverWait(DriverManager.getInstance().getCurrentDriver().getWebDriver(), Duration.ofSeconds(seconds));
            wait.until(driver -> {
                try {
                    Thread.sleep(seconds * 1000L);
                    return true;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            });
        } catch (TimeoutException e) {
            log.warn("Wait completed after {} seconds", seconds);
        }
    }
    /**
     * Clear text from the element
     *
     * @param elementLocator locator of element
     */
    @When("I clear {string}")
    public void clearElement(final String elementLocator) {
        DriverManager.getInstance().getCurrentDriver().getWebDriver().findElement(LocatorStrategyFactory.buildBy(elementLocator)).clear();
    }

    /**
     * Switch to parent frame
     */
    @When("I switch to parent frame")
    public void switchToParentFrame() {
        DriverManager.getInstance().getCurrentDriver().getWebDriver().switchTo().parentFrame();
    }

    /**
     * Switch to specific frame by number
     *
     * @param frameNumber number of the frame
     */
    @When("I switch to {int} frame")
    public void switchToFrame(final int frameNumber) {
        DriverManager.getInstance().getCurrentDriver().getWebDriver().switchTo().frame(frameNumber);
    }

    /**
     * Switch to specific frame by frame name
     *
     * @param frameName name of frame element
     */
    @When("I switch to {string} frame")
    public void switchToFrameElement(final String frameName) {
        DriverManager.getInstance().getCurrentDriver().getWebDriver().switchTo().frame(frameName);
    }

    /**
     * Switch to alert window
     */
    @When("I switch to alert")
    public void switchToAlert() {
        DriverManager.getInstance().getCurrentDriver().getWebDriver().switchTo().alert();
    }

    /**
     * Type text to element
     *
     * @param alias element to type
     * @param value value to type
     * @example I type 'wikipedia' to 'Google Input'
     */
    @When("I type {string} to {string}")
    public void typeToElement(final String value, final String alias) {
        final By resolvedLocator = LocatorStrategyFactory.buildBy(alias);
        log.info("I type {} to {}", value, resolvedLocator.toString());
        DriverManager.getInstance().getCurrentDriver().getWebDriver().findElement(resolvedLocator).sendKeys(value);
    }

    /**
     * Set specific windows size
     *
     * @param windowSize windows size value (e.g. '1024x678')
     */
    @When("I set window size {string}")
    public void setWindowSize(final String windowSize) {
        if (!windowSize.matches(dimensionRegex)) {
            throw new AqaException("Incorrect window size value. Pattern should be corresponded pattern: \\d+x\\d+");
        }
        final int height = Integer.parseInt(RegexpUtils.getStringByRegexp(windowSize, dimensionRegex, 1));
        final int width = Integer.parseInt(RegexpUtils.getStringByRegexp(windowSize, dimensionRegex, 2));
        DriverManager.getInstance().getCurrentDriver()
                .getWebDriver()
                .manage()
                .window()
                .setSize(new Dimension(width, height));
    }

    /**
     * Refresh the current page
     */
    @When("I refresh page")
    public void refreshPage() {
        DriverManager.getInstance()
                .getCurrentDriver()
                .getWebDriver()
                .navigate()
                .refresh();
    }

    /**
     * Accept the alert window
     */
    @When("I accept alert")
    public void acceptAlert() {
        DriverManager.getInstance()
                .getCurrentDriver()
                .getWebDriver()
                .switchTo()
                .alert()
                .accept();
    }

    /**
     * Dismiss the alert window
     */
    @When("I dismiss alert")
    public void dismissAlert() {
        DriverManager.getInstance()
                .getCurrentDriver()
                .getWebDriver()
                .switchTo()
                .alert()
                .dismiss();
    }

    /**
     * Type specific text to alert window
     *
     * @param text value for typing
     */
    @When("I type {string} to alert")
    public void typeTextToAlert(final String text) {
        DriverManager.getInstance()
                .getCurrentDriver()
                .getWebDriver()
                .switchTo()
                .alert()
                .sendKeys(text);
    }

    /**
     * Select option from the dropdown element
     *
     * @param option value from the option list
     * @param dropdown locator for dropdown element
     */
    @When("I select {string} option from {string} dropdown")
    public void selectOptionByText(final String option, final String dropdown) {
        final WebElement selectElement = DriverManager.getInstance()
                .getCurrentDriver()
                .getWebDriver().findElement(LocatorStrategyFactory.buildBy(dropdown));
        final Select select = new Select(selectElement);
        select.selectByValue(option);
    }

    /**
     * Select option from the dropdown element by index
     *
     * @param optionNumber index number of option
     * @param dropdown locator for dropdown element
     */
    @When("I select {int}(st|nd|rd|th) option from {string} dropdown")
    public void selectOptionByNumber(final int optionNumber, final String dropdown) {
        final WebElement selectElement = DriverManager.getInstance()
                .getCurrentDriver()
                .getWebDriver().findElement(LocatorStrategyFactory.buildBy(dropdown));
        final Select select = new Select(selectElement);
        select.selectByIndex(optionNumber - 1);
    }

    /**
     * Hover over specific element
     *
     * @param elementLocator locator of the element
     */
    @When("I hover over {string}")
    public void hoverElement(final String elementLocator) {
        final WebDriver driver = DriverManager.getInstance()
                .getCurrentDriver()
                .getWebDriver();

        final org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(driver);
        actions.moveToElement(driver.findElement(LocatorStrategyFactory.buildBy(elementLocator))).perform();
    }

    /**
     * Press keys (e.g. 'ENTER' or 'CTRL+A')
     *
     * @param keys combination of keys
     */
    @When("I press {string} key")
    public void pressKeys(final String keys) {
        final String keysRegex = "(.*)\\+(.*)";
        final WebDriver driver = DriverManager.getInstance()
                .getCurrentDriver()
                .getWebDriver();
        final org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(driver);
        if (keys.matches(keysRegex)) {
            final String firstKey = RegexpUtils.getStringByRegexp(keys, keysRegex, 1);
            final String secondKey = RegexpUtils.getStringByRegexp(keys, keysRegex, 2);
            if (secondKey.length() > 1) {
                actions.keyDown(Keys.valueOf(firstKey)).keyDown(Keys.valueOf(secondKey)).perform();
            } else {
                actions.keyDown(Keys.valueOf(firstKey)).sendKeys(secondKey).perform();
            }
        } else {
            if (keys.length() > 1) {
                actions.keyDown(Keys.valueOf(keys)).perform();
            } else {
                actions.sendKeys(keys).perform();
            }
        }
    }


    /**
     * Press keys combination n-times
     *
     * @param keys combination of keys
     * @param times amount of pressing
     */
    @When("I press {string} key {int} time(s)")
    public void pressKeys(final String keys, final int times) {
        int timesCounter = times;
        while (timesCounter-- > 0) {
            pressKeys(keys);
        }
    }

    /**
     * Open new tab
     */
    @When("I open new tab")
    public void openNewTab() {
        DriverManager.getInstance()
                .getCurrentDriver()
                .getWebDriver()
                .switchTo()
                .newWindow(WindowType.TAB);
    }

    /**
     * Switch to window by number
     * @param windowNumber number of the window
     */
    @When("I switch to {int} window")
    public void switchToWindow(final int windowNumber) {
        final WebDriver driver = DriverManager.getInstance()
                .getCurrentDriver()
                .getWebDriver();
        final List<String> windows = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(windows.get(windowNumber));
    }

    /**
     * Switch to window by name
     *
     * @param windowName name of the window
     */
    @When("I switch to {string} window")
    public void switchToWindow(final String windowName) {
        final WebDriver driver = DriverManager.getInstance()
                .getCurrentDriver()
                .getWebDriver();
        driver.switchTo().window(windowName);
    }

    /**
     * Click on element from the element collection using text value
     * @param text text value of the element from the collection
     * @param elementLocator element collection locator
     */
    @When("I click {string} text in {string} collection")
    public void clickTextInCollection(final String text, final String elementLocator) {
        final WebDriver driver = DriverManager.getInstance()
                .getCurrentDriver()
                .getWebDriver();
        driver.findElements(LocatorStrategyFactory.buildBy(elementLocator))
                .stream()
                .filter(element -> element.getText().equals(text))
                .findFirst()
                .orElseThrow(NoSuchElementException::new).click();
    }

    @When("^I click '(\\d+)(?:st|nd|rd|th)' element in '([^']*)' collection$")
    public void clickElementInCollectionByNumber(final int optionNumber, final String elementLocator) {
        DriverManager.getInstance().getCurrentDriver().getWebDriver()
                .findElements(LocatorStrategyFactory.buildBy(elementLocator)).get(optionNumber - 1).click();
    }

    /**
     * Scroll to target element by locator
     *
     * @param elementLocator locator of the element
     */
    @When("I scroll to {string}")
    public void scrollToElement(final String elementLocator) {
        final WebDriver driver = DriverManager.getInstance()
                .getCurrentDriver()
                .getWebDriver();

        final org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(driver);
        actions.scrollToElement(driver.findElement(LocatorStrategyFactory.buildBy(elementLocator))).perform();
    }

    /**
     * Drag and drop one element to the another
     *
     * @param elementLocator locator of the element
     * @param targetElementLocator locator of the target element
     */
    @When("I drag and drop {string} in {string}")
    public void dragAndDropToElement(final String elementLocator, final String targetElementLocator) {
        final WebDriver driver = DriverManager.getInstance()
                .getCurrentDriver()
                .getWebDriver();

        final org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(driver);
        final WebElement element = driver.findElement(LocatorStrategyFactory.buildBy(elementLocator));
        final WebElement targetElement = driver.findElement(LocatorStrategyFactory.buildBy(targetElementLocator));
        actions.dragAndDrop(element, targetElement).perform();
    }

    /**
     * Move mouse to coordinates
     *
     * @param coordinates value of coordinates (e.g. 150x200)
     */
    @When("I move mouse to {string}")
    public void moveMouseByCoordinates(final String coordinates) {
        if (!coordinates.matches(dimensionRegex)) {
            throw new AqaException(coordinatesExceptionMessage);
        }
        final int height = Integer.parseInt(RegexpUtils.getStringByRegexp(coordinates, dimensionRegex, 1));
        final int width = Integer.parseInt(RegexpUtils.getStringByRegexp(coordinates, dimensionRegex, 2));
        final WebDriver driver = DriverManager.getInstance()
                .getCurrentDriver()
                .getWebDriver();
        final org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(driver);
        actions.moveByOffset(width, height).perform();
    }

    /**
     * Click on element with offset
     *
     * @param coordinates coordinates value of offset (e.g. 150x200)
     * @param elementLocator locator of the element
     */
    @When("I click {string} coordinates in {string}")
    public void clickByCoordinatesInElement(final String coordinates, final String elementLocator) {
        if (!coordinates.matches(dimensionRegex)) {
            throw new AqaException(coordinatesExceptionMessage);
        }
        final int xOffset = Integer.parseInt(RegexpUtils.getStringByRegexp(coordinates, dimensionRegex, 1));
        final int yOffset = Integer.parseInt(RegexpUtils.getStringByRegexp(coordinates, dimensionRegex, 2));
        final WebDriver driver = DriverManager.getInstance()
                .getCurrentDriver()
                .getWebDriver();

        final org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(driver);
        actions.moveToElement(driver.findElement(LocatorStrategyFactory.buildBy(elementLocator)), xOffset, yOffset).perform();
    }

    /**
     * Hold down on key
     *
     * @param key value of the key
     */
    @When("I hold down {string} key")
    public void holdKey(final String key) {
        final WebDriver driver = DriverManager.getInstance()
                .getCurrentDriver()
                .getWebDriver();
        final org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(driver);
        actions.keyDown(Keys.valueOf(key)).perform();
    }

    /**
     * Release the key
     *
     * @param key value of the key
     */
    @When("I release {string} key")
    public void releaseKey(final String key) {
        final WebDriver driver = DriverManager.getInstance()
                .getCurrentDriver()
                .getWebDriver();
        final org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(driver);
        actions.keyUp(Keys.valueOf(key)).perform();
    }

    /**
     * Execute javascript function
     *
     * @param script js code for execution
     */
    @When("I execute {string} function")
    public void executeScript(final String script) {
        final WebDriver driver = DriverManager.getInstance()
                .getCurrentDriver()
                .getWebDriver();
        ((JavascriptExecutor) driver).executeScript(script);
    }

    /**
     * Set specific cookie
     *
     * @param cookieValue value of the cookie
     * @param cookieName name of the cookie
     */
    @When("I set {string} cookie as {string}")
    public void executeScript(final String cookieValue, final String cookieName) {
        final WebDriver driver = DriverManager.getInstance()
                .getCurrentDriver()
                .getWebDriver();
        driver.manage().addCookie(new Cookie(cookieName, cookieValue));
    }
}
