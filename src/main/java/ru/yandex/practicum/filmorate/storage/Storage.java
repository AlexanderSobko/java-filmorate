package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface Storage<T> {

    T save(T entity);

    void delete(int id);

    T update(T entity);

    List<T> findAll();

    boolean exists(int id);

    T findById(int id);


}
