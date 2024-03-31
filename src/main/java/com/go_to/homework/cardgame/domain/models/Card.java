package com.go_to.homework.cardgame.domain.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.Objects;
import java.util.UUID;

public class Card {
    private UUID uuid;
    private CardFace face;
    private CardSuit suit;
    @JsonBackReference
    private Deck deck;

    public static Card createCard(CardFace face, CardSuit suit, Deck deck) {
        return new Card(UUID.randomUUID(), face, suit, deck);
    }

    private Card(UUID uuid, CardFace face, CardSuit suit, Deck deck) {
        this.uuid = uuid;
        this.face = face;
        this.suit = suit;
        this.deck = deck;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public CardFace getFace() {
        return face;
    }

    public void setFace(CardFace face) {
        this.face = face;
    }

    public CardSuit getSuit() {
        return suit;
    }

    public void setSuit(CardSuit suit) {
        this.suit = suit;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return face == card.face &&
                suit == card.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deck, face, suit);
    }
}