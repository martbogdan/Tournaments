package com.task.tournaments.service;

import java.util.List;

public interface EntityService<T> {
    List<T> getAll();

    T getById(Long id);

    T createOrUpdate(T t);

    void deleteById(Long id);
}
