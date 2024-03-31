package com.go_to.homework.cardgame.domain.repositories;

import com.go_to.homework.cardgame.domain.models.Player;
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

//    @Override
//    public Player update(Player player) {
//        synchronized (mapLock) {
//            if (playersByUuid.containsKey(player.getUuid())) {
//                playersByUuid.put(player.getUuid(), player);
//                return player;
//            } else {
//                throw new NoSuchElementException("Player with UUID " + player.getUuid() + " not found.");
//            }
//        }
//    }
}