package com.go_to.homework.cardgame.domain.models;

import java.util.*;

public class Game {
    private UUID uuid;
    private String name;
    private Set<Player> players = new HashSet<>();

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

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }


    public List<Deck> getDecks() {
        return decks;
    }

    public void setDecks(List<Deck> decks) {
        this.decks = decks;
    }

}
