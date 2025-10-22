package com.staf.examples.ui.selenide.elitea.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.staf.common.metadata.Toolset;
import com.staf.common.util.WaitUtils;
import com.staf.examples.ui.selenide.elitea.fragment.PromptCard;
import com.staf.ui.core.AbstractPage;
import com.staf.ui.core.ElementList;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.*;
import static com.staf.common.metadata.ModuleType.SELENIDE;

@Toolset(SELENIDE)
public class EliteaPromptsPage extends AbstractPage {
    
    private final SelenideElement title = $x("//nav//p[contains(text(), 'Prompts')]");
    private final SelenideElement addPromptButton = $x("//button[@aria-label='Create Prompt']");
    private final ElementsCollection tags = $$(".MuiChip-label");
    @Getter
    private final ElementList<PromptCard, EliteaPromptsPage> promptCards =
            new ElementList<>($$(".MuiCardContent-root"), this, PromptCard.class);
    //TODO: remove when extend PageObjectStrategy with Fragments
    private ElementsCollection ownerName = $$x(".//div[contains(@class, 'MuiAvatar-root')]/following-sibling::div");

    public boolean isTitleDisplayed() {
        return title.isDisplayed();
    }

    public EliteaEditPromptPage selectPrompt(final String title) {
        WaitUtils.waitFor(() -> promptCards.getElements().size() > 0);
        return promptCards.getElements(e -> e.getName().equals(title)).get(0).selectCard();
    }
    
    public EliteaEditPromptPage clickAddPromptButton() {
        addPromptButton.click();
        return Selenide.page(EliteaEditPromptPage.class);
    }

    public EliteaPromptsPage clickTagFilter(String filterName) {
        tags.find(Condition.text(filterName)).click();
        return this;
    }

    public String clickOwnerNameOfFirstPromptCard() {
        WaitUtils.waitFor(() -> promptCards.getElements().size() > 0);
        String ownerName = promptCards.getElements().get(0).getOwnName();
        promptCards.getElements().get(0).clickOwner();
        return ownerName;
    }
}
