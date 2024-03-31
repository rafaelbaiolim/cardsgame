package com.go_to.homework.cardgame.domain.repositories;

import com.go_to.homework.cardgame.domain.models.GameEngine;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class DataSourceGameEngineRepository implements GameEngineRepository {

    private final Map<UUID, GameEngine> gameEnginesByUuid = new HashMap<>();
    private final Object mapLock = new Object();

    @Override
    public Optional<GameEngine> find(UUID uuid) {
        synchronized (mapLock) {
            return Optional.ofNullable(gameEnginesByUuid.get(uuid));
        }
    }

    @Override
    public GameEngine save(GameEngine model) {
        synchronized (mapLock) {
            gameEnginesByUuid.put(model.getGameUuid(), model);
            return model;
        }
    }

    @Override
    public void delete(UUID uuid) {
        synchronized (mapLock) {
            gameEnginesByUuid.remove(uuid);
        }
    }

    @Override
    public List<GameEngine> listAll() {
        synchronized (mapLock) {
            return new ArrayList<>(gameEnginesByUuid.values());
        }
    }

}
