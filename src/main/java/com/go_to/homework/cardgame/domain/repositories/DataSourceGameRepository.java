package com.go_to.homework.cardgame.domain.repositories;

import com.go_to.homework.cardgame.domain.models.Game;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class DataSourceGameRepository implements GameRepository {

    private final Map<UUID, Game> gamesByUuid = Collections.synchronizedMap(new HashMap<>());

    @Override
    public synchronized Optional<Game> find(UUID uuid) {
        return Optional.ofNullable(gamesByUuid.get(uuid));
    }

    @Override
    public synchronized Game save(Game model) {
        gamesByUuid.put(model.getUuid(), model);
        return model;
    }

    @Override
    public synchronized void delete(UUID uuid) {
        gamesByUuid.remove(uuid);
    }

    @Override
    public synchronized List<Game> listAll() {
        synchronized (gamesByUuid) {
            return new ArrayList<>(gamesByUuid.values());
        }
    }
}
