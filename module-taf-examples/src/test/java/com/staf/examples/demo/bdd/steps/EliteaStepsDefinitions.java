package com.staf.examples.demo.bdd.steps;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.staf.common.property.EliteaConfiguration;
import com.staf.examples.ui.selenide.elitea.page.EliteaEditPromptPage;
import com.staf.examples.ui.selenide.elitea.page.EliteaLoginPage;
import com.staf.examples.ui.selenide.elitea.page.EliteaMainPage;
import com.staf.examples.ui.selenide.elitea.page.EliteaPromptsPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;

import static com.codeborne.selenide.Selenide.page;

public class EliteaStepsDefinitions {
    private final String endpoint = EliteaConfiguration.get().baseUrl();
    private EliteaLoginPage loginPage;
    private EliteaMainPage mainPage;
    private EliteaPromptsPage eliteaPromptsPage;
    private EliteaEditPromptPage eliteaEditPromptPage;
    private String ownerName;

    @Before(order = 1)
    public void setUp() {
        loginPage = page(EliteaLoginPage.class);
    }

    @After(order = 1)
    public void tearDown() {
        Selenide.closeWebDriver();
    }

    @Given("I open Elitea application")
    public void openEliteaApplication() {
        Selenide.open(endpoint);
        Configuration.pollingInterval = 500;
        Configuration.timeout = 10000;
    }

    @When("I log in with username {string} and password {string} to Elitea app")
    public void loginAsUserUsernamePassPasswordToEliteaApp(String username, String password) {
        mainPage = loginPage.inputUsername(username).inputPassword(password).clickSubmitButton();
    }

    @Then("I switch to the Private project")
    public void switchToPrivateProject() {
        mainPage.openProjects().selectPrivate();
    }

    @When("I navigate to the Prompts menu")
    public void navigateToPromptsMenu() {
        eliteaPromptsPage = mainPage.openMenu().selectPrompts();
    }

    @When("I click the Add Prompt button")
    public void clickAddPromptButton() {
        eliteaEditPromptPage = eliteaPromptsPage.clickAddPromptButton();
    }

    @When("I select prompt {string}")
    public void selectAPrompt(String name) {
        eliteaEditPromptPage = eliteaPromptsPage.selectPrompt(name);
    }
    
    @When("I select the Configuration tab")
    public void selectConfigurationTab() {
        eliteaEditPromptPage.selectConfiguration();
    }
    
    @When("I click the Table View button")
    public void clickTableViewButton() {
        mainPage.clickTableViewButton();
    }

    @When("I put Name {string} and Description {string}")
    public void tryToEditNameOrDescriptionFields(String name, String description) {
        eliteaEditPromptPage.setName(name).setDescription(description);
    }

    @When("I click the Save New Version button")
    public void clickSaveNewVersionButton() {
        eliteaEditPromptPage.clickSaveAsVersionButton();
    }

    @When("I set the dialog input to {string}")
    public void setDialogInput(String value) {
        eliteaEditPromptPage.setDialogInput(value);
    }

    @When("I click the dialog Save button")
    public void clickDialogSaveButton() {
        eliteaEditPromptPage.clickDialogSaveButton();
    }

    @When("I click the Save button")
    public void clickSaveButton() {
        eliteaEditPromptPage.clickSaveButton();
    }

    @When("I click Delete Prompt button")
    public void clickDeletePromptButton() {
        eliteaEditPromptPage.clickDeletePromptButton();
    }
    
    @When("I click the Delete Version button")
    public void clickDeleteVersionButton() {
        eliteaEditPromptPage.clickDeleteVersionButton();
    }

    @When("I confirm the version deletion")
    public void confirmDeletion() {
        eliteaEditPromptPage.clickConfirmButton();
    }
    
    @When("I confirm prompt {string} deletion")
    public void confirmDeletion(String name) {
        eliteaEditPromptPage.setConfirmationName(name);
        eliteaEditPromptPage.clickDeleteButton();
    }
    
