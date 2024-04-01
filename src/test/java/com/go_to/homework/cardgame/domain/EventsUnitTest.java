package com.go_to.homework.cardgame.domain;

import com.go_to.homework.cardgame.domain.entity.Player;
import com.go_to.homework.cardgame.domain.events.listener.EntityChangeListener;
import com.go_to.homework.cardgame.services.PlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("tests")
public class EventsUnitTest {

    @Autowired
    private EntityChangeListener entityChangeListener;

    @Autowired
    private PlayerService playerService;

    @Test
    public void whenEventOccurred_CheckHistory() {
        Player playerObj = playerService.save("Player1");
        playerService.delete(playerObj.getUuid());
        assertThat(entityChangeListener.getEventHistory(), hasSize(2));
    }
}
