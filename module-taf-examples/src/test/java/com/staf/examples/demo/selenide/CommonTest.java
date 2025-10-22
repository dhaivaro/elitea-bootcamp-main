package com.staf.examples.demo.selenide;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.page;
import static com.staf.common.metadata.ModuleType.SELENIDE;

import com.staf.common.metadata.Toolset;
import com.staf.common.property.Configuration;
import com.staf.examples.domain.sauce.InventoryItem;
import com.staf.examples.ui.selenide.sauce.page.LoginDemoPage;
import com.staf.examples.ui.selenide.sauce.page.ProductsPage;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;

@Slf4j
@Toolset(SELENIDE)
public abstract class CommonTest {
    private final String DEMO_URL = Configuration.get().baseUrl();
    private final String USERNAME = "standard_user";
    private final String PASSWORD = "secret_sauce";

    protected void simpleLogTest() {
        log.info("Test log");
    }

    protected void loginTest() {
        open(DEMO_URL);
        LoginDemoPage loginDemoPage = page(LoginDemoPage.class);
        ProductsPage productsPage = loginDemoPage
                .getUsernameInput()
                .setValue(USERNAME)
                .getPasswordInput()
                .setValue(PASSWORD)
                .clickSubmitButton();
        Assertions.assertThat(productsPage.getProductItems().getElements())
                .as("There is no any grid item on the page")
                .isNotEmpty();
    }

    protected void loginFragmentTest() {
        open(DEMO_URL);
        LoginDemoPage loginDemoPage = page(LoginDemoPage.class);
        log.info(
                "Login form inputs: {}",
                loginDemoPage.getLoginFormFragment().waitForElement().getInputs());
        ProductsPage productsPage = loginDemoPage
                .getLoginFormFragment()
                .submitForm(Map.of("Username", USERNAME, "Password", PASSWORD), ProductsPage.class);
        Assertions.assertThat(productsPage.getProductItems().getElements())
                .as("There is no any grid item on the page")
                .isNotEmpty();
    }

    protected void failedTest() {
        open(DEMO_URL);
        Assertions.fail("Demo failure");
    }

    protected void loginStepTest() {
        open(DEMO_URL);
        ProductsPage productsPage = page(LoginDemoPage.class).login(USERNAME, PASSWORD);
        Assertions.assertThat(productsPage.getProductItems().getElements())
                .as("There is no any grid item on the page")
                .isNotEmpty();
    }

    protected void gridItemsTest() {
        open(DEMO_URL);
        LoginDemoPage loginDemoPage = page(LoginDemoPage.class);
        ProductsPage productsPage = loginDemoPage
                .getLoginFormFragment()
                .submitForm(Map.of("Username", USERNAME, "Password", PASSWORD), ProductsPage.class);
        Assertions.assertThat(productsPage.getProductItems().getElements(3))
                .as("There is no any grid item on the page")
                .isNotEmpty();
        // interaction with custom elements' list
        productsPage
                .getProductItems()
                .getElements()
                .forEach(productItem -> log.info("Extracted item: {}", productItem.getItem()));
        // example of elements filter by predicate (items with a price $15.99)
        Assertions.assertThat(productsPage
                        .getProductItems()
                        .getElements(productItem -> {
                            final InventoryItem item = productItem.getItem();
                            return item.getPrice() == 15.99 && item.getCcy().equalsIgnoreCase("$");
                        })
                        .size())
                .as("Incorrect number of product items with price $15.99")
                .isEqualTo(2);
    }
}
