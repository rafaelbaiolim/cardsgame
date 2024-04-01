package com.go_to.homework.cardgame.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Repository<T> {
    T save(T model);

    void delete(UUID uuid);

    List<T> listAll();

    Optional<T> find(UUID uuid);

    void deleteAll();
}
