package ru.yandex.practicum.filmorate.controllers;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user) {
        return ResponseEntity.status(201).body(userService.save(user));
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        return ResponseEntity.ok().body(userService.updateUser(user));
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.getUser(id));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return ResponseEntity.ok().body(userService.addFriend(id, friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return ResponseEntity.ok().body(userService.deleteFriend(id, friendId));
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriendList(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.getFriendList(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return ResponseEntity.ok().body(userService.getCommonFriends(id, otherId));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest()
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(exception.getBindingResult().getAllErrors());
    }

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
}
