package com.go_to.homework.cardgame.services;

import com.go_to.homework.cardgame.domain.entity.Game;
import com.go_to.homework.cardgame.domain.entity.GameEngine;
import com.go_to.homework.cardgame.domain.entity.Player;
import com.go_to.homework.cardgame.domain.exceptions.NoMoreCardsException;
import com.go_to.homework.cardgame.domain.repositories.DataSourceGameEngineRepository;
import com.go_to.homework.cardgame.domain.repositories.DataSourceGameRepository;
import com.go_to.homework.cardgame.domain.repositories.DataSourcePlayerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("tests")
public class GameEngineServiceIntegrationTest {
    @Autowired
    private DeckService deckService;
    @Autowired
    private GameService gameService;

    @Autowired
    private GameEngineService gameEngineService;

    @Autowired
    private DataSourceGameRepository gameRepository;

    @Autowired
    private DataSourcePlayerRepository playerRepository;

    @Autowired
    private DataSourceGameEngineRepository gameEngineRepository;

    @AfterEach
    public void resetDb() {
        gameRepository.deleteAll();
        playerRepository.deleteAll();
        gameEngineRepository.deleteAll();
    }

    @Test
    public void whenDealingCard_Then_ShouldAvoidInvalidDealCard() {
        Game gameTest = gameService.save("GameTest");
        gameTest.appendPlayer(new Player("PlayerTest"));
        gameTest.appendDeck(deckService.save());
        Game gameUpdated = gameService.update(gameTest);

        gameEngineService.shuffleCards(gameUpdated.getUuid());

        for (int i = 0; i < 52; i++) {
            GameEngine gameEngine = gameEngineService.dealCardToPlayer(gameUpdated.getUuid());
        }

        assertThrows(NoMoreCardsException.class, () ->
                gameEngineService.dealCardToPlayer(gameUpdated.getUuid()), "No more cards to deal. The game is over.");

    }

    @Test
    public void whenDealingCard_Then_ShouldEachPlayerReceiveACard() {
        Game gameTest = gameService.save("GameTest");
        Player player1 = new Player("PlayerTest");
        Player player2 = new Player("PlayerTest");
        gameTest.appendPlayer(player1);
        gameTest.appendPlayer(player2);
        gameTest.appendDeck(deckService.save());
        Game gameUpdated = gameService.update(gameTest);

        gameEngineService.shuffleCards(gameUpdated.getUuid());

        for (int i = 0; i < 4; i++) {
            GameEngine gameEngine = gameEngineService.dealCardToPlayer(gameUpdated.getUuid());
        }

        assertEquals(2, gameEngineService.getPlayerCards(player1.getUuid())
                .getPlayerCards().size(), "Player1 should have 2 cards");
        assertEquals(2, gameEngineService.getPlayerCards(player2.getUuid())
                .getPlayerCards().size(), "Player2 should have 2 cards");

    }
}
