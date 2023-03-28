package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotExistsException;
import ru.yandex.practicum.filmorate.models.Mpa;
import ru.yandex.practicum.filmorate.services.MpaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService service;

    @PostMapping
    public ResponseEntity<Mpa> save(Mpa entity) {
        throw new NotExistsException("На данный момент эта функция не доступна!");
    }

    @PutMapping
    public ResponseEntity<Mpa> update(Mpa entity) {
        throw new NotExistsException("На данный момент эта функция не доступна!");
    }

    @GetMapping
    public ResponseEntity<List<Mpa>> getAll() {
        return ResponseEntity.ok().body(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mpa> getById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.get(id));
    }
}
