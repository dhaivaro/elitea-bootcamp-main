package com.staf.examples.ui.selenide.elitea.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.staf.common.metadata.Toolset;
import com.staf.ui.core.AbstractPage;

import static com.codeborne.selenide.Selenide.$;
import static com.staf.common.metadata.ModuleType.SELENIDE;

@Toolset(SELENIDE)
public class EliteaLoginPage extends AbstractPage {

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement signInBtn = $("#kc-login");

    public EliteaMainPage clickSubmitButton() {
        signInBtn.click();
        waitForPageToLoad();
        return Selenide.page(EliteaMainPage.class);
    }

    public EliteaLoginPage inputUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public EliteaLoginPage inputPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }
}
