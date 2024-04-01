package com.go_to.homework.cardgame.domain.repositories;

import com.go_to.homework.cardgame.domain.entity.Player;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class DataSourcePlayerRepository implements PlayerRepository {

    private final Map<UUID, Player> playersByUuid = new HashMap<>();
    private final Object mapLock = new Object();

    @Override
    public Optional<Player> find(UUID uuid) {
        synchronized (mapLock) {
            return Optional.ofNullable(playersByUuid.get(uuid));
        }
    }

    @Override
    public Player save(Player model) {
        synchronized (mapLock) {
            playersByUuid.put(model.getUuid(), model);
            return model;
        }
    }

    @Override
    public void delete(UUID uuid) {
        synchronized (mapLock) {
            playersByUuid.remove(uuid);
        }
    }

    @Override
    public List<Player> listAll() {
        synchronized (mapLock) {
            return new ArrayList<>(playersByUuid.values());
        }
    }

    @Override
    public void deleteAll() {
        synchronized (mapLock) {
            playersByUuid.clear();
        }
    }
}
