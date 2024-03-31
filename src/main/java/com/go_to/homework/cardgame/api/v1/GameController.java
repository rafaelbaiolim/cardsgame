package com.go_to.homework.cardgame.api.v1;

import com.go_to.homework.cardgame.api.assemblers.GameAssembler;
import com.go_to.homework.cardgame.domain.models.Deck;
import com.go_to.homework.cardgame.domain.models.Game;
import com.go_to.homework.cardgame.services.DeckService;
import com.go_to.homework.cardgame.services.GameService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/games")
public class GameController {

    private final GameService gameService;
    private final DeckService deckService;
    private final GameAssembler gameAssembler;


    public GameController(GameService gameService, GameAssembler gameAssembler, DeckService deckService) {

        this.gameService = gameService;
        this.gameAssembler = gameAssembler;
        this.deckService = deckService;
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

        return gameOptional.map(game ->
                ResponseEntity.ok(gameAssembler.toModel(game))
        ).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{gameUuid}/decks")
    public ResponseEntity<?> addDeckToGame(@PathVariable UUID gameUuid, @RequestParam UUID deckUuid) {
        Optional<Game> gameOptional = gameService.find(gameUuid);
        Optional<Deck> deckOptional = deckService.find(deckUuid);

        if (!gameOptional.isPresent()) {
            throw new EntityNotFoundException("Game not found");
        }

        if (!deckOptional.isPresent()) {
            throw new EntityNotFoundException("Deck not found");
        }

        Game gameToUpdate = gameOptional.get();
        Deck deck = deckOptional.get();

        if (deck.getGame() != null) {
            throw new IllegalStateException("Deck is already associated with a game");
        }
        gameToUpdate.appendDeck(deck);
        return ResponseEntity.ok(gameAssembler.toModel(gameService.update(gameToUpdate)));
    }
}