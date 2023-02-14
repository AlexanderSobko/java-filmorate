package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.services.FilmService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/films/")
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public ResponseEntity<Film> saveFilm(@Valid @RequestBody Film film) {
        return ResponseEntity.ok().body(filmService.save(film));
    }

    @PatchMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        return ResponseEntity.ok().body(filmService.updateFilm(film));
    }

    @GetMapping
    public ResponseEntity<List<Film>> getFilms() {
        return ResponseEntity.ok().body(filmService.getAllFilms());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseStatusException handleValidationException(MethodArgumentNotValidException exception) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }
}
