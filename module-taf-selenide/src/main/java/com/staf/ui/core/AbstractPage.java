package com.staf.ui.core;

import com.codeborne.selenide.Selenide;

import static com.codeborne.selenide.Selenide.executeJavaScript;

public abstract class AbstractPage {

    protected void waitForPageToLoad() {
        Selenide.Wait().until(webDriver ->
                executeJavaScript("return document.readyState").equals("complete")
        );
    }
}
