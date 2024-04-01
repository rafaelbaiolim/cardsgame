package com.go_to.homework.cardgame.api;

import com.go_to.homework.cardgame.CardGameApp;
import com.go_to.homework.cardgame.domain.entity.Deck;
import com.go_to.homework.cardgame.domain.repositories.DataSourceDeckRepository;
import com.go_to.homework.cardgame.services.DeckService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CardGameApp.class)
@AutoConfigureMockMvc
public class DecksApiIntegrationTest {

    static final String BASE_DECK_URL = "/api/v1/decks";
    static final int TOTAL_EXHAUSTION_DECKS = 50; //max was tested with 5000

    @Autowired
    private MockMvc mvc;

    @Autowired
    private DeckService deckService;

    @Autowired
    private DataSourceDeckRepository deckRepository;

    @AfterEach
    public void resetDataSource() {
        deckRepository.deleteAll();
    }

    @Test
    public void whenAll_thenCreateExhaustionPersistenceDecks() throws Exception {
        for (int i = 0; i < TOTAL_EXHAUSTION_DECKS; i++) {
            deckService.save();
        }
        mvc.perform(get(BASE_DECK_URL).accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded.deckList", hasSize(equalTo(TOTAL_EXHAUSTION_DECKS))))
                .andDo(print());

    }

    @Test
    public void whenAll_thenCheckCardSuitsDecks() throws Exception {
        for (int i = 0; i < 5; i++) {
            deckService.save();
        }
        final int expectedCardsPerSuit = 13;
        mvc.perform(get(BASE_DECK_URL).accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded.deckList[0].cards[*].suit", hasSize(equalTo(52))))
                .andExpect(jsonPath("$._embedded.deckList[0].cards[?(@.suit == 'HEARTS')]", hasSize(equalTo(expectedCardsPerSuit))))
                .andExpect(jsonPath("$._embedded.deckList[0].cards[?(@.suit == 'SPADES')]", hasSize(equalTo(expectedCardsPerSuit))))
                .andExpect(jsonPath("$._embedded.deckList[0].cards[?(@.suit == 'CLUBS')]", hasSize(equalTo(expectedCardsPerSuit))))
                .andExpect(jsonPath("$._embedded.deckList[0].cards[?(@.suit == 'DIAMONDS')]", hasSize(equalTo(expectedCardsPerSuit))))
                .andDo(print());
    }

    @Test
    public void whenAll_thenRemoveDecksAction() throws Exception {
        Deck deckSaved = deckService.save();
        String deckUuid = deckSaved.getUuid().toString();
        final String REMOVE_DECK_URL = BASE_DECK_URL + "/" + deckUuid;
        mvc.perform(delete(REMOVE_DECK_URL))
                .andDo(print())
                .andExpect(status().isNoContent());
    }


}
