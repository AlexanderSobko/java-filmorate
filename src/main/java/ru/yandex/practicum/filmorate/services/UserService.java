package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.NotExistsException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.repositories.UserRepository;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        if (userRepository.exists(user)) {
            throw new AlreadyExistsException("user", user.getId());
        } else {
            User savedUser = userRepository.save(user);
            log.info("User with id({}) successfully saved!",savedUser.getId());
            return savedUser;
        }
    }

    public User updateUser(User user) {
        if (userRepository.exists(user)) {
            User updatedUser = userRepository.save(user);
            log.info("User with id({}) successfully updated!", updatedUser.getId());
            return updatedUser;
        } else {
            throw new NotExistsException("user", user.getId());
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
