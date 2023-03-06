package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    Map<Long, User> innerStorage;

    {
        innerStorage = new HashMap<>();
    }
    @Override
    public User save(User user) {
        innerStorage.put(user.getId(), user);
        return innerStorage.get(user.getId());
    }

    @Override
    public void delete(long id) {
        innerStorage.remove(id);
    }

    @Override
    public User update(User entity) {
        return innerStorage.put(entity.getId(), entity);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(innerStorage.values());
    }

    @Override
    public boolean exists(long id) {
        return innerStorage.containsKey(id);
    }

    @Override
    public User findById(long id) {
        return innerStorage.get(id);
    }
}
