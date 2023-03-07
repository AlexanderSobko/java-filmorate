package ru.yandex.practicum.filmorate.controllers;


import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.services.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public ResponseEntity<Film> saveFilm(@Valid @RequestBody Film film) {
        return ResponseEntity.status(201).body(filmService.save(film));
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        return ResponseEntity.ok().body(filmService.updateFilm(film));
    }

    @GetMapping
    public ResponseEntity<List<Film>> getFilms() {
        return ResponseEntity.ok().body(filmService.getAllFilms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable Long id) {
        return ResponseEntity.ok().body(filmService.getFilm(id));
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<String> addLike(@PathVariable Long id, @PathVariable Long userId) {
        return ResponseEntity.ok().body(filmService.addLike(id, userId));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<String> deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        return ResponseEntity.ok().body(filmService.deleteLike(id, userId));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getTopFilms(@RequestParam(required = false, defaultValue = "10") String count) {
        return ResponseEntity.ok().body(filmService.getTopFilms(count));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest()
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(exception.getBindingResult().getAllErrors());
    }

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }
}
