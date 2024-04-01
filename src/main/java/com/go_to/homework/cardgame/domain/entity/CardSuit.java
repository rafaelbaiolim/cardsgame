package com.go_to.homework.cardgame.domain.entity;

public enum CardSuit {
    HEARTS(4), SPADES(3), CLUBS(2), DIAMONDS(1);

    private final Integer weight;

    private CardSuit(Integer weight) {
        this.weight = weight;
    }

    public Integer getWeight() {
        return this.weight;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}