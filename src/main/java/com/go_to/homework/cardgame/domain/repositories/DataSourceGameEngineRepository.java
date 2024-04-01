package com.go_to.homework.cardgame.domain.repositories;

import com.go_to.homework.cardgame.domain.models.Card;
import com.go_to.homework.cardgame.domain.models.Game;
import com.go_to.homework.cardgame.domain.models.GameEngine;
import com.go_to.homework.cardgame.domain.models.Player;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class DataSourceGameEngineRepository implements GameEngineRepository {
    private final DataSourceGameRepository gameRepository;
    private final DataSourcePlayerRepository playerRepository;
    private final Map<UUID, GameEngine> gameEnginesByUuid = new HashMap<>();
    private final Object mapLock = new Object();

    public DataSourceGameEngineRepository(DataSourceGameRepository gameRepository, DataSourcePlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

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
            updateGameWithNewPlayerInfo(model);
            return model;
        }
    }

    private void updateGameWithNewPlayerInfo(GameEngine gameEngine) {
        Game game = gameRepository.find(gameEngine.getGameUuid())
                .orElseThrow(() -> new IllegalArgumentException("Game not found for UUID: " + gameEngine.getGameUuid()));

        List<Player> updatedPlayers = new ArrayList<>(game.getPlayers());

        gameEngine.getPlayerCards().entrySet().forEach(entry -> {
            UUID playerUuid = entry.getKey();
            List<Card> cards = entry.getValue();

            updatedPlayers.forEach(player -> {
                if (player.getUuid().equals(playerUuid)) {
                    player.setPlayerCards(cards);
                    playerRepository.save(player);
                }
            });
        });
        game.setPlayers(updatedPlayers);

        gameRepository.update(game);
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

    public Optional<GameEngine> getCurrentCardsFromPlayer(UUID playerUuid) {
        for (GameEngine gameEngine : gameEnginesByUuid.values()) {
            List<Card> cards = gameEngine.getPlayerCards().get(playerUuid);
            if (cards != null) {
                return Optional.of(gameEngine);
            }
        }
        return Optional.empty();
    }

}
