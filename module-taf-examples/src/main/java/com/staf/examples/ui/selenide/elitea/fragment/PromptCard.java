package com.staf.examples.ui.selenide.elitea.fragment;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.staf.common.metadata.Toolset;
import com.staf.examples.ui.selenide.elitea.page.EliteaEditPromptPage;
import com.staf.ui.core.AbstractElement;
import com.staf.ui.core.AbstractPage;

import static com.staf.common.metadata.ModuleType.SELENIDE;

@Toolset(SELENIDE)
public class PromptCard<P extends AbstractPage> extends AbstractElement<P, PromptCard<P>> {

    private SelenideElement title = element.$(".MuiTypography-headingSmall");
    private SelenideElement tag = element.$("div[color=\"text.secondary\"]");
    private SelenideElement ownerName = element.$x(".//div[contains(@class, 'MuiAvatar-root')]/following-sibling::div");
    /**
     * Constructor for initialization of the element that introduces page instance and context element
     *
     * @param element context element
     * @param page    page instance
     */
    public PromptCard(SelenideElement element, P page) {
        super(element, page);
    }

    public String getName() {
        return title.getText();
    }

    public EliteaEditPromptPage selectCard() {
        title.click();
        return Selenide.page(EliteaEditPromptPage.class);
    }

    public String getTagText() {
        return tag.innerText();
    }

    public String getOwnName() {
        return ownerName.innerText();
    }

    public void clickOwner() {
        ownerName.click();
    }
}
