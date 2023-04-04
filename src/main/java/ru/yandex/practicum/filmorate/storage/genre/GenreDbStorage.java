package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.Genre;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAll() {
        String sql = "SELECT * FROM genres";
        return jdbcTemplate.query(sql, new GanreRowMapper());
    }

    @Override
    public boolean exists(int id) {
        String query = "SELECT EXISTS(SELECT * FROM genres WHERE genre_id = ?)";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, Boolean.class);
    }

    @Override
    public Genre findById(int id) {
        String sql = "SELECT * FROM genres WHERE genre_id = ?;";
        return jdbcTemplate.queryForObject(sql, new GanreRowMapper(), id);
    }
}
