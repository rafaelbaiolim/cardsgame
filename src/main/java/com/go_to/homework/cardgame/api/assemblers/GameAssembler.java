package com.go_to.homework.cardgame.api.assemblers;

import com.go_to.homework.cardgame.api.v1.GameController;
import com.go_to.homework.cardgame.domain.entity.Game;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GameAssembler implements SimpleRepresentationModelAssembler<Game> {

    @Override
    public void addLinks(EntityModel<Game> resource) {
        UUID uuid = Objects.requireNonNull(resource.getContent()).getUuid();
        resource.add(linkTo(methodOn(GameController.class).findByUuid(uuid)).withSelfRel());
        resource.add(linkTo(methodOn(GameController.class).listAllGames()).withRel("games"));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<Game>> resources) {
        resources.add(linkTo(methodOn(GameController.class).listAllGames()).withSelfRel());
    }
}
