package com.staf.examples.ui.selenide.elitea.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.staf.common.metadata.Toolset;
import com.staf.ui.core.AbstractPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static com.staf.common.metadata.ModuleType.SELENIDE;

@Toolset(SELENIDE)
public class EliteaEditPromptPage extends AbstractPage {
    // Run
    private final SelenideElement title = $x("//div[contains(@class, 'MuiBox-root')]/span[contains(@class, 'MuiTypography-headingSmall')]");
    private final SelenideElement description = $x("//div[contains(@class, 'MuiBox-root')]/span[contains(@class, 'MuiTypography-bodyMedium')]");
    // Configuration
    private final SelenideElement configurationTab = $x("//button[contains(@class, 'MuiTab-root') and contains(., 'Configuration')]");
    private final SelenideElement promptName = $("input#prompt-name");
    private final SelenideElement promptDescription = $("#prompt-desc");
    private final SelenideElement saveAsVersionButton = $x("//button[contains(@class, 'MuiButton-root') and contains(text(), 'Save As Version')]");
    private final SelenideElement saveButton = $x("//button[contains(@class, 'MuiButton-root') and text()='Save']");
    private final SelenideElement deleteVersionButton = $x("//button[contains(text(), 'Delete Version')]");
    private final SelenideElement deletePromptButton = $x("//button[@aria-label='delete entity']");
    private final SelenideElement contextTextarea = $x("//textarea[@id='prompt-context']");

    private final SelenideElement runTab = $x("//button[contains(@class, 'MuiTab-root') and contains(., 'Run')]");
    private final SelenideElement errorMessage = $("p.MuiFormHelperText-root.Mui-error");
    
    private final SelenideElement dialogInputField = $x("//div[contains(@class, 'MuiDialogContent-root')]//input[@type='text']");
    private final SelenideElement dialogSaveButton = $x("//div[contains(@class, 'MuiDialogActions-root')]//button[contains(text(), 'Save')]");

    private final SelenideElement alertMessage = $x("//div[contains(@class, 'MuiAlert-message')]");

    private final SelenideElement confirmButton = $x("//button[contains(text(), 'Confirm')]");
    private final SelenideElement nameConfirmationInput = $x("//input[@id='name']");
    private final SelenideElement deleteButton = $x("//button[text()='Delete']");
    
    private final SelenideElement versionDropdown = $("#simple-select-undefined");
    private final SelenideElement firstListItem = $x("//ul[contains(@class, 'MuiList-root')]//li[1]");
    
    public EliteaEditPromptPage selectConfiguration() {
        configurationTab.click();
        return this;
    }

    public EliteaEditPromptPage setName(String name) {
        Selenide.Wait()
                .withTimeout(Duration.ofSeconds(15))
                .pollingEvery(Duration.ofSeconds(1))
                .until(webDriver -> promptName.is(Condition.editable));
        promptName.setValue(name);
        return this;
    }

    public EliteaEditPromptPage setDescription(String description) {
        promptDescription.setValue(description);
        return this;
    }

    public boolean isNameInputDisabled() {
        Selenide.Wait()
                .withTimeout(Duration.ofSeconds(7))
                .pollingEvery(Duration.ofMillis(500))
                .until(webDriver -> promptName.is(Condition.visible));
        return promptName.is(Condition.disabled);
    }

    public boolean isDescriptionInputDisabled() {
        return promptDescription.is(Condition.disabled);
    }
    
    public EliteaEditPromptPage clickSaveAsVersionButton() {
        saveAsVersionButton.click();
        return this;
    }

    public EliteaEditPromptPage clickSaveButton() {
        saveButton.click();
        waitForPageToLoad();
        return this;
    }

    public boolean isSaveButtonDisabled() {
        Selenide.Wait()
                .withTimeout(Duration.ofSeconds(15))
                .pollingEvery(Duration.ofSeconds(1))
                .until(webDriver -> saveButton.is(Condition.disabled));
        return saveButton.is(Condition.disabled);
    }

    public EliteaEditPromptPage setContext(String context) {
        contextTextarea.setValue(context);
        return this;
    }

    public String getContext() {
        return contextTextarea.getValue();
    }

    public EliteaEditPromptPage setDialogInput(String value) {
        dialogInputField.setValue(value);
        return this;
    }

    public EliteaEditPromptPage clickDialogSaveButton() {
        dialogSaveButton.click();
        return this;
    }

    public boolean isAlertDisplayed() {
        Selenide.Wait()
                .withTimeout(Duration.ofSeconds(15))
                .pollingEvery(Duration.ofSeconds(1))
                .until(webDriver -> alertMessage.isDisplayed());
        return alertMessage.isDisplayed();
    }

    public boolean isRunTabDisplayed() {
        return runTab.isDisplayed();
    }

    public void checkErrorMessageDisplayed() {
        errorMessage.shouldBe(visible);
    }

    public void checkMessageText(String expectedText) {
        errorMessage.shouldHave(text(expectedText));
    }
    
    public String getAlertMessageText() {
        alertMessage.shouldBe(visible);
        return alertMessage.getText();
    }

    public EliteaEditPromptPage clickDeleteVersionButton() {
        deleteVersionButton.click();
        return this;
    }
    
    public EliteaEditPromptPage clickDeletePromptButton() {
        deletePromptButton.click();
        return this;
    }
    
    public EliteaEditPromptPage clickConfirmButton() {
        confirmButton.click();
        return this;
    }
    
    public EliteaEditPromptPage setConfirmationName(String name) {
        nameConfirmationInput.setValue(name);
        return this;
    }

    public EliteaEditPromptPage clickDeleteButton() {
        Selenide.Wait()
                .withTimeout(Duration.ofSeconds(5))
                .pollingEvery(Duration.ofMillis(300))
                .until(webDriver -> deleteButton.is(Condition.interactable));
        deleteButton.click();
        return this;
    }
    
    public EliteaEditPromptPage clickVersionDropdown() {
        versionDropdown.click();
        return this;
    }

    public EliteaEditPromptPage clickFirstListItem() {
        firstListItem.click();
        return this;
    }

    public boolean isVersionDisplayed() {
        Selenide.Wait().until(webDriver -> versionDropdown.isDisplayed());
        return versionDropdown.isDisplayed();
    }
}
