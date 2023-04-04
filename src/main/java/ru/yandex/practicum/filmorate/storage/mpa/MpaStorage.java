package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.models.Mpa;

import java.util.List;

public interface MpaStorage {

    List<Mpa> findAll();

    boolean exists(int id);

    Mpa findById(int id);

}
