package com.go_to.homework.cardgame.domain.repositories;

import com.go_to.homework.cardgame.domain.entity.Game;

public interface GameRepository extends Repository<Game> {
    Game update(Game game);
}