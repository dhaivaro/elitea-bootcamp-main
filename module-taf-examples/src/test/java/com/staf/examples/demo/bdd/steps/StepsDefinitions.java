package com.staf.examples.demo.bdd.steps;

import com.staf.examples.domain.sauce.InventoryItem;
import com.staf.examples.ui.selenide.sauce.page.LoginDemoPage;
import com.staf.examples.ui.selenide.sauce.page.ProductsPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
//import org.openqa.selenium.WebDriver;

import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.page;

@Slf4j
public class StepsDefinitions {
    private Scenario scenario;
//    private WebDriver driver;
    private LoginDemoPage loginDemoPage;
    private ProductsPage productsPage;

    @Before
    public void setUp(Scenario scenario) {
//        driver = WebDriverManager.getDriver();
        this.scenario = scenario;
        loginDemoPage = page(LoginDemoPage.class);
    }

    @After
    public void quit() {
//        WebDriverManager.quitDriver(scenario);
    }

    // TODO: resolve Exception in thread "main" io.cucumber.core.runner.DuplicateStepDefinitionException: Duplicate step definitions in com.staf.examples.demo.bdd.steps.StepsDefinitions.openUrl(java.lang.String) and com.staf.kwd.library.steps.Actions.openUrl(java.lang.String)
    @Given("I open bdd {string} url")
    public void openBddUrl(String url) {
        open(url);
    }
    
    @And("I login as {string} username {string} password")
    public void loginAs(String username, String password) {
        productsPage = loginDemoPage
                .getUsernameInput()
                .setValue(username)
                .getPasswordInput()
                .setValue(password)
                .clickSubmitButton();
    }

    
    @And("I verify page contains grid")
    public void verifyGrid() {
        Assertions.assertThat(productsPage.getProductItems().getElements())
                .as("There is no any grid item on the page")
                .isNotEmpty();
    }

    @And("I verify grid is not empty")
    public void verifyGridNotEmpty() {
        Assertions.assertThat(productsPage.getProductItems().getElements(3))
                .as("There is no any grid item on the page")
                .isNotEmpty();
    }

    @And("I list all grid items")
    public void listGridItems() {
        // interaction with custom elements' list
        productsPage
                .getProductItems()
                .getElements()
                .forEach(productItem -> log.info("Extracted item: {}", productItem.getItem()));
    }

    @And("I expect number of items with price '([^']*)' to be equal '{int}'")
    public void verifyItemsWithPriceIsEqualTo(double price, int number) {
        // example of elements filter by predicate (items with a price $15.99)
        Assertions.assertThat(productsPage
                        .getProductItems()
                        .getElements(productItem -> {
                            final InventoryItem item = productItem.getItem();
                            return item.getPrice() == price && item.getCcy().equalsIgnoreCase("$");
                        })
                        .size())
                .as("Incorrect number of product items with price $15.99")
                .isEqualTo(number);
    }
}
