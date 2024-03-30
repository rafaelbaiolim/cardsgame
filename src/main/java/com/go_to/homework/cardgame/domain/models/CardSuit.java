package com.go_to.homework.cardgame.domain.models;

public enum CardSuit {
    HEARTS(4), SPADES(3), CLUBS(2), DIAMONDS(1);

    private final Integer weight;

    private CardSuit(Integer weight) {
        this.weight = weight;
    }

    public Integer getWeight() {
        return this.weight;
    }
}