package com.go_to.homework.cardgame.api.v1;

import com.go_to.homework.cardgame.api.assemblers.GameEngineAssembler;
import com.go_to.homework.cardgame.api.assemblers.PlayerAssembler;
import com.go_to.homework.cardgame.domain.entity.Player;
import com.go_to.homework.cardgame.services.GameEngineService;
import com.go_to.homework.cardgame.services.PlayerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Tag(name = "Players")
@RestController
@RequestMapping("/api/v1/players")
public class PlayerController {

    private final PlayerService playerService;
    private final GameEngineService gameEngineService;
    private final GameEngineAssembler gameEngineAssembler;
    private final PlayerAssembler playerAssembler;

    public PlayerController(PlayerService playerService, GameEngineService gameEngineService, GameEngineAssembler gameEngineAssembler, PlayerAssembler playerAssembler) {
        super();
        this.playerService = playerService;
        this.gameEngineService = gameEngineService;
        this.gameEngineAssembler = gameEngineAssembler;
        this.playerAssembler = playerAssembler;
    }

    @GetMapping
    public ResponseEntity<Object> listAllPlayers() {
        CollectionModel players = playerAssembler.toCollectionModel(playerService.listAll());
        return ResponseEntity.ok(players);
    }

    @PostMapping
    public EntityModel<Player> newPlayer(@RequestParam String name) {
        return playerAssembler.toModel(playerService.save(name));
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deletePlayer(@PathVariable UUID uuid) {
        boolean exists = playerService.exists(uuid);
        if (!exists) {
            return ResponseEntity.notFound().build();
        }

        playerService.delete(uuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<EntityModel<Player>> findByUUID(@PathVariable UUID uuid) {
        Optional<Player> playerOptional = playerService.find(uuid);

        return playerOptional.map(player -> ResponseEntity.ok(playerAssembler.toModel(player))).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("{playeruuid}/cards")
    public ResponseEntity<EntityModel<Player>> cardsOfPlayer(@PathVariable(name = "playeruuid") UUID playerUuid) {
        EntityModel<Player> gameEngineModel = playerAssembler.toModel(gameEngineService.getPlayerCards(playerUuid));
        return ResponseEntity.ok(gameEngineModel);
    }
}
