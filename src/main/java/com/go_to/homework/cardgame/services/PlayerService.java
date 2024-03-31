package com.go_to.homework.cardgame.services;

import com.go_to.homework.cardgame.domain.models.Player;
import com.go_to.homework.cardgame.domain.repositories.DataSourcePlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerService implements AppService<Player> {
    private final DataSourcePlayerRepository playerRepository;

    public PlayerService(DataSourcePlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Player save(String name) {
        return playerRepository.save(new Player(name));
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
        }
    }

    @Override
    public List<Player> listAll() {
        return playerRepository.listAll();
    }

    @Override
    public Optional<Player> find(UUID uuid) {
        return playerRepository.find(uuid);
    }

    @Override
    public boolean exists(UUID uuid) {
        return playerRepository.find(uuid).isPresent();
    }
}
