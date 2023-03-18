package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.NotExistsException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService implements EntityService<Film> {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }


    @Override
    public Film save(Film film) {
        if (film.getId() != null && filmStorage.exists(film.getId())) {
            throw new AlreadyExistsException("Фильм", film.getId());
        } else {
            Film savedFilm = filmStorage.save(film);
            log.info("Film with id({}) successfully saved!", savedFilm.getId());
            return savedFilm;
        }
    }

    public String addLike(Integer filmId, Integer userId) {
        validateNotExists(filmId);
        filmStorage.addLike(filmId, userId);
        log.info("Film with id({}) received a like from user with id({})!", filmId, userId);
        return "Вы успешно лайкнули фильм)";
    }

    public String deleteLike(Integer filmId, Integer userId) {
        validateNotExists(filmId);
        filmStorage.deleteLike(filmId, userId);
        log.info("Film with id({}) has lost a like from user with id({})!", filmId, userId);
        return "Вы успешно удалили лайк)";
    }

    public List<Film> getTopFilms(String count) {
        return filmStorage.findAll().stream()
                .sorted((f, s) -> Integer.compare(s.getLikes().size(), f.getLikes().size()))
                .limit(Integer.parseInt(count))
                .collect(Collectors.toList());
    }

    @Override
    public Film get(Integer id) {
        validateNotExists(id);
        return filmStorage.findById(id);
    }

    @Override
    public Film update(Film film) {
        validateNotExists(film.getId());
        Film updatedFilm = filmStorage.update(film);
        log.info("Film with id({}) successfully updated!", updatedFilm.getId());
        return updatedFilm;
    }

    @Override
    public List<Film> getAll() {
        return filmStorage.findAll();
    }

    private void validateNotExists(int id) {
        if (!filmStorage.exists(id)) {
            throw new NotExistsException("Фильм", id);
        }
    }
}
