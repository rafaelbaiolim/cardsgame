package com.go_to.homework.cardgame.domain.models;

public enum CardFace {
    ACE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(11),
    QUEEN(12),
    KING(13);

    private final Integer faceValue;

    private CardFace(Integer faceValue) {
        this.faceValue = faceValue;
    }

    public Integer getFaceValue() {
        return this.faceValue;
    }
}
