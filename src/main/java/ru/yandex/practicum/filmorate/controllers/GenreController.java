package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotExistsException;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.services.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {

    private final GenreService service;

    @PostMapping
    public ResponseEntity<Genre> save(Genre entity) {
        throw new NotExistsException("На данный момент эта функция не доступна!");
    }

    @PutMapping
    public ResponseEntity<Genre> update(Genre entity) {
        throw new NotExistsException("На данный момент эта функция не доступна!");
    }

    @GetMapping
    public ResponseEntity<List<Genre>> getAll() {
        return ResponseEntity.ok().body(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.get(id));
    }
}
