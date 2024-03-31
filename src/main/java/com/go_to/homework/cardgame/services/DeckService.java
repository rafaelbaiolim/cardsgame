package com.go_to.homework.cardgame.services;

import com.go_to.homework.cardgame.domain.builders.DeckConfigurationBuilder;
import com.go_to.homework.cardgame.domain.exceptions.DeckOperationException;
import com.go_to.homework.cardgame.domain.models.Deck;
import com.go_to.homework.cardgame.domain.repositories.DataSourceDeckRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeckService implements AppService<Deck> {

    private final DataSourceDeckRepository deckRepository;
    private final DeckConfigurationBuilder deckConfigurationBuilder;

    public DeckService(DataSourceDeckRepository deckRepository, DeckConfigurationBuilder deckConfigurationBuilder) {
        this.deckRepository = deckRepository;
        this.deckConfigurationBuilder = deckConfigurationBuilder;
    }

    @Override
    public Deck save(String name) {
        return null;
    }

    @Override
    public Deck save() {
        return deckRepository.save(deckConfigurationBuilder.buildDeck());
    }

    @Override
    public void delete(UUID uuid) {
        Optional<Deck> deckOptional = deckRepository.find(uuid);
        if (deckOptional.isPresent()) {
            Deck deck = deckOptional.get();
            if (deck.getGameUuid() != null) {
                throw new DeckOperationException("Deck remove error: This deck already has a relation with a game and cannot be deleted.");
            }
            deckRepository.delete(uuid);
        }
    }

    @Override
    public List listAll() {
        return deckRepository.listAll();
    }

    @Override
    public Optional find(UUID uuid) {
        return deckRepository.find(uuid);
    }

    @Override
    public boolean exists(UUID uuid) {
        return deckRepository.find(uuid).isPresent();
    }
}