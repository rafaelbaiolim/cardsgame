package com.go_to.homework.cardgame.api.v1;

import com.go_to.homework.cardgame.api.assemblers.CardsGameSummaryAssembler;
import com.go_to.homework.cardgame.api.assemblers.GameAssembler;
import com.go_to.homework.cardgame.api.assemblers.GameEngineAssembler;
import com.go_to.homework.cardgame.api.assemblers.PlayerAssembler;
import com.go_to.homework.cardgame.api.dto.DeckRequest;
import com.go_to.homework.cardgame.api.dto.GameRequest;
import com.go_to.homework.cardgame.api.dto.PlayerRequest;
import com.go_to.homework.cardgame.domain.entity.Deck;
import com.go_to.homework.cardgame.domain.entity.Game;
import com.go_to.homework.cardgame.domain.entity.GameEngine;
import com.go_to.homework.cardgame.domain.entity.Player;
import com.go_to.homework.cardgame.domain.exceptions.NoMoreCardsException;
import com.go_to.homework.cardgame.services.DeckService;
import com.go_to.homework.cardgame.services.GameEngineService;
import com.go_to.homework.cardgame.services.GameService;
import com.go_to.homework.cardgame.services.PlayerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Tag(name = "Games")
@RestController
@RequestMapping("/api/v1/games")
public class GameController {

    private final GameService gameService;

    private final GameEngineService gameEngineService;
    private final DeckService deckService;
    private final PlayerService playerService;
    private final GameAssembler gameAssembler;

    private final GameEngineAssembler gameEngineAssembler;

    private final PlayerAssembler playerAssembler;

    private final CardsGameSummaryAssembler cardsGameSummaryAssembler;

    public GameController(GameService gameService, GameEngineService gameEngineService, GameAssembler gameAssembler, DeckService deckService, PlayerService playerService, GameEngineAssembler gameEngineAssembler, PlayerAssembler playerAssembler, CardsGameSummaryAssembler cardsGameSummaryAssembler) {

        this.gameService = gameService;
        this.gameEngineService = gameEngineService;
        this.gameAssembler = gameAssembler;
        this.deckService = deckService;
        this.playerService = playerService;
        this.gameEngineAssembler = gameEngineAssembler;
        this.playerAssembler = playerAssembler;
        this.cardsGameSummaryAssembler = cardsGameSummaryAssembler;
    }

    @GetMapping
    public ResponseEntity<Object> listAllGames() {
        CollectionModel<EntityModel<Game>> games = gameAssembler.toCollectionModel(gameService.listAll());
        return ResponseEntity.ok(games);
    }

    @PostMapping
    public EntityModel<Game> newGame(@RequestBody GameRequest request) {
        return gameAssembler.toModel(gameService.save(request.getName()));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteGame(@PathVariable UUID uuid) {
        boolean exists = gameService.exists(uuid);
        if (!exists) {
            return ResponseEntity.notFound().build();
        }

        gameService.delete(uuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<EntityModel<Game>> findByUuid(@PathVariable UUID uuid) {
        Optional<Game> gameOptional = gameService.find(uuid);
        return gameOptional.map(game -> ResponseEntity.ok(gameAssembler.toModel(game))).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{uuid}/players")
    public ResponseEntity<CollectionModel<EntityModel<Player>>> getPlayersOfGame(@PathVariable UUID uuid) {
        Optional<Game> gameOptional = gameService.find(uuid);

        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            List<Player> players = game.getPlayers();

            CollectionModel<EntityModel<Player>> playerCollectionModel = playerAssembler.toCollectionModel(players);

            return ResponseEntity.ok(playerCollectionModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{uuid}/cards/undealt")
    public ResponseEntity<EntityModel<Map<String, Long>>> getUndealtCards(@PathVariable UUID uuid) {
        Map<String, Long> undealtCards = gameEngineService.getUndealtCards(uuid);
        return ResponseEntity.ok(cardsGameSummaryAssembler.toModel(uuid, undealtCards));
    }

    @PutMapping("/{gameUuid}/decks")
    public ResponseEntity<?> addDeckToGame(@PathVariable UUID gameUuid, @RequestBody DeckRequest deckRequest) {
        return associateWithGame(gameUuid, deckRequest.getDeckUuid(), deckService::find, Game::appendDeck);
    }

    @PutMapping("/{gameUuid}/players")
    public ResponseEntity<?> addPlayerToGame(@PathVariable UUID gameUuid, @RequestBody PlayerRequest playerRequest) {
        return associateWithGame(gameUuid, playerRequest.getPlayerUuid(), playerService::find, Game::appendPlayer);
    }

    private <T> ResponseEntity<?> associateWithGame(UUID gameUuid, UUID entityUuid, Function<UUID, Optional<T>> findEntity, GameAssociation<T> associationAction) {
        Game game = gameService.find(gameUuid).orElseThrow(() -> new EntityNotFoundException("Game not found"));
        T entity = findEntity.apply(entityUuid).orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        checkEntityAssociationWithGame(entity);
        associationAction.associate(game, entity);
        return ResponseEntity.ok(gameAssembler.toModel(gameService.update(game)));
    }

    private void checkEntityAssociationWithGame(Object entity) {
        if (entity instanceof Deck deck) {
            if (deck.getGameUuid() != null) {
                throw new IllegalStateException("Deck is already associated with a game");
            }
        } else if (entity instanceof Player player) {
            if (player.getGameUuid() != null) {
                throw new IllegalStateException("Player is already associated with a game");
            }
        } else {
            throw new IllegalArgumentException("Unsupported entity type for game association");
        }
    }

    @PatchMapping("/{gameUuid}/shuffleDeckCards")
    public ResponseEntity<?> shuffleGameEngineCards(@PathVariable UUID gameUuid) {
        try {
            GameEngine gameEngine = gameEngineService.shuffleCards(gameUuid);
            Object gameEngineModel = gameEngineAssembler.toModel(gameEngine);
            return ResponseEntity.ok(gameEngineModel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping("/{gameUuid}/dealCards")
    public ResponseEntity<?> dealGameEngineDeckCards(@PathVariable UUID gameUuid) {
        try {
            GameEngine gameEngine = gameEngineService.dealCardToPlayer(gameUuid);
            Object gameEngineModel = gameEngineAssembler.toModel(gameEngine);
            return ResponseEntity.ok(gameEngineModel);
        } catch (NoMoreCardsException e) {
            return ResponseEntity.ok(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}