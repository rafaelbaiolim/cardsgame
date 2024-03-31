package com.go_to.homework.cardgame.domain.builders;

import com.go_to.homework.cardgame.domain.models.Card;
import com.go_to.homework.cardgame.domain.models.CardFace;
import com.go_to.homework.cardgame.domain.models.CardSuit;
import com.go_to.homework.cardgame.domain.models.Deck;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static com.go_to.homework.cardgame.domain.models.Card.createCard;

@Component
public class DeckConfigurationBuilder {

    public Deck buildDeck() {
        Deck deck = new Deck();
        deck.setCards(build(deck));
        return deck;
    }

    private Set<Card> build(Deck deck) {
        Set<Card> cards = new HashSet<>();
        for (CardSuit suit : CardSuit.values()) {
            for (CardFace face : CardFace.values()) {
                cards.add(createCard(face, suit, deck));
            }
        }

        return cards;
    }


}