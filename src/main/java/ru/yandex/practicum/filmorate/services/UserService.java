package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class UserService implements EntityService<User> {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User save(User user) {
        if (user.getId() != null && userStorage.exists(user.getId())) {
            throw new AlreadyExistsException("Пользователь", user.getId());
        } else {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            User savedUser = userStorage.save(user);
            log.info("User with id({}) successfully saved!", savedUser.getId());
            return savedUser;
        }
    }

    @Override
    public User update(User user) {
        validateNotExists(user.getId());
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        User updatedUser = userStorage.update(user);
        log.info("User with id({}) successfully updated!", updatedUser.getId());
        return updatedUser;

    }

    public String addFriend(Integer id, Integer friendId) {
        validateNotExists(id);
        validateNotExists(friendId);
        userStorage.addFriend(id, friendId);
        log.info("User with id({}) has become friends with another user id({})!", id, friendId);
        return String.format("Пользователи с id(%d) стал другом для пользователя с id(%d)!", id, friendId);
    }

    public String deleteFriend(Integer id, Integer friendId) {
        validateNotExists(id);
        validateNotExists(friendId);
        userStorage.deleteFriend(id, friendId);
        log.info("User with id({}) have lost his friendship with user id({}) !", id, friendId);
        return String.format("Пользователь с id(%d) разорвал дружбу с пользователем id(%d)!", id, friendId);
    }

    public List<User> getCommonFriends(Integer id, Integer friendId) {
        validateNotExists(id);
        validateNotExists(friendId);
        List<Integer> commonFriends = new ArrayList<>(userStorage.getFriends(id));
        commonFriends.retainAll(userStorage.getFriends(friendId));
        return commonFriends.stream()
                .map(userStorage::findById)
                .collect(Collectors.toList());

    }

    @Override
    public List<User> getAll() {
        return userStorage.findAll();
    }

    public List<User> getFriendList(Integer id) {
        validateNotExists(id);
        return userStorage.getFriends(id).stream()
                .map(this::get)
                .collect(Collectors.toList());
    }

    @Override
    public User get(Integer id) {
        validateNotExists(id);
        return userStorage.findById(id);
    }

    private void validateNotExists(int id) {
        if (!userStorage.exists(id)) {
            throw new NotExistsException("Пользователь", id);
        }
    }

}
