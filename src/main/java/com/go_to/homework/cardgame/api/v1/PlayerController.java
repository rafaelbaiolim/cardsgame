package com.go_to.homework.cardgame.api.v1;

import com.go_to.homework.cardgame.api.assemblers.PlayerAssembler;
import com.go_to.homework.cardgame.domain.models.Player;
import com.go_to.homework.cardgame.services.PlayerService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/players")
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerAssembler playerAssembler;

    public PlayerController(PlayerService playerService, PlayerAssembler playerAssembler) {
        super();
        this.playerService = playerService;
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
}
