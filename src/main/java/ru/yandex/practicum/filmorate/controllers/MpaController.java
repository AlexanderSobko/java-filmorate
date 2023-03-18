package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.NotExistsException;
import ru.yandex.practicum.filmorate.models.Mpa;
import ru.yandex.practicum.filmorate.services.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController extends AbstractController<Mpa, MpaService>{

    @Autowired
    public MpaController(MpaService service) {
        super(service);
    }

    @Override
    public ResponseEntity<Mpa> save(Mpa entity) {
        throw new NotExistsException("На данный момент эта функция не доступна!");
    }

    @Override
    public ResponseEntity<Mpa> update(Mpa entity) {
        throw new NotExistsException("На данный момент эта функция не доступна!");
    }

    @Override
    public ResponseEntity<List<Mpa>> getAll() {
        return ResponseEntity.ok().body(service.getAll());
    }

    @Override
    public ResponseEntity<Mpa> getById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.get(id));
    }
}
