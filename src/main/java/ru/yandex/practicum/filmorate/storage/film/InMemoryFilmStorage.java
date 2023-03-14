package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private static Long currentId = 1L;

    private final Map<Long, Film> innerStorage;

    {
        innerStorage = new HashMap<>();
    }

    @Override
    public Film save(Film film) {
        innerStorage.put(film.getId(), film);
        return film;
    }

    @Override
    public void delete(long id) {
        innerStorage.remove(id);
    }

    @Override
    public Film update(Film film) {
        innerStorage.put(film.getId(), film);
        return innerStorage.get(film.getId());
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(innerStorage.values());
    }

    @Override
    public boolean exists(long id) {
        return innerStorage.containsKey(id);
    }

    @Override
    public Film findById(long id) {
        return innerStorage.get(id);
    }

    @Override
    public long getCurrentId() {
        return currentId;
    }

    @Override
    public void incrementCurrentId() {
        currentId++;
    }


}
