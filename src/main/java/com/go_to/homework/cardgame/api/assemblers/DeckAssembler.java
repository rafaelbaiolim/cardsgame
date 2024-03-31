package com.go_to.homework.cardgame.api.assemblers;

import com.go_to.homework.cardgame.api.v1.DeckController;
import com.go_to.homework.cardgame.api.v1.GameController;
import com.go_to.homework.cardgame.domain.models.Deck;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DeckAssembler implements SimpleRepresentationModelAssembler<Deck> {

    private void addLinkGame(EntityModel<Deck> resource) {
        UUID gameUuid = Objects.requireNonNull(resource.getContent()).getGameUuid();
        if (gameUuid != null) {
            resource.add(linkTo(methodOn(GameController.class).find(gameUuid)).withRel("game"));
        }
    }

    @Override
    public void addLinks(EntityModel<Deck> resource) {
        UUID uuid = Objects.requireNonNull(resource.getContent()).getUuid();
        resource.add(linkTo(methodOn(DeckController.class).findByUUID(uuid)).withSelfRel());
        resource.add(linkTo(methodOn(DeckController.class).listAllDecks()).withRel("decks"));
        addLinkGame(resource);
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<Deck>> resources) {
        resources.add(linkTo(methodOn(DeckController.class).listAllDecks()).withSelfRel());
    }
}
