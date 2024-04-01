package com.go_to.homework.cardgame.domain.entity;

import java.util.*;

public class GameEngine {
    private UUID gameUuid;
    private List<Card> shuffledDeck = new ArrayList<>();

    private Map<UUID, List<Card>> playerCards = new HashMap<>();

    private int currentPlayerIndex = 0;

    public UUID getGameUuid() {
        return gameUuid;
    }

    public void setGameUuid(UUID gameUuid) {
        this.gameUuid = gameUuid;
    }

    public List<Card> getShuffledDeck() {
        return shuffledDeck;
    }

    public void setShuffledDeck(List<Card> shuffledDeck) {
        this.shuffledDeck = shuffledDeck;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public Map<UUID, List<Card>> getPlayerCards() {
        return playerCards;
    }

    public void setPlayerCards(Map<UUID, List<Card>> playerCards) {
        this.playerCards = playerCards;
    }

    @Override
    public String toString() {
        return "[Game UUID: %s]".formatted(gameUuid);
    }
}
