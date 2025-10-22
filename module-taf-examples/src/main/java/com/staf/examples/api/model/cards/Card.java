package com.staf.examples.api.model.cards;

import lombok.Data;

@Data
public class Card {

    private String code;
    private String image;
    private Images images;
    private String value;
    private Suit suit;
}
