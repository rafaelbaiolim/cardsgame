package com.go_to.homework.cardgame.services;

import com.go_to.homework.cardgame.domain.exceptions.NoMoreCardsException;
import com.go_to.homework.cardgame.domain.models.*;
import com.go_to.homework.cardgame.domain.repositories.DataSourceGameEngineRepository;
import com.go_to.homework.cardgame.domain.repositories.DataSourceGameRepository;
import com.go_to.homework.cardgame.domain.repositories.DataSourcePlayerRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameEngineService {

    private final DataSourceGameRepository gameRepository;
    private final DataSourceGameEngineRepository gameEngineRepository;

    private final DataSourcePlayerRepository playerRepository;

    public GameEngineService(DataSourceGameRepository gameRepository,
                             DataSourceGameEngineRepository gameEngineRepository, DataSourcePlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.gameEngineRepository = gameEngineRepository;
        this.playerRepository = playerRepository;
    }

    public Map<String, Long> getUndealtCards(UUID gameUuid) {

        GameEngine gameEngine = gameEngineRepository.find(gameUuid)
                .orElseThrow(() -> new IllegalArgumentException("Game not found for UUID: " + gameUuid));

        List<Card> undealtCards = gameEngine.getShuffledDeck();

        return undealtCards.stream()
                .collect(Collectors.groupingBy(card -> card.getSuit().toString(), Collectors.counting()));
    }

    public GameEngine shuffleCards(UUID gameUuid) {

        Game game = gameRepository.find(gameUuid)
                .orElseThrow(() -> new IllegalArgumentException("Game not found for UUID: " + gameUuid));

        List<Card> mergedCards = game.getDecks().stream()
                .flatMap(deck -> deck.getCards().stream())
                .collect(Collectors.toList());

        Collections.shuffle(mergedCards);

        GameEngine gameEngine = gameEngineRepository.find(gameUuid)
                .map(existingGameEngine -> {
                    existingGameEngine.setShuffledDeck(mergedCards);
                    return existingGameEngine;
                })
                .orElseGet(() -> {
                    GameEngine newGameEngine = new GameEngine();
                    newGameEngine.setGameUuid(gameUuid);
                    newGameEngine.setShuffledDeck(mergedCards);
                    return newGameEngine;
                });

        return gameEngineRepository.save(gameEngine);

    }

    public GameEngine dealCardToPlayer(UUID gameUuid) {
        GameEngine gameEngine = gameEngineRepository.find(gameUuid)
                .orElseThrow(() -> new IllegalArgumentException("Please shuffle cards before deal it to Players: " + gameUuid));

        if (gameEngine.getShuffledDeck().isEmpty()) {
            throw new NoMoreCardsException("No more cards to deal. The game is over.");
        }

        Game game = gameRepository.find(gameUuid)
                .orElseThrow(() -> new IllegalArgumentException("Game not found for UUID: " + gameUuid));

        UUID currentPlayerUuid = game.getPlayers().get(gameEngine.getCurrentPlayerIndex()).getUuid();
        Card cardToDeal = gameEngine.getShuffledDeck().remove(0);
        gameEngine.getPlayerCards().computeIfAbsent(currentPlayerUuid, k -> new ArrayList<>()).add(cardToDeal);

        int nextPlayerIndex = (gameEngine.getCurrentPlayerIndex() + 1) % game.getPlayers().size();
        gameEngine.setCurrentPlayerIndex(nextPlayerIndex);

        GameEngine gameEngineSaved = gameEngineRepository.save(gameEngine);
        return gameEngineSaved;
    }

    public Player getPlayerCards(UUID playerUuid) {
        Optional<GameEngine> gameEngine = Optional.ofNullable(gameEngineRepository.getCurrentCardsFromPlayer(playerUuid)
                .orElseThrow(() -> new IllegalArgumentException("Player cards not found for UUID: " + playerUuid)));

        Optional<Player> player = playerRepository.find(playerUuid);
        Player currentPlayer = player.get();
        Player updatedPLayer = new Player(currentPlayer.getName(), currentPlayer.getUuid(), currentPlayer.getGameUuid(),
                gameEngine.get().getPlayerCards().get(playerUuid));

        return updatedPLayer;
    }


}
