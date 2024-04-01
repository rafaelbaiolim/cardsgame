package com.go_to.homework.cardgame.api.dto;

import java.util.UUID;

public class PlayerRequest {
    private String name;
    private UUID playerUuid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public void setPlayerUuid(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }
}
