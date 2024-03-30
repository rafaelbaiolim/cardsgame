package com.go_to.homework.cardgame.services;

import com.go_to.homework.cardgame.domain.models.Game;
import com.go_to.homework.cardgame.domain.repositories.DataSourceGameRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GameService implements AppService<Game> {

    private final DataSourceGameRepository gameRepository;

    public GameService(DataSourceGameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Game save(String name) {
        Game game = new Game(name);
        return gameRepository.save(game);
    }

    @Override
    public void delete(UUID uuid) {
        gameRepository.delete(uuid);
    }

    @Override
    public List<Game> listAll() {
        return gameRepository.listAll();
    }

    @Override
    public Optional<Game> find(UUID uuid) {
        return gameRepository.find(uuid);
    }

    @Override
    public boolean exists(UUID uuid) {
        return gameRepository.find(uuid).isPresent();
    }
}
