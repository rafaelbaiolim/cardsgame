package com.go_to.homework.cardgame.domain.repositories;

import com.go_to.homework.cardgame.domain.entity.Game;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class DataSourceGameRepository implements GameRepository {

    private final Map<UUID, Game> gamesByUuid = new HashMap<>();
    private final Object mapLock = new Object();

    @Override
    public Optional<Game> find(UUID uuid) {
        synchronized (mapLock) {
            return Optional.ofNullable(gamesByUuid.get(uuid));
        }
    }

    @Override
    public Game save(Game model) {
        synchronized (mapLock) {
            gamesByUuid.put(model.getUuid(), model);
            return model;
        }
    }

    @Override
    public void delete(UUID uuid) {
        synchronized (mapLock) {
            gamesByUuid.remove(uuid);
        }
    }

    @Override
    public List<Game> listAll() {
        synchronized (mapLock) {
            return new ArrayList<>(gamesByUuid.values());
        }
    }

    @Override
    public Game update(Game game) {
        synchronized (mapLock) {
            if (gamesByUuid.containsKey(game.getUuid())) {
                gamesByUuid.put(game.getUuid(), game);
                return game;
            } else {
                throw new NoSuchElementException("Game with UUID " + game.getUuid() + " not found.");
            }
        }
    }

    @Override
    public void deleteAll() {
        synchronized (mapLock) {
            gamesByUuid.clear();
        }
    }
}