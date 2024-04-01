package com.go_to.homework.cardgame.api.assemblers;

import com.go_to.homework.cardgame.api.v1.GameController;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CardsGameSummaryAssembler {

    public EntityModel<Map<String, Long>> toModel(UUID gameUuid, Map<String, Long> summary) {
        return EntityModel.of(summary,
                linkTo(methodOn(GameController.class).getUndealtCards(gameUuid)).withSelfRel(),
                linkTo(methodOn(GameController.class).findByUuid(gameUuid)).withRel("game"));
    }
}

