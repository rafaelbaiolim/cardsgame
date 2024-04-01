package com.go_to.homework.cardgame.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.go_to.homework.cardgame.CardGameApp;
import com.go_to.homework.cardgame.domain.repositories.DataSourceGameEngineRepository;
import com.go_to.homework.cardgame.domain.repositories.DataSourceGameRepository;
import com.go_to.homework.cardgame.services.GameEngineService;
import com.go_to.homework.cardgame.services.GameService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CardGameApp.class)
@AutoConfigureMockMvc
public class GameApiIntegrationTest {

    static final String BASE_PLAYER_URL = "/api/v1/games";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private GameService gameService;

    @Autowired
    private GameEngineService gameEngineService;

    @Autowired
    private DataSourceGameRepository gameRepository;
    @Autowired
    private DataSourceGameEngineRepository gameEngineRepository;

    @AfterEach
    public void resetDataSource() {
        gameRepository.deleteAll();
        gameEngineRepository.deleteAll();
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenAll_thenCheckAppendDeckToGame() throws Exception {
        // 1. Create a game and get its UUID
        String gameResponse = mvc.perform(post("/api/v1/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Game1\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String gameUuid = objectMapper.readTree(gameResponse).get("uuid").asText();

        // 2. Create a deck and get its UUID
        String deckResponse = mvc.perform(post("/api/v1/decks").accept("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String deckUuid = objectMapper.readTree(deckResponse).get("uuid").asText();

        // 4. Append deck to game
        mvc.perform(put("/api/v1/games/" + gameUuid + "/decks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"deckUuid\": \"" + deckUuid + "\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        // Check if game has 1 deck
        mvc.perform(get("/api/v1/games/" + gameUuid).accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.decks", hasSize(1)))
                .andDo(print());
    }

    @Test
    public void whenAll_thenCheckAppendPlayerToGame() throws Exception {
        // 1. Create a game and get its UUID
        String gameResponse = mvc.perform(post("/api/v1/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Game1\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String gameUuid = objectMapper.readTree(gameResponse).get("uuid").asText();

        // 2. Create a player and get its UUID
        String playerResponse = mvc.perform(post("/api/v1/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Player1\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String playerUuid = objectMapper.readTree(playerResponse).get("uuid").asText();

        // 4. Append deck to game
        mvc.perform(put("/api/v1/games/" + gameUuid + "/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"playerUuid\": \"" + playerUuid + "\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        // Check if game has 1 deck
        mvc.perform(get("/api/v1/games/" + gameUuid).accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.players", hasSize(1)))
                .andDo(print());
    }

}
