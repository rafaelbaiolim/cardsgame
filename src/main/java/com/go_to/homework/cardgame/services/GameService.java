package com.go_to.homework.cardgame.services;

import com.go_to.homework.cardgame.domain.entity.Game;
import com.go_to.homework.cardgame.domain.events.EntityChangeEvent;
import com.go_to.homework.cardgame.domain.repositories.DataSourceGameRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GameService implements AppService<Game> {

    private final DataSourceGameRepository gameRepository;
    private final ApplicationEventPublisher eventPublisher;

    public GameService(DataSourceGameRepository gameRepository, ApplicationEventPublisher eventPublisher) {

        this.gameRepository = gameRepository;
        this.eventPublisher = eventPublisher;
    }


    @Override
    public Game save(String name) {
        Game createdGame = gameRepository.save(new Game(name));
        eventPublisher.publishEvent(new EntityChangeEvent(createdGame, "New game created"));
        return createdGame;
    }

    public Game update(Game game) {
        Game updatedGame = gameRepository.update(game);
        eventPublisher.publishEvent(new EntityChangeEvent(updatedGame, "Game updated"));
        return updatedGame;
    }

    @Override
    public Game save() {
        return null;
    }

    @Override
    public void delete(UUID uuid) {
        gameRepository.delete(uuid);
        eventPublisher.publishEvent(new EntityChangeEvent(uuid, "Game deleted"));
    }

    @Override
    public List<Game> listAll() {
        eventPublisher.publishEvent(new EntityChangeEvent(null, "Listed All Games"));
        return gameRepository.listAll();
    }

    @Override
    public Optional<Game> find(UUID uuid) {
        eventPublisher.publishEvent(new EntityChangeEvent(uuid, "Game was Searched"));
        return gameRepository.find(uuid);
    }

    @Override
    public boolean exists(UUID uuid) {
        return gameRepository.find(uuid).isPresent();
    }
}
