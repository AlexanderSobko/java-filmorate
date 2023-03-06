package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.NotExistsException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User save(User user) {
        if (user.getId() != null && userStorage.exists(user.getId())) {
            throw new AlreadyExistsException("Пользователь", user.getId());
        } else {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            if (user.getId() == null) {
                user.setId(User.currentId);
                User.currentId++;
            }
            User savedUser = userStorage.save(user);
            log.info("User with id({}) successfully saved!", savedUser.getId());
            return savedUser;
        }
    }

    public User updateUser(User user) {
        if (userStorage.exists(user.getId())) {
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            User updatedUser = userStorage.save(user);
            log.info("User with id({}) successfully updated!", updatedUser.getId());
            return updatedUser;
        } else {
            throw new NotExistsException("Пользователь", user.getId());
        }
    }

    public String addFriend(Long id, Long friendId) {
        if (userStorage.exists(id) && userStorage.exists(friendId)) {
            User user = userStorage.findById(id);
            User friend = userStorage.findById(friendId);
            user.addFriend(friendId);
            friend.addFriend(id);
            log.info("User with id({}) and user with id({}) successfully became friends!", id, friendId);
            return "Пользовател";
        } else {
            throw new NotExistsException("Пользователь", id);
        }
    }

    public String deleteFriend(Long id, Long friendId) {
        if (userStorage.exists(id) && userStorage.exists(friendId)) {
            User user = userStorage.findById(id);
            User friend = userStorage.findById(friendId);
            user.deleteFriend(friendId);
            friend.deleteFriend(id);
            log.info("User with id({}) and user with id({}) have lost their friendship!", id, friendId);
            return "Пользовател";
        } else {
            throw new NotExistsException("Пользователь", id);
        }
    }

    public List<User> getCommonFriends(Long id, Long friendId) {
        if (userStorage.exists(id) && userStorage.exists(friendId)) {
            User user = userStorage.findById(id);
            User friend = userStorage.findById(friendId);
            List<Long> commonFriends = new ArrayList<>(user.getFriends());
            commonFriends.retainAll(friend.getFriends());
            return commonFriends.stream()
                    .map(userStorage::findById)
                    .collect(Collectors.toList());
        } else {
            throw new NotExistsException("Пользователь", id);
        }
    }

    public List<User> getAllUsers() {
        return userStorage.findAll();
    }

    public List<User> getFriendList(Long id) {
        if (userStorage.exists(id)) {
            return userStorage.findById(id).getFriends().stream().map(userStorage::findById).collect(Collectors.toList());
        } else {
            throw new NotExistsException("Пользователь", id);
        }
    }

    public User getUser(Long id) {
        if (userStorage.exists(id)) {
            return userStorage.findById(id);
        } else {
            throw new NotExistsException("Пользователь", id);
        }
    }
}
