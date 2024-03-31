package com.go_to.homework.cardgame.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.go_to.homework.cardgame.domain.exceptions.DeckOperationException;

import java.util.*;

public class Deck {
    private UUID uuid;
    @JsonManagedReference
    private Set<Card> cards = new HashSet<>();

    @JsonIgnore
    private UUID gameUuid;

    public Deck() {
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public List<Card> getCards() {
        return new ArrayList<>(this.cards);
    }

    public void setCards(Set<Card> cards) throws DeckOperationException {
        if (this.cards != null && !this.cards.isEmpty())
            throw new DeckOperationException("Deck modification error: The deck has already been initialized and cannot be altered");
        this.cards = cards;
    }

    public UUID getGameUuid() {
        return this.gameUuid;
    }

    public void setGameUuid(UUID gameUuid) {
        this.gameUuid = gameUuid;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Deck o)) return false;
        return Objects.equals(uuid, o.uuid);
    }
}