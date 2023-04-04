package ru.yandex.practicum.filmorate.services;

import java.util.List;

public interface EntityService<T> {

    T save(T entity);

    T get(Integer id);

    T update(T entity);

    List<T> getAll();
}
