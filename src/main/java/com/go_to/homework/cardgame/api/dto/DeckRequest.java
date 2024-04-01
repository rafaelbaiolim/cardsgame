package com.go_to.homework.cardgame.api.dto;

import java.util.UUID;

public class DeckRequest {
    private String name;

    private UUID deckUuid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getDeckUuid() {
        return deckUuid;
    }

    public void setDeckUuid(UUID deckUuid) {
        this.deckUuid = deckUuid;
    }
}
