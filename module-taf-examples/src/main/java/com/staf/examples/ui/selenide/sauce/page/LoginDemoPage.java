package com.staf.examples.ui.selenide.sauce.page;

import static com.codeborne.selenide.Selenide.$;
import static com.staf.common.metadata.ModuleType.SELENIDE;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.staf.common.metadata.Toolset;
import com.staf.examples.ui.selenide.sauce.element.NamedInput;
import com.staf.examples.ui.selenide.sauce.fragment.Form;
import com.staf.ui.core.AbstractPage;
import lombok.Getter;

@Toolset(SELENIDE)
public final class LoginDemoPage extends AbstractPage {
    // option #1
    private final SelenideElement loginForm = $("form");
    private final SelenideElement submitBtn = $("#login-button");

    // Option #1.1
    @Getter
    private final NamedInput<LoginDemoPage> usernameInput =
            NamedInput.init(loginForm.$("[placeholder='Username']"), this, NamedInput.class);

    // Option #1.2
    @Getter
    private final NamedInput<LoginDemoPage> passwordInput = new NamedInput<>(loginForm, "Password", this);

    // Option #2
    @Getter
    private final Form<LoginDemoPage> loginFormFragment = new Form<>(loginForm, this);

    public ProductsPage clickSubmitButton() {
        submitBtn.click();
        return Selenide.page(ProductsPage.class);
    }

    public ProductsPage login(final String username, final String password) {
        return usernameInput.setValue(username).passwordInput.setValue(password).clickSubmitButton();
    }
}
