package com.go_to.homework.cardgame.domain.models;

import java.util.Objects;
import java.util.UUID;

public class Card implements Comparable<Card> {
    private UUID uuid;
    private CardFace face;
    private CardSuit suit;
    private UUID deckUuid;

    public static Card createCard(CardFace face, CardSuit suit, Deck deck) {
        return new Card(UUID.randomUUID(), face, suit, deck);
    }

    private Card(UUID uuid, CardFace face, CardSuit suit, Deck deck) {
        this.uuid = uuid;
        this.face = face;
        this.suit = suit;
        this.deckUuid = deck.getUuid();
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

    public UUID getDeckUuid() {
        return deckUuid;
    }

    public void setDeckUuid(UUID deckUuid) {
        this.deckUuid = deckUuid;
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
        return Objects.hash(deckUuid, face, suit);
    }

    @Override
    public int compareTo(Card o) {
        return this.face.getFaceValue() - o.face.getFaceValue();
    }
}