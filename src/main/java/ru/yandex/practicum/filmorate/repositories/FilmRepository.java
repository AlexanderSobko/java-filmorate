package ru.yandex.practicum.filmorate.repositories;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FilmRepository {

    private final Map<Long, Film> filmList = new HashMap<>();


    public Film save(Film film) {
        filmList.put(film.getId(), film);
        return filmList.get(film.getId());
    }

    public boolean exists(Film film) {
        return filmList.containsKey(film.getId());
    }

    public List<Film> findAll() {
        return new ArrayList<>(filmList.values());
    }
}
