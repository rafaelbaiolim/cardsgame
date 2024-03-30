package com.go_to.homework.cardgame.api.v1;

import com.go_to.homework.cardgame.api.assemblers.GameAssembler;
import com.go_to.homework.cardgame.domain.models.Game;
import com.go_to.homework.cardgame.services.GameService;
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
    private final GameAssembler gameAssembler;

    public GameController(GameService gameService, GameAssembler gameAssembler) {

        this.gameService = gameService;
        this.gameAssembler = gameAssembler;
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
}
