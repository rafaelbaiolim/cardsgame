package com.go_to.homework.cardgame.domain.models;

@FunctionalInterface
public interface GameAssociation<T> {
    void associate(Game game, T entity);
}