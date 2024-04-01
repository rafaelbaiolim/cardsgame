package com.go_to.homework.cardgame.domain.repositories;

import com.go_to.homework.cardgame.domain.entity.Deck;

import java.util.Optional;
import java.util.UUID;


public interface DeckRepository extends Repository<Deck> {

    Optional<Deck> findByGameUuid(UUID uuid);
}