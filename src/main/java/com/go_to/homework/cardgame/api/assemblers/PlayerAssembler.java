package com.go_to.homework.cardgame.api.assemblers;

import com.go_to.homework.cardgame.api.v1.GameController;
import com.go_to.homework.cardgame.api.v1.PlayerController;
import com.go_to.homework.cardgame.domain.models.Player;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PlayerAssembler implements SimpleRepresentationModelAssembler<Player> {

    private void addLinkGame(EntityModel<Player> resource) {
        UUID gameUuid = Objects.requireNonNull(resource.getContent()).getGameUuid();
        if (gameUuid != null) {
            resource.add(linkTo(methodOn(GameController.class).findByUuid(gameUuid)).withRel("game"));
        }
    }


    @Override
    public void addLinks(EntityModel<Player> resource) {
        UUID uuid = Objects.requireNonNull(resource.getContent()).getUuid();
        resource.add(linkTo(methodOn(PlayerController.class).findByUUID(uuid)).withSelfRel());
        resource.add(linkTo(methodOn(PlayerController.class).listAllPlayers()).withRel("players"));
        addLinkGame(resource);
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<Player>> resources) {
        resources.add(linkTo(methodOn(PlayerController.class).listAllPlayers()).withSelfRel());
    }

    public CollectionModel<EntityModel<Player>> toCollectionModel(List<Player> players) {
        List<EntityModel<Player>> playerModels = players.stream()
                .map(player -> EntityModel.of(player,
                        linkTo(methodOn(PlayerController.class).findByUUID(player.getUuid())).withSelfRel(),
                        linkTo(methodOn(GameController.class).findByUuid(player.getGameUuid())).withRel("gamePlayers")))
                .collect(Collectors.toList());

        return CollectionModel.of(playerModels,
                linkTo(methodOn(GameController.class).findByUuid(players.get(0).getGameUuid())).withSelfRel());
    }
}
