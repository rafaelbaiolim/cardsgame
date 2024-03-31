package com.go_to.homework.cardgame.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public class Player {
    private UUID uuid;
    private String name;
    @JsonIgnore
    private UUID gameUuid;

    public Player(String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;
//        this.game = game;
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
}
