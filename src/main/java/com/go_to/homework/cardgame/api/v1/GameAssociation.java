package com.go_to.homework.cardgame.api.v1;

import com.go_to.homework.cardgame.domain.entity.Game;

@FunctionalInterface
public interface GameAssociation<T> {
    void associate(Game game, T entity);
}