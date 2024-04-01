package com.go_to.homework.cardgame.api;

import com.go_to.homework.cardgame.CardGameApp;
import com.go_to.homework.cardgame.domain.entity.Player;
import com.go_to.homework.cardgame.domain.repositories.DataSourcePlayerRepository;
import com.go_to.homework.cardgame.services.PlayerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CardGameApp.class)
@AutoConfigureMockMvc
public class PlayersApiIntegrationTest {

    static final String BASE_PLAYER_URL = "/api/v1/players";
    static final int TOTAL_EXHAUSTION_PLAYERS = 5; //max was tested with 5000

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private DataSourcePlayerRepository playerRepository;

    @AfterEach
    public void resetDataSource() {
        playerRepository.deleteAll();
    }

    @Test
    public void whenAll_thenCreateExhaustionPersistencePlayers() throws Exception {
        for (int i = 0; i < TOTAL_EXHAUSTION_PLAYERS; i++) {
            playerService.save("Player" + i);
        }
        mvc.perform(get(BASE_PLAYER_URL).accept(MediaTypes.HAL_JSON_VALUE)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded.playerList", hasSize(equalTo(TOTAL_EXHAUSTION_PLAYERS))))
                .andDo(print());
    }

    @Test
    public void whenAll_thenPlayerNamesExist() throws Exception {
        for (int i = 0; i < 3; i++) {
            playerService.save("Player" + i);
        }
        mvc.perform(get(BASE_PLAYER_URL).accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded.playerList[0].name", not(emptyOrNullString())))
                .andExpect(jsonPath("$._embedded.playerList[1].name", not(emptyOrNullString())))
                .andExpect(jsonPath("$._embedded.playerList[2].name", not(emptyOrNullString())))
                .andDo(print());
    }

    @Test
    public void whenAll_thenRemovePlayersAction() throws Exception {
        Player playerSaved = playerService.save("Player1");
        String playerUuid = playerSaved.getUuid().toString();
        final String REMOVE_PLAYER_URL = BASE_PLAYER_URL + "/" + playerUuid;
        mvc.perform(delete(REMOVE_PLAYER_URL)).andDo(print()).andExpect(status().isNoContent());
    }


}