    @When("Switch view to list")
    public void switchViewToList() {
        mainPage.clickTableViewButton();
    }

    @When("I change context to {string}")
    public void changeContextTo(String context) {
        eliteaEditPromptPage.setContext(context);
    }

    @Then("I should see the user avatar")
    public void verifyUserIsLoggedInToEliteaApp() {
        Assertions.assertThat(mainPage.isAvatarDisplayed()).isTrue();
    }

    @Then("Run tab is displayed")
    public void isRunTabDisplayed() {
        Assertions.assertThat(eliteaEditPromptPage.isRunTabDisplayed()).isTrue();
    }
    
    @Then("Prompt is updated")
    public void verifyPromptIsSaved() {
        Assertions.assertThat(eliteaEditPromptPage.isAlertDisplayed()).isTrue();
        Assertions.assertThat(eliteaEditPromptPage.getAlertMessageText()).contains("The prompt has been updated");
    }

    @Then("Check Prompt deleted with message {string}")
    public void verifyPromptIsDeleted(String message) {
        Assertions.assertThat(eliteaEditPromptPage.isAlertDisplayed()).isTrue();
        Assertions.assertThat(eliteaEditPromptPage.getAlertMessageText()).contains(message);
    }

    @Then("Notification appeared")
    public void notificationAppeared() {
        Assertions.assertThat(eliteaEditPromptPage.isAlertDisplayed()).isTrue();
    }

    @Then("Table grid is displayed")
    public void verifyTableViewDisplayed() {
        Assertions.assertThat(mainPage.isTableDisplayed()).isTrue();
    }

    @Then("Verify List Grid displayed")
    public void verifyListGridDisplayed() {
        Assertions.assertThat(mainPage.isGridContainerDisplayed()).isTrue();
    }

    // TODO: unify is|not
    @Then("Prompt version is displayed")
    public void promptVersionIsDisplayed() {
        Assertions.assertThat(eliteaEditPromptPage.isVersionDisplayed()).isTrue();
    }

    @Then("Prompt version is not displayed")
    public void promptVersionIsNotDisplayed() {
        Assertions.assertThat(eliteaEditPromptPage.isVersionDisplayed()).isFalse();
    }

    @Then("The field error message is displayed")
    public void checkErrorMessageDisplayed() {
        eliteaEditPromptPage.checkErrorMessageDisplayed();
    }

    @Then("The message should have text {string}")
    public void checkMessageText(String expectedText) {
        eliteaEditPromptPage.checkMessageText(expectedText);
    }

    @Then("Name input is disabled")
    public void isNameInputDisabled() {
        Assertions.assertThat(eliteaEditPromptPage.isNameInputDisabled()).isTrue();
    }

    @Then("Description input is disabled")
    public void isDescriptionInputDisabled() {
        Assertions.assertThat(eliteaEditPromptPage.isDescriptionInputDisabled()).isTrue();
    }
    
    @Then("Save button is not clickable")
    public void saveButtonIsNotClickable() {
        Assertions.assertThat(eliteaEditPromptPage.isSaveButtonDisabled()).isTrue();
    }

    @And("I choose tag {string}")
    public void chooseTag(String filterName) {
        eliteaPromptsPage.clickTagFilter(filterName);
    }

    @Then("only prompts with tag {string} are visible")
    public void onlyPromptsWithTagAreVisible(String tag) {
        Assertions.assertThat(
                eliteaPromptsPage.getPromptCards().getElements().stream().allMatch(e -> e.getTagText().equals(tag)))
                .isTrue();
    }

    @And("I click on owner name of first card")
    public void iClickOnOwnerNameOfFirstCard() {
         ownerName = eliteaPromptsPage.clickOwnerNameOfFirstPromptCard();
    }

    @Then("only prompts with chosen owner are visible")
    public void onlyPromptsWithChosenOwnerAreVisible() {
        Assertions.assertThat(
                        eliteaPromptsPage.getPromptCards().getElements().stream().allMatch(e -> e.getOwnName().equals(ownerName)))
                .isTrue();

    }
}
