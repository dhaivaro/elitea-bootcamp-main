package com.staf.examples.ui.selenide.elitea.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.staf.common.metadata.Toolset;
import com.staf.ui.core.AbstractPage;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static com.staf.common.metadata.ModuleType.SELENIDE;

@Toolset(SELENIDE)
public class EliteaMainPage extends AbstractPage {
    private final SelenideElement avatar = $(".MuiAvatar-root");
    private final SelenideElement projectSelector = $(".MuiFormControl-root");
    private final SelenideElement privateProjectListItem = $("li[role=\"option\"][data-value=\"21\"]");

    private final SelenideElement menuButton = $(".MuiButtonBase-root[aria-label=\"open drawer\"]");
    private final SelenideElement promptMenuItem = $x("//li[contains(., 'Prompts')]");

    private final SelenideElement tableViewButton = $x("//button[@value='table']");
    private final SelenideElement cardViewButton = $x("//button[@value='cards']");
    private final SelenideElement gridContainer = $x("//div[@role='tabpanel']//div[contains(@class, 'MuiGrid-container') and not(contains(@style, 'display: none'))]");
    private final SelenideElement tableElement = $x("//div[@role='tabpanel']//table[contains(@class, 'MuiTable-root')]");

    public boolean isAvatarDisplayed() {
        return avatar.isDisplayed();
    }

    public EliteaMainPage openProjects() {
        Selenide.Wait().withTimeout(Duration.ofSeconds(15)).pollingEvery(Duration.ofSeconds(1)).until(webDriver -> projectSelector.isDisplayed());
        projectSelector.click();
        return this;
    }

    public EliteaMainPage selectPrivate() {
        Selenide.Wait().until(webDriver -> privateProjectListItem.isDisplayed());
        privateProjectListItem.click();
        return this;
    }
    
    public EliteaMainPage openMenu() {
        Selenide.Wait().until(webDriver -> menuButton.isDisplayed());
        menuButton.click();
        return this;
    }

    public EliteaPromptsPage selectPrompts() {
        Selenide.Wait().until(webDriver -> promptMenuItem.isDisplayed());
        promptMenuItem.click();
        return Selenide.page(EliteaPromptsPage.class);
    }

    public EliteaMainPage clickTableViewButton() {
        tableViewButton.click();
        return this;
    }

    public EliteaMainPage clickCardViewButton() {
        cardViewButton.click();
        return this;
    }

    public boolean isGridContainerDisplayed() {
        return gridContainer.isDisplayed();
    }

    public boolean isTableDisplayed() {
        return tableElement.isDisplayed();
    }
}
