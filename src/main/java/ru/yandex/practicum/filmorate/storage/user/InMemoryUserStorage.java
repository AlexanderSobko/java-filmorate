package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

    private static Integer currentId = 1;

    private final Map<Integer, User> innerStorage;

    {
        innerStorage = new HashMap<>();
    }
    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(currentId);
            currentId++;
        }
        innerStorage.put(user.getId(), user);
        return innerStorage.get(user.getId());
    }

    @Override
    public void delete(int id) {
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
    public boolean exists(int id) {
        return innerStorage.containsKey(id);
    }

    @Override
    public User findById(int id) {
        return innerStorage.get(id);
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
        User user = findById(id);
        user.addFriend(friendId);
    }

    @Override
    public void deleteFriend(Integer id, Integer friendId) {
        User user = findById(id);
        user.deleteFriend(friendId);
    }

    @Override
    public Set<Integer> getFriends(int id) {
        return new HashSet<>(innerStorage.get(id).getFriends());
    }
}
