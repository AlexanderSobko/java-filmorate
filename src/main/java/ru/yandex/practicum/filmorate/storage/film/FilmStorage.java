package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;
import java.util.Set;

public interface FilmStorage extends Storage<Film> {

    void addLike(Integer filmId, Integer userId);

    void deleteLike(Integer filmId, Integer userId);

    Set<Integer> getLikes(int filmId);

     List<Film> getTopFilms(int i);

}
