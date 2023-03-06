package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.NotExistsException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film save(Film film) {
        if (film.getId() != null && filmStorage.exists(film.getId())) {
            throw new AlreadyExistsException("Фильм", film.getId());
        } else {
            if (film.getId() == null) {
                film.setId(Film.currentId);
                Film.currentId++;
            }
            Film savedFilm = filmStorage.save(film);
            log.info("Film with id({}) successfully saved!", savedFilm.getId());
            return savedFilm;
        }
    }

    public String addLike(Long filmId, Long userId) {
        if (filmStorage.exists(filmId)) {
            Film film = filmStorage.findById(filmId);
            film.addLike(userId);
            filmStorage.save(film);
            log.info("Film with id({}) received a like from user with id({})!", filmId, userId);
            return "Вы успешно лайкнули фильм)";
        } else {
            throw new NotExistsException("Фильм", filmId);
        }
    }

    public String deleteLike(Long filmId, Long userId) {
        if (filmStorage.exists(filmId)) {
            Film film = filmStorage.findById(filmId);
            List<Long> list = film.getLikes();
            if (list.contains(userId)) {
                list.remove(userId);
                film.setLikes(new HashSet<>(list));
                filmStorage.save(film);
                log.info("Film with id({}) has lost a like from user with id({})!", filmId, userId);
                return "Вы успешно удалили лайк)";
            } else {
                throw new NotExistsException("Пользователь", userId);
            }
        } else {
            throw new NotExistsException("Фильм", filmId);
        }
    }

    public List<Film> getTopFilms(String count) {
        return filmStorage.findAll().stream()
                .sorted((f,s) -> Integer.compare(s.getLikes().size(), f.getLikes().size()))
                .limit(Integer.parseInt(count))
                .collect(Collectors.toList());
    }

    public Film getFilm(Long id) {
        if (filmStorage.exists(id)) {
            return filmStorage.findById(id);
        } else {
            throw new NotExistsException("Фильм", id);
        }
    }

    public Film updateFilm(Film film) {
        if (filmStorage.exists(film.getId())) {
            Film updatedFilm = filmStorage.save(film);
            log.info("Film with id({}) successfully updated!", updatedFilm.getId());
            return updatedFilm;
        } else {
            throw new NotExistsException("Фильм", film.getId());
        }
    }

    public List<Film> getAllFilms() {
        return filmStorage.findAll();
    }
}
