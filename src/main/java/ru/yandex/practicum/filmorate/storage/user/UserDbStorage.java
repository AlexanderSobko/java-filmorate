package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    @Override
    public User save(User entity) {
        final String sql = String.format("INSERT INTO users (email,login,name,birthday) VALUES ('%s','%s','%s','%s')",
                entity.getEmail(), entity.getLogin(), entity.getName(), entity.getBirthday());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> connection.prepareStatement(sql, new String[] {"user_id"}), keyHolder);
        return findById(keyHolder.getKey().intValue());
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public User update(User entity) {
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
        jdbcTemplate.update(sql,
                entity.getEmail(),
                entity.getLogin(),
                entity.getName(),
                entity.getBirthday(),
                entity.getId());
        return findById(entity.getId());
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users;";
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper());
        users.forEach(u -> u.setFriends(getFriends(u.getId())));
        return users;
    }

    @Override
    public boolean exists(int id) {
        String query = "SELECT EXISTS(SELECT * FROM users WHERE user_id = ?)";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, Boolean.class);
    }

    @Override
    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE user_id = ?;";
        User user =  jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
        user.setFriends(getFriends(id));
        return user;
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
        String sql = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?);";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public void deleteFriend(Integer id, Integer friendId) {
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?;";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public Set<Integer> getFriends(int id) {
        String sql = "SELECT friend_id FROM friends WHERE user_id = ?;";
        return new HashSet<>(jdbcTemplate.queryForList(sql, Integer.class, id));
    }
}
