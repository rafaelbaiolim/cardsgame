package com.go_to.homework.cardgame.domain.exceptions;

public class NoMoreCardsException extends RuntimeException {
    public NoMoreCardsException(String message) {
        super(message);
    }
}