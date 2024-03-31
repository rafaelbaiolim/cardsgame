package com.go_to.homework.cardgame.api.v1;

import com.go_to.homework.cardgame.api.assemblers.DeckAssembler;
import com.go_to.homework.cardgame.domain.models.Deck;
import com.go_to.homework.cardgame.services.DeckService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/decks")
public class DeckController {

    private final DeckService deckService;
    private final DeckAssembler deckAssembler;

    public DeckController(DeckService deckService, DeckAssembler deckAssembler) {
        super();
        this.deckService = deckService;
        this.deckAssembler = deckAssembler;
    }

    @GetMapping
    public ResponseEntity<Object> listAllDecks() {
        CollectionModel decks = deckAssembler.toCollectionModel(deckService.listAll());
        return ResponseEntity.ok(decks);
    }

    @PostMapping
    public EntityModel<Deck> newDeck() {
        return deckAssembler.toModel(deckService.save());
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteDeck(@PathVariable UUID uuid) {
        boolean exists = deckService.exists(uuid);
        if (!exists) {
            return ResponseEntity.notFound().build();
        }

        deckService.delete(uuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<EntityModel<Deck>> findByUUID(@PathVariable UUID uuid) {
        Optional<Deck> deckOptional = deckService.find(uuid);

        return deckOptional.map(deck ->
                ResponseEntity.ok(deckAssembler.toModel(deck))
        ).orElse(ResponseEntity.notFound().build());
    }
}
