package com.go_to.homework.cardgame.api.assemblers;

import com.go_to.homework.cardgame.api.v1.GameController;
import com.go_to.homework.cardgame.domain.models.GameEngine;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GameEngineAssembler implements SimpleRepresentationModelAssembler<GameEngine> {

    @Override
    public void addLinks(EntityModel<GameEngine> resource) {
        UUID uuid = Objects.requireNonNull(resource.getContent()).getGameUuid();
        resource.add(linkTo(methodOn(GameController.class).findByUuid(uuid)).withRel("game"));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<GameEngine>> resources) {
        resources.add(linkTo(methodOn(GameController.class).listAllGames()).withSelfRel());
    }
}
