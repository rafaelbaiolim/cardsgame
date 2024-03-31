package com.go_to.homework.cardgame.api.v1;

import com.go_to.homework.cardgame.api.assemblers.GameAssembler;
import com.go_to.homework.cardgame.domain.models.Deck;
import com.go_to.homework.cardgame.domain.models.Game;
import com.go_to.homework.cardgame.domain.models.Player;
import com.go_to.homework.cardgame.services.DeckService;
import com.go_to.homework.cardgame.domain.models.GameAssociation;
import com.go_to.homework.cardgame.services.GameService;
import com.go_to.homework.cardgame.services.PlayerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@RestController
@RequestMapping("/v1/games")
public class GameController {

    private final GameService gameService;
    private final DeckService deckService;
    private final PlayerService playerService;
    private final GameAssembler gameAssembler;


    public GameController(GameService gameService, GameAssembler gameAssembler, DeckService deckService, PlayerService playerService) {

        this.gameService = gameService;
        this.gameAssembler = gameAssembler;
        this.deckService = deckService;
        this.playerService = playerService;
    }

    @GetMapping
    public ResponseEntity<Object> listAllGames() {
        CollectionModel<EntityModel<Game>> games = gameAssembler.toCollectionModel(gameService.listAll());
        return ResponseEntity.ok(games);
    }

    @PostMapping
    public EntityModel<Game> newGame(@RequestParam String name) {
        return gameAssembler.toModel(gameService.save(name));
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
    public ResponseEntity<EntityModel<Game>> find(@PathVariable UUID uuid) {
        Optional<Game> gameOptional = gameService.find(uuid);

        return gameOptional.map(game -> ResponseEntity.ok(gameAssembler.toModel(game))).orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{gameUuid}/decks")
    public ResponseEntity<?> addDeckToGame(@PathVariable UUID gameUuid, @RequestParam UUID deckUuid) {
        return associateWithGame(gameUuid, deckUuid, deckService::find, Game::appendDeck);
    }

    @PutMapping("/{gameUuid}/players")
    public ResponseEntity<?> addPlayerToGame(@PathVariable UUID gameUuid, @RequestParam UUID playerUuid) {
        return associateWithGame(gameUuid, playerUuid, playerService::find, Game::appendPlayer);
    }

    private <T> ResponseEntity<?> associateWithGame(UUID gameUuid, UUID entityUuid,
                                                    Function<UUID, Optional<T>> findEntity,
                                                    GameAssociation<T> associationAction) {
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


}