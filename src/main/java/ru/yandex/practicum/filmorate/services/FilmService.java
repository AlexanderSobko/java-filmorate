package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.NotExistsException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.repositories.FilmRepository;

import java.util.List;

@Slf4j
@Service
public class FilmService {

    private final FilmRepository filmRepository;

    @Autowired
    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public Film save(Film film) {
        if (filmRepository.exists(film)) {
            throw new AlreadyExistsException("Фильм", film.getId());
        } else {
            Film savedFilm = filmRepository.save(film);
            log.info("Film with id({}) successfully saved!", savedFilm.getId());
            return savedFilm;
        }

    }

    public Film updateFilm(Film film) {
        if (filmRepository.exists(film)) {
            Film updatedFilm = filmRepository.save(film);
            log.info("Film with id({}) successfully updated!", updatedFilm.getId());
            return updatedFilm;
        } else {
            throw new NotExistsException("Фильм", film.getId());
        }
    }

    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }
}
