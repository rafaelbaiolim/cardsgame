package com.go_to.homework.cardgame.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppService<T> {
    T save(String name);

    T save();

    void delete(UUID uuid);

    List<T> listAll();

    Optional<T> find(UUID uuid);

    boolean exists(UUID uuid);
}
