package com.go_to.homework.cardgame.domain.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Game {
    private UUID uuid;
    private String name;
    private List<Player> players = new ArrayList<>();

    private List<Deck> decks = new ArrayList<>();

    public Game() {
        this.uuid = UUID.randomUUID();
    }

    public Game(String name) {
        this();
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Player> getPlayers() {
        return players.stream()
                .sorted(Comparator.comparingInt(Player::getHandValue).reversed())
                .collect(Collectors.toList());
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Deck> getDecks() {
        return decks;
    }

    public void appendDeck(Deck deck) {
        this.decks.add(deck);
        deck.setGameUuid(this.getUuid());
    }

    public void appendPlayer(Player player) {
        this.players.add(player);
        player.setGameUuid(this.getUuid());
    }

    @Override
    public String toString() {
        return "[%s], %s".formatted(uuid, name.toLowerCase());
    }

}
