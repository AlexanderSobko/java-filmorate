package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<User> save(@Valid @RequestBody User user) {
        return ResponseEntity.status(201).body(service.save(user));
    }

    @PutMapping
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        return ResponseEntity.ok().body(service.update(user));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok().body(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.get(id));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        return ResponseEntity.ok().body(service.addFriend(id, friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        return ResponseEntity.ok().body(service.deleteFriend(id, friendId));
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriendList(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.getFriendList(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return ResponseEntity.ok().body(service.getCommonFriends(id, otherId));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest()
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(exception.getBindingResult().getAllErrors());
    }
}
