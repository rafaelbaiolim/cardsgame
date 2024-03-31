package com.go_to.homework.cardgame.api.assemblers;

import com.go_to.homework.cardgame.api.v1.GameController;
import com.go_to.homework.cardgame.api.v1.PlayerController;
import com.go_to.homework.cardgame.domain.models.Player;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PlayerAssembler implements SimpleRepresentationModelAssembler<Player> {

    private void addLinkGame(EntityModel<Player> resource) {
        UUID gameUuid = Objects.requireNonNull(resource.getContent()).getGameUuid();
        if (gameUuid != null) {
            resource.add(linkTo(methodOn(GameController.class).find(gameUuid)).withRel("game"));
        }
    }

    private void addLinkCards(EntityModel<Player> resource) {
        UUID gameUuid = Objects.requireNonNull(resource.getContent()).getGameUuid();
        if (gameUuid != null) {
//            resource.add(linkTo(methodOn(PlayerController.class).find(game.getUuid())).withRel("cards"));
        }
    }

    @Override
    public void addLinks(EntityModel<Player> resource) {
        UUID uuid = Objects.requireNonNull(resource.getContent()).getUuid();
        resource.add(linkTo(methodOn(PlayerController.class).findByUUID(uuid)).withSelfRel());
        resource.add(linkTo(methodOn(PlayerController.class).listAllPlayers()).withRel("players"));
        addLinkGame(resource);
        addLinkCards(resource);
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<Player>> resources) {
        resources.add(linkTo(methodOn(PlayerController.class).listAllPlayers()).withSelfRel());
    }
}
