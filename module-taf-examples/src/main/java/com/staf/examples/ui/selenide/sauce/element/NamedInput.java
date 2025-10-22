package com.staf.examples.ui.selenide.sauce.element;

import static com.staf.common.metadata.ModuleType.SELENIDE;

import com.codeborne.selenide.SelenideElement;
import com.staf.common.metadata.Toolset;
import com.staf.ui.core.AbstractElement;
import com.staf.ui.core.AbstractPage;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom element
 *
 * @param <P> page context
 */
@Slf4j
@Toolset(SELENIDE)
public final class NamedInput<P extends AbstractPage> extends AbstractElement<P, NamedInput<P>> {
    public NamedInput(final SelenideElement elementBlock, final String name, final P pageContext) {
        super(elementBlock, pageContext);
        this.element = elementBlock.$(String.format("[placeholder='%s']", name));
    }

    public NamedInput(final SelenideElement elementBlock, final P pageContext) {
        super(elementBlock, pageContext);
    }

    public String getName() {
        return element.getAttribute("Placeholder");
    }

    public String getValue() {
        return element.getText();
    }

    public P setValue(final String value) {
        log.info("Attempt to type '{}' into the input '{}'", value, getName());
        element.setValue(value);
        return pageContext;
    }
}
