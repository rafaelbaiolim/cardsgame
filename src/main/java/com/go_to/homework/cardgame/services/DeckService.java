package com.go_to.homework.cardgame.services;

import com.go_to.homework.cardgame.domain.builders.DeckConfigurationBuilder;
import com.go_to.homework.cardgame.domain.events.EntityChangeEvent;
import com.go_to.homework.cardgame.domain.exceptions.DeckOperationException;
import com.go_to.homework.cardgame.domain.entity.Deck;
import com.go_to.homework.cardgame.domain.repositories.DataSourceDeckRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeckService implements AppService<Deck> {

    private final DataSourceDeckRepository deckRepository;
    private final DeckConfigurationBuilder deckConfigurationBuilder;
    private final ApplicationEventPublisher eventPublisher;

    public DeckService(DataSourceDeckRepository deckRepository, DeckConfigurationBuilder deckConfigurationBuilder,
                       ApplicationEventPublisher eventPublisher) {
        this.deckRepository = deckRepository;
        this.deckConfigurationBuilder = deckConfigurationBuilder;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Deck save(String name) {
        return null;
    }

    @Override
    public Deck save() {
        Deck deckSaved = deckRepository.save(deckConfigurationBuilder.buildDeck());
        eventPublisher.publishEvent(new EntityChangeEvent(deckSaved, "New deck created"));
        return deckSaved;
    }

    @Override
    public void delete(UUID uuid) {
        Optional<Deck> deckOptional = deckRepository.find(uuid);
        if (deckOptional.isPresent()) {
            Deck deck = deckOptional.get();
            if (deck.getGameUuid() != null) {
                eventPublisher.publishEvent(new EntityChangeEvent(deck, "Invalid deck operation"));
                throw new DeckOperationException("Deck remove error: This deck already has a relation with a game and cannot be deleted.");
            }
            deckRepository.delete(uuid);
            eventPublisher.publishEvent(new EntityChangeEvent(uuid, "Deck deleted"));
        }
    }

    @Override
    public List listAll() {
        eventPublisher.publishEvent(new EntityChangeEvent(null, "Listed All Decks"));
        return deckRepository.listAll();
    }

    @Override
    public Optional find(UUID uuid) {
        eventPublisher.publishEvent(new EntityChangeEvent(uuid, "Deck was searched"));
        return deckRepository.find(uuid);
    }

    @Override
    public boolean exists(UUID uuid) {
        return deckRepository.find(uuid).isPresent();
    }
}