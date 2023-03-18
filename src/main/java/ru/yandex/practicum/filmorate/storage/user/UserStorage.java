package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Set;

public interface UserStorage extends Storage<User> {
    void addFriend(Integer id, Integer friendId);

    void deleteFriend(Integer id, Integer friendId);

    Set<Integer> getFriends(int id);
}
