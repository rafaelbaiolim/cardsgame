package com.go_to.homework.cardgame.services;

import com.go_to.homework.cardgame.domain.entity.Player;
import com.go_to.homework.cardgame.domain.events.EntityChangeEvent;
import com.go_to.homework.cardgame.domain.repositories.DataSourcePlayerRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerService implements AppService<Player> {
    private final DataSourcePlayerRepository playerRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PlayerService(DataSourcePlayerRepository playerRepository, ApplicationEventPublisher eventPublisher) {
        this.playerRepository = playerRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Player save(String name) {
        Player player = playerRepository.save(new Player(name));
        eventPublisher.publishEvent(new EntityChangeEvent(player, "New player created"));
        return player;
    }

    @Override
    public Player save() {
        return null;
    }

    @Override
    public void delete(UUID uuid) {
        Optional<Player> playerOptional = playerRepository.find(uuid);
        if (playerOptional.isPresent()) {
            playerRepository.delete(uuid);
            eventPublisher.publishEvent(new EntityChangeEvent(uuid, "Player was deleted"));
        }
    }

    @Override
    public List<Player> listAll() {
        eventPublisher.publishEvent(new EntityChangeEvent("", "Listed All Players"));
        return playerRepository.listAll();
    }

    @Override
    public Optional<Player> find(UUID uuid) {
        eventPublisher.publishEvent(new EntityChangeEvent(uuid, "Player was Searched"));
        return playerRepository.find(uuid);
    }

    @Override
    public boolean exists(UUID uuid) {
        return playerRepository.find(uuid).isPresent();
    }
}
