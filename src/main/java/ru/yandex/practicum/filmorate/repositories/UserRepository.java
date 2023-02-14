package ru.yandex.practicum.filmorate.repositories;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserRepository {

    private final Map<Long, User> userList = new HashMap<>();

    public User save(User user) {
        userList.put(user.getId(), user);
        return userList.get(user.getId());
    }

    public boolean exists(User user) {
        return userList.containsKey(user.getId());
    }

    public List<User> findAll() {
        return new ArrayList<>(userList.values());
    }
}
