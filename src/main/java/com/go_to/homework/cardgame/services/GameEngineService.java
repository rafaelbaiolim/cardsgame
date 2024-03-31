package com.go_to.homework.cardgame.services;

import com.go_to.homework.cardgame.domain.exceptions.NoMoreCardsException;
import com.go_to.homework.cardgame.domain.models.Card;
import com.go_to.homework.cardgame.domain.models.Game;
import com.go_to.homework.cardgame.domain.models.GameEngine;
import com.go_to.homework.cardgame.domain.repositories.DataSourceGameEngineRepository;
import com.go_to.homework.cardgame.domain.repositories.DataSourceGameRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GameEngineService {

    private final DataSourceGameRepository gameRepository;
    private final DataSourceGameEngineRepository gameEngineRepository;

    public GameEngineService(DataSourceGameRepository gameRepository, DataSourceGameEngineRepository gameEngineRepository) {
        this.gameRepository = gameRepository;
        this.gameEngineRepository = gameEngineRepository;
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
        return gameEngineRepository.save(gameEngine);

    }


}
