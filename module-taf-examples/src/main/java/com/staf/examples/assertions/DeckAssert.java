package com.staf.examples.assertions;

import com.staf.examples.api.model.cards.Deck;
import com.staf.examples.api.service.impl.Response;
import org.assertj.core.internal.Integers;

public class DeckAssert extends AbstractResponseAssert<DeckAssert, Deck> {

    private final Integers integers = Integers.instance();

    protected DeckAssert(final Response<Deck> actual) {
        super(actual, DeckAssert.class);
    }

    public DeckAssert hasRemaining(final int expected) {
        isNotNull();
        hasBody();

        integers.assertEqual(info, getActualBody().getRemaining(), expected);
        return myself;
    }

    public DeckAssert hasCards(final int expected) {
        isNotNull();
        hasBody();

        integers.assertEqual(info, getActualBody().getCards().size(), expected);
        return myself;
    }
}
