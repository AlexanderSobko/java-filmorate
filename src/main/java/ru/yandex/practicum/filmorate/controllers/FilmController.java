package ru.yandex.practicum.filmorate.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.services.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController extends AbstractController<Film, FilmService> {

    @Autowired
    public FilmController(FilmService service) {
        super(service);
    }

    @Override
    @PostMapping
    public ResponseEntity<Film> save(@Valid @RequestBody Film film) {
        return ResponseEntity.status(201).body(service.save(film));
    }

    @Override
    @PutMapping
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        return ResponseEntity.ok().body(service.update(film));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<Film>> getAll() {
        return ResponseEntity.ok().body(service.getAll());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Film> getById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.get(id));
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<String> addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return ResponseEntity.ok().body(service.addLike(id, userId));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<String> deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return ResponseEntity.ok().body(service.deleteLike(id, userId));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getTopFilms(@RequestParam(required = false, defaultValue = "10") String count) {
        return ResponseEntity.ok().body(service.getTopFilms(count));
    }

}
