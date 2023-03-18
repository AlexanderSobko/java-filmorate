package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.services.EntityService;

import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractController<E, S extends EntityService<?>> {

    protected final S service;

    @PostMapping
    public abstract ResponseEntity<E> save(E entity);


    @PutMapping
    public abstract ResponseEntity<E> update(E entity);

    @GetMapping
    public abstract ResponseEntity<List<E>> getAll();

    @GetMapping("/{id}")
    public abstract ResponseEntity<E> getById(Integer id);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest()
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(exception.getBindingResult().getAllErrors());
    }

}
