package com.go_to.homework.cardgame.domain.builders;

import com.go_to.homework.cardgame.domain.entity.Card;
import com.go_to.homework.cardgame.domain.entity.CardFace;
import com.go_to.homework.cardgame.domain.entity.CardSuit;
import com.go_to.homework.cardgame.domain.entity.Deck;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static com.go_to.homework.cardgame.domain.entity.Card.createCard;

@Component
public class DeckConfigurationBuilder {

    public Deck buildDeck() {
        Deck deck = new Deck();
        deck.setCards(build(deck));
        return deck;
    }

    private Set<Card> build(Deck deck) {
        return Arrays.stream(CardSuit.values())
                .flatMap(suit -> Arrays.stream(CardFace.values())
                        .map(face -> createCard(face, suit, deck)))
                .collect(Collectors.toSet());
    }


}