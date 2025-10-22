package com.staf.examples.api.model.cards;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
public class Deck {

    private boolean success;

    @JsonProperty("deck_id")
    private String deckId;

    @JsonInclude(NON_NULL)
    private boolean shuffled;

    private int remaining;

    @JsonInclude(NON_NULL)
    private List<Card> cards;
}
