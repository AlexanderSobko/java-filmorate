package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private static int currentId = 1;

    private final Map<Integer, Film> innerStorage;

    {
        innerStorage = new HashMap<>();
    }

    @Override
    public Film save(Film film) {
        if (film.getId() == null) {
            film.setId(currentId);
            currentId++;
        }
        innerStorage.put(film.getId(), film);
        return film;
    }

    @Override
    public void delete(int id) {
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
    public boolean exists(int id) {
        return innerStorage.containsKey(id);
    }

    @Override
    public Film findById(int id) {
        return innerStorage.get(id);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {

    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {

    }

    @Override
    public Set<Integer> getLikes(int film_id) {
        return null;
    }
}
