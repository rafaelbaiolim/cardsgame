package com.go_to.homework.cardgame.domain.exceptions;

public class DeckOperationException extends RuntimeException {
    public DeckOperationException(String message) {
        super(message);
    }

    public DeckOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}