package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    public Film save(Film film) {
        if (film.getId() != null && filmStorage.exists(film.getId())) {
            throw new AlreadyExistsException("Фильм", film.getId());
        } else {
            if (film.getId() == null) {
                film.setId(filmStorage.getCurrentId());
                filmStorage.incrementCurrentId();
            }
            Film savedFilm = filmStorage.save(film);
            log.info("Film with id({}) successfully saved!", savedFilm.getId());
            return savedFilm;
        }
    }

    public String addLike(Long filmId, Long userId) {
        validateNotExists(filmId);
        Film film = filmStorage.findById(filmId);
        film.addLike(userId);
        filmStorage.save(film);
        log.info("Film with id({}) received a like from user with id({})!", filmId, userId);
        return "Вы успешно лайкнули фильм)";
    }

    public String deleteLike(Long filmId, Long userId) {
        validateNotExists(filmId);
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
    }

    public List<Film> getTopFilms(String count) {
        return filmStorage.findAll().stream()
                .sorted((f, s) -> Integer.compare(s.getLikes().size(), f.getLikes().size()))
                .limit(Integer.parseInt(count))
                .collect(Collectors.toList());
    }

    public Film getFilm(Long id) {
        validateNotExists(id);
        return filmStorage.findById(id);
    }

    public Film updateFilm(Film film) {
        validateNotExists(film.getId());
        Film updatedFilm = filmStorage.save(film);
        log.info("Film with id({}) successfully updated!", updatedFilm.getId());
        return updatedFilm;
    }

    public List<Film> getAllFilms() {
        return filmStorage.findAll();
    }

    private void validateNotExists(long id) {
        if (!filmStorage.exists(id)) {
            throw new NotExistsException("Фильм", id);
        }
    }
}
