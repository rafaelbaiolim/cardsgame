package com.go_to.homework.cardgame.domain.events.listener;


import com.go_to.homework.cardgame.domain.events.EntityChangeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class EntityChangeListener {

    private final List<String> eventHistory = new ArrayList<>();

    @EventListener
    public void onEntityChangeEvent(EntityChangeEvent event) {
        eventHistory.add(event.getMessage());
    }

    private List<String> getEventHistoryReversed() {
        List<String> reversedHistory = new ArrayList<>(eventHistory);
        Collections.reverse(reversedHistory);
        return reversedHistory;
    }

    public List<String> getEventHistory() {
        return getEventHistoryReversed();
    }
}