package com.staf.examples.api.service.impl;

import com.staf.examples.api.model.cards.Deck;
import com.staf.examples.api.service.CardsService;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.http.NameValuePair;

import java.net.URI;
import java.util.List;
import javax.annotation.Nullable;

public class CardsServiceImpl extends AbstractCardsService implements CardsService {
    private static final String URI_SHUFFLE_PATTERN = "%s/shuffle";

    @Override
    @NonNull
    protected String baseUrl() {
        return "deckofcardsapi.com/api/deck";
    }

    @Override
    @SneakyThrows
    public Response<Deck> brandNewDeck(final boolean jokersEnabled) {
        final URI uri = toUri("new/");
        final List<NameValuePair> params = asParams("jokers_enabled", jokersEnabled);
        return execute(uri, params, Deck.class);
    }

    @Override
    public Response<Deck> drawCards(final String deckId, final int number) {
        final List<NameValuePair> params = asParams("count", number);
        final URI uri = toUri(String.format("%s/draw", deckId), params);
        return execute(uri, Deck.class);
    }

    @Override
    public Response<Deck> shuffle(final String deckId, final boolean remaining) {
        final List<NameValuePair> params = asParams("remaining", remaining);
        final URI uri = toUri(String.format(URI_SHUFFLE_PATTERN, deckId), params);
        return execute(uri, Deck.class);
    }

    @Nullable
    @Override
    public Response<Deck> shuffle(final String deckId, final String... cards) {
        if (cards == null || cards.length == 0) {
            return null;
        }

        final List<NameValuePair> params = asParams("cards", String.join(",", cards));
        final URI uri = toUri(URI_SHUFFLE_PATTERN, params);
        return execute(uri, Deck.class);
    }
}
