package com.go_to.homework.cardgame.domain.repositories;

import com.go_to.homework.cardgame.domain.models.Game;

public interface GameRepository extends Repository<Game> {

    Game update(Game game);
}