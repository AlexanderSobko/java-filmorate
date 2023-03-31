package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.User;

import java.util.List;

@Component
@RequiredArgsConstructor
@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User save(User entity) {
        final String sql = String.format("INSERT INTO users (email,login,name,birthday) VALUES ('%s','%s','%s','%s'); ",
                entity.getEmail(), entity.getLogin(), entity.getName(),
                entity.getBirthday());
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
        String sql = "SELECT users.*,GROUP_CONCAT(friends.friend_id) AS friend_list " +
                "FROM friends " +
                "RIGHT JOIN users ON friends.user_id = users.user_id " +
                "GROUP BY users.user_id;";
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper());
        return users;
    }

    @Override
    public boolean exists(int id) {
        String query = "SELECT EXISTS(SELECT * FROM users WHERE user_id = ?)";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, Boolean.class);
    }

    @Override
    public User findById(int id) {
        String sql = "SELECT users.*, GROUP_CONCAT(friends.friend_id) AS friend_list " +
                "FROM friends " +
                "RIGHT JOIN users ON friends.user_id = users.user_id " +
                "WHERE users.user_id = ? " +
                "GROUP BY friends.user_id;";
        User user =  jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
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
    public List<User> getFriends(int id) {
        String sql = "SELECT users.*, GROUP_CONCAT(f.friend_id) AS friend_list " +
                "FROM friends " +
                "RIGHT JOIN users ON friends.friend_id= users.user_id " +
                "LEFT JOIN friends AS f ON f.user_id = users.user_id " +
                "WHERE friends.user_id = ? " +
                "GROUP BY f.user_id " +
                "ORDER BY f.user_id DESC;";
        return jdbcTemplate.query(sql, new UserRowMapper(), id);
    }

    @Override
    public List<User> getCommonFriends(int id, int friendId) {
        String sql = "SELECT users.*, NULL AS friend_list from friends AS f1 " +
                "JOIN friends AS f2 ON f1.friend_id= f2.friend_id " +
                "JOIN users ON users.user_id = f1.friend_id " +
                "WHERE  (f1.user_id = ? AND f2.user_id = ?);";
        return jdbcTemplate.query(sql, new UserRowMapper(), id, friendId);
    }
}
