package com.go_to.homework.cardgame.domain.events;

import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EntityChangeEvent extends ApplicationEvent {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String message;
    private String timestamp;

    public EntityChangeEvent(Object source, String message) {
        super(source);
        this.message = message;
        this.timestamp = LocalDateTime.now().format(FORMATTER);
    }

    public String getMessage() {
        return String.format("[%s] %s - %s", timestamp, message, source.toString());
    }

}
