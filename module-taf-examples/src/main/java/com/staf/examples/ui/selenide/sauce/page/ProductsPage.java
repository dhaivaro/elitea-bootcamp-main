package com.staf.examples.ui.selenide.sauce.page;

import static com.codeborne.selenide.Selenide.$;
import static com.staf.common.metadata.ModuleType.SELENIDE;

import com.codeborne.selenide.SelenideElement;
import com.staf.common.metadata.Toolset;
import com.staf.examples.ui.selenide.sauce.element.ProductItem;
import com.staf.ui.core.AbstractPage;
import com.staf.ui.core.ElementList;
import lombok.Getter;

@Toolset(SELENIDE)
public final class ProductsPage extends AbstractPage {
    private final SelenideElement pageContext = $("#inventory_container");

    @Getter
    private final ElementList<ProductItem, ProductsPage> productItems =
            new ElementList<>(pageContext.$$(".inventory_item"), this, ProductItem.class);
}
