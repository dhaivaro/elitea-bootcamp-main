package com.staf.examples.api.service;

import com.staf.examples.api.model.cards.Deck;
import com.staf.examples.api.service.impl.Response;
import lombok.NonNull;

import javax.annotation.Nullable;

public interface CardsService {

    /**
     * Default deck size without jokers.
     */
    int DEFAULT_DECK_SIZE = 52;

    /**
     * Default deck size with jokers.
     */
    int DEFAULT_DECK_SIZE_WITH_JOKERS = 54;

    /**
     * Creates a new deck of cards.
     *
     * @param jokersEnabled should a new deck contain jokers
     * @return a new deck of cards
     */
    Response<Deck> brandNewDeck(boolean jokersEnabled);

    /**
     * Draws a specific amount the cards from a specific deck.
     *
     * @param deckId deck ID from which cards should be drawn
     * @param number  number of cards
     * @return deck with cards
     * @see Deck#getCards()
     */
    Response<Deck> drawCards(String deckId, int number);

    /**
     * Draws a specific amount the cards from a specific deck.
     *
     * @param deck  the deck from which cards should be drawn
     * @param count number of cards
     * @return deck with cards
     * @see Deck#getCards()
     */
    default Response<Deck> drawCards(@NonNull Deck deck, int count) {
        return drawCards(deck.getDeckId(), count);
    }

    /**
     * Shuffles cards in a specific deck.
     *
     * @param deckId    ID of the deck to shuffle
     * @param remaining should shuffle only remaining cards in the deck
     * @return shuffled deck
     */
    Response<Deck> shuffle(String deckId, boolean remaining);

    /**
     * Shuffles cards in a specific deck.
     *
     * @param deck      deck to shuffle its cards
     * @param remaining should shuffle only remaining cards in the deck
     * @return shuffled deck
     */
    default Response<Deck> shuffle(@NonNull Deck deck, boolean remaining) {
        return shuffle(deck.getDeckId(), remaining);
    }

    /**
     * Shuffles only specific cards in the given deck. If the {@code cards} parameter is null or empty - this method
     * will return {@code null}.
     *
     * @param deckId ID of the deck to shuffle
     * @param cards  cards which would be shuffled
     * @return shuffled deck
     */
    @Nullable
    Response<Deck> shuffle(String deckId, String... cards);

    /**
     * Shuffles only specific cards in the given deck. If the {@code cards} parameter is null or empty - this method
     * will return {@code null}.
     *
     * @param deck  deck to shuffle
     * @param cards cards which would be shuffled
     * @return shuffled deck
     */
    @Nullable
    default Response<Deck> shuffle(@NonNull Deck deck, String... cards) {
        return shuffle(deck.getDeckId(), cards);
    }
}
