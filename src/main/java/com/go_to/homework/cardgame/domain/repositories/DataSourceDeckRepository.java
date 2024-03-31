package com.go_to.homework.cardgame.domain.repositories;

import com.go_to.homework.cardgame.domain.models.Deck;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class DataSourceDeckRepository implements DeckRepository {

    private final Map<UUID, Deck> decksByUuid = new HashMap<>();
    private final Object mapLock = new Object();

    @Override
    public Optional<Deck> find(UUID uuid) {
        synchronized (mapLock) {
            return Optional.ofNullable(decksByUuid.get(uuid));
        }
    }

    @Override
    public Deck save(Deck model) {
        synchronized (mapLock) {
            decksByUuid.put(model.getUuid(), model);
            return model;
        }
    }

    @Override
    public void delete(UUID uuid) {
        synchronized (mapLock) {
            decksByUuid.remove(uuid);
        }
    }

    @Override
    public List<Deck> listAll() {
        synchronized (mapLock) {
            return new ArrayList<>(decksByUuid.values());
        }
    }

    @Override
    public Optional<Deck> findByGameUuid(UUID uuid) {
        synchronized (mapLock) {
            return decksByUuid.values().stream()
                    .filter(deck -> deck.getGame() != null && deck.getGame().getUuid().equals(uuid))
                    .findFirst();
        }
    }
}
