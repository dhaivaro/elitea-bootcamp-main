package com.staf.examples.ui.selenide.sauce.fragment;

import static com.staf.common.metadata.ModuleType.SELENIDE;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.staf.common.metadata.Toolset;
import com.staf.examples.ui.selenide.sauce.element.NamedInput;
import com.staf.ui.core.AbstractElement;
import com.staf.ui.core.AbstractPage;
import com.staf.ui.core.ElementList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Form fragment that encapsulates custom elements
 *
 * @param <P> page context
 */
@Toolset(SELENIDE)
public final class Form<P extends AbstractPage> extends AbstractElement<P, Form<P>> {
    private final ElementList<NamedInput<P>, P> formInputs =
            new ElementList(element.$$(".form_group input"), pageContext, NamedInput.class);

    private final SelenideElement submitButton = element.$("[type='submit']");

    public Form(final SelenideElement elementBlock, final P page) {
        super(elementBlock, page);
    }

    public P submitForm(final Map<String, String> inputsData) {
        return (P) submitForm(inputsData, pageContext.getClass());
    }

    public <N extends AbstractPage> N submitForm(final Map<String, String> inputsData, final Class<N> newPage) {
        formInputs.getElements().forEach(input -> {
            final String inputName = input.getName();
            final String inputData = inputsData.getOrDefault(
                    input.getName(), String.format("input data is undefined for field '%s'", inputName));
            // add logging in case inputData is incorrect, empty, etc.
            input.setValue(inputData);
        });
        submitButton.click();
        return Selenide.page(newPage);
    }

    public List<String> getInputs() {
        return formInputs.getElements().stream().map(NamedInput::getName).collect(Collectors.toList());
    }
}
