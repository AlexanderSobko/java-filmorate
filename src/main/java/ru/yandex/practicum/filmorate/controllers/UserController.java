package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user) {
        return ResponseEntity.status(201).body(userService.save(user));
    }

    @PatchMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        return ResponseEntity.ok().body(userService.updateUser(user));
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<String> handleValidationException(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest()
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(exception.getMessage());
    }

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
}
