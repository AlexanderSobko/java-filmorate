package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User save(User user) {
        if (user.getId() != null && userStorage.exists(user.getId())) {
            throw new AlreadyExistsException("Пользователь", user.getId());
        } else {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            if (user.getId() == null) {
                user.setId(userStorage.getCurrentId());
                userStorage.incrementCurrentId();
            }
            User savedUser = userStorage.save(user);
            log.info("User with id({}) successfully saved!", savedUser.getId());
            return savedUser;
        }
    }

    public User updateUser(User user) {
        validateNotExists(user.getId());
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        User updatedUser = userStorage.save(user);
        log.info("User with id({}) successfully updated!", updatedUser.getId());
        return updatedUser;

    }

    public String addFriend(Long id, Long friendId) {
        validateNotExists(id);
        validateNotExists(friendId);
        User user = userStorage.findById(id);
        User friend = userStorage.findById(friendId);
        user.addFriend(friendId);
        friend.addFriend(id);
        log.info("User with id({}) and user with id({}) successfully became friends!", id, friendId);
        return String.format("Пользователи с id(%d) и id(%d) стали друзьями!", id, friendId);

    }

    public String deleteFriend(Long id, Long friendId) {
        validateNotExists(id);
        validateNotExists(friendId);
        User user = userStorage.findById(id);
        User friend = userStorage.findById(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(id);
        log.info("User with id({}) and user with id({}) have lost their friendship!", id, friendId);
        return String.format("Пользователи с id(%d) и id(%d) перестали быть друзьями!", id, friendId);

    }

    public List<User> getCommonFriends(Long id, Long friendId) {
        validateNotExists(id);
        validateNotExists(friendId);
        User user = userStorage.findById(id);
        User friend = userStorage.findById(friendId);
        List<Long> commonFriends = new ArrayList<>(user.getFriends());
        commonFriends.retainAll(friend.getFriends());
        return commonFriends.stream()
                .map(userStorage::findById)
                .collect(Collectors.toList());

    }

    public List<User> getAllUsers() {
        return userStorage.findAll();
    }

    public List<User> getFriendList(Long id) {
        validateNotExists(id);
        return userStorage.findById(id).getFriends().stream().map(userStorage::findById).collect(Collectors.toList());
    }

    public User getUser(Long id) {
        validateNotExists(id);
        return userStorage.findById(id);
    }

    private void validateNotExists(long id) {
        if (!userStorage.exists(id)) {
            throw new NotExistsException("Пользователь", id);
        }
    }

}
