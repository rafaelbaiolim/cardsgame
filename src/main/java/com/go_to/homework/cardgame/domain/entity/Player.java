package com.go_to.homework.cardgame.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Player {
    private UUID uuid;
    private String name;
    @JsonIgnore
    private UUID gameUuid;

    private List<Card> playerCards;

    int handValue = 0;

    public Player(String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.playerCards = new ArrayList<>();
    }

    public Player(String name, UUID uuid, UUID gameUuid, List<Card> playerCards) {
        this.name = name;
        this.uuid = uuid;
        this.gameUuid = gameUuid;
        this.setPlayerCards(playerCards);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getGameUuid() {
        return gameUuid;
    }

    public void setGameUuid(UUID gameUuid) {
        this.gameUuid = gameUuid;
    }

    public List<Card> getPlayerCards() {
        if (!playerCards.isEmpty()) {
            return playerCards.stream()
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());
        }
        return playerCards;
    }

    public int getHandValue() {
        return handValue;
    }

    public void setHandValue(int handValue) {
        this.handValue = handValue;
    }

    private void calculateHandValue() {
        this.handValue = playerCards.stream().mapToInt(card -> card.getFace().getFaceValue()).sum();
    }

    public void setPlayerCards(List<Card> playerCards) {
        this.playerCards = playerCards;
        calculateHandValue();
    }

    @Override
    public String toString() {
        return "[%s] %s".formatted(uuid, name);
    }

}
