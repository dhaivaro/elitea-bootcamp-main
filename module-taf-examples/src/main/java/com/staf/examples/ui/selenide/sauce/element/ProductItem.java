package com.staf.examples.ui.selenide.sauce.element;

import static com.staf.common.metadata.ModuleType.SELENIDE;

import com.codeborne.selenide.SelenideElement;
import com.staf.common.metadata.Toolset;
import com.staf.examples.domain.sauce.InventoryItem;
import com.staf.ui.core.AbstractElement;
import com.staf.ui.core.AbstractPage;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom element for a product item (grid) item
 *
 * @param <P> page context
 */
@Slf4j
@Toolset(SELENIDE)
public final class ProductItem<P extends AbstractPage> extends AbstractElement<P, ProductItem<P>> {
    private final SelenideElement name = element.$(".inventory_item_name");
    private final SelenideElement description = element.$(".inventory_item_desc");
    private final SelenideElement price = element.$(".inventory_item_price");
    private final SelenideElement addToCartBtn = element.$x(".//button[contains(@name, 'add-to-cart')]");
    private final SelenideElement removeBtn = element.$x(".//button[contains(@name, 'remove')]");

    public ProductItem(final SelenideElement elementBlock, final P pageContext) {
        super(elementBlock, pageContext);
    }

    public InventoryItem getItem() {
        final String itemPrice = getPrice();
        return InventoryItem.builder()
                .name(getName())
                .description(getDescription())
                .price(Double.parseDouble(itemPrice.substring(1)))
                .ccy(itemPrice.substring(0, 1))
                .build();
    }

    public String getName() {
        return name.getText();
    }

    public String getDescription() {
        return description.getText();
    }

    public String getPrice() {
        return price.getText();
    }

    public P clickAddToCart() {
        addToCartBtn.click();
        return pageContext;
    }

    public P clickRemove() {
        removeBtn.click();
        return pageContext;
    }

    public boolean isRemoveBtnPresent() {
        return removeBtn.isDisplayed();
    }
}
