package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface Storage<T> {

    T save(T entity);
    void delete(long id);
    T update(T entity);
    List<T> findAll();
    boolean exists(long id);
    T findById(long id);

}
