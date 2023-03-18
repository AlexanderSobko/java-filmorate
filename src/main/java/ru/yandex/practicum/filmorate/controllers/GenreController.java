package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.NotExistsException;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.services.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController extends AbstractController<Genre, GenreService> {

    @Autowired
    public GenreController(GenreService service) {
        super(service);
    }

    @Override
    public ResponseEntity<Genre> save(Genre entity) {
        throw new NotExistsException("На данный момент эта функция не доступна!");
    }

    @Override
    public ResponseEntity<Genre> update(Genre entity) {
        throw new NotExistsException("На данный момент эта функция не доступна!");
    }

    @Override
    public ResponseEntity<List<Genre>> getAll() {
        return ResponseEntity.ok().body(service.getAll());
    }

    @Override
    public ResponseEntity<Genre> getById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.get(id));
    }
}
