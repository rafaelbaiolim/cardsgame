package com.go_to.homework.cardgame.api.v1;

import com.go_to.homework.cardgame.domain.events.listener.EntityChangeListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventHistoryController {

    private final EntityChangeListener entityChangeListener;

    public EventHistoryController(EntityChangeListener entityChangeListener) {
        this.entityChangeListener = entityChangeListener;
    }

    @GetMapping
    public List<String> getEventHistory() {
        return entityChangeListener.getEventHistory();
    }
}