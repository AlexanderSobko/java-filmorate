package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotExistsException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film save(Film entity) {
        final String sql = String.format("INSERT INTO films (name,description,duration,release_date,mpa_id)" +
                " VALUES ('%s','%s','%s','%s',%d);",
                entity.getName(), entity.getDescription(), entity.getDuration(),
                entity.getReleaseDate().toString(), entity.getMpa().getId());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> connection.prepareStatement(sql, new String[]{"film_id"}), keyHolder);
        int id = keyHolder.getKey().intValue();
        if (entity.getGenres() != null) {
            updateAllGenres(entity.getGenres(), id);
        }
        return findById(id);
    }

    private void updateAllGenres(Set<Genre> list, Integer film_id) {
        List<Genre> genres = new ArrayList<>(list);
        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?;", film_id);
        jdbcTemplate.batchUpdate("INSERT INTO film_genres (film_id, genre_id) VALUES(?,?);",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Genre genre = genres.get(i);
                        ps.setInt(1, film_id);
                        ps.setInt(2, genre.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return genres.size();
                    }
                });
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM films WHERE film_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Film update(Film entity) {
        String sql = "UPDATE films SET name = ?, description = ?, duration = ?, release_date = ?, mpa_id = ?" +
                "WHERE film_id = ?";
        Film film = findById(entity.getId());
        if (entity.getGenres() != null && film.getGenres() != null
                && (!film.getGenres().containsAll(entity.getGenres())
                || !entity.getGenres().containsAll(film.getGenres()))) {
            updateAllGenres(entity.getGenres(), entity.getId());
        }
        jdbcTemplate.update(sql, entity.getName(),
                entity.getDescription(), entity.getDuration(),
                entity.getReleaseDate().toString(),
                entity.getMpa().getId(),
                entity.getId());
        return findById(entity.getId());
    }

    @Override
    public List<Film> findAll() {
        String sql = "SELECT f.*,m.name AS mpa_name, GROUP_CONCAT(fl.user_id) AS likes, " +
                "GROUP_CONCAT(CONCAT(g.genre_id,'@',g.name) ORDER BY g.genre_id) AS genres " +
                "FROM films AS f " +
                "JOIN mpas AS m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_likes AS fl ON fl.film_id = f.film_id " +
                "LEFT JOIN film_genres AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id " +
                "GROUP BY f.film_id;";
        return jdbcTemplate.query(sql, new FilmRowMapper());
    }

    @Override
    public boolean exists(int id) {
        String query = "SELECT EXISTS(SELECT * FROM films WHERE film_id = ?)";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, Boolean.class);
    }

    @Override
    public Film findById(int id) {
        String sql = "SELECT f.*,m.name AS mpa_name, GROUP_CONCAT(fl.user_id) AS likes," +
                "GROUP_CONCAT(CONCAT(g.genre_id,'@',g.name) ORDER BY g.genre_id) AS genres " +
                "FROM films AS f " +
                "JOIN mpas AS m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_likes AS fl ON fl.film_id = f.film_id " +
                "LEFT JOIN film_genres AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id " +
                "WHERE f.film_id = ? " +
                "GROUP BY f.film_id;";
        return jdbcTemplate.queryForObject(sql, new FilmRowMapper(), id);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        String sql = "INSERT INTO film_likes (film_id,user_id) VALUES (?,?);";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        String sql = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?;";
        int count = jdbcTemplate.update(sql, filmId, userId);
        if (count == 0) {
            throw new NotExistsException("Лайк у фильма с id(" + filmId + ") от пользователя", userId);
        }
    }

    @Override
    public List<Film> getTopFilms(int count) {
        String sql = "SELECT f.*,m.name AS mpa_name," +
                "COUNT(fl.user_id) AS count_likes,GROUP_CONCAT(fl.user_id) AS likes, " +
                "GROUP_CONCAT(CONCAT(g.genre_id,'@',g.name) ORDER BY g.genre_id) AS genres " +
                "FROM films AS f " +
                "JOIN mpas AS m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_likes AS fl ON fl.film_id = f.film_id " +
                "LEFT JOIN film_genres AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id " +
                "GROUP BY f.film_id " +
                "ORDER BY count_likes DESC " +
                "LIMIT ?;";
        return jdbcTemplate.query(sql, new FilmRowMapper(), count);
    }

}
