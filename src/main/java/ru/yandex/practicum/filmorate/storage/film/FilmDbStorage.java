package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotExistsException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.Mpa;
import ru.yandex.practicum.filmorate.storage.genre.GanreRowMapper;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRowMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film save(Film entity) {
        final String sql = String.format("INSERT INTO films (name,description,duration,release_date)" +
                        " VALUES ('%s','%s','%s','%s')",
                entity.getName(), entity.getDescription(), entity.getDuration(), entity.getReleaseDate().toString());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> connection.prepareStatement(sql, new String[]{"film_id"}), keyHolder);
        int id = keyHolder.getKey().intValue();
        if (entity.getMpa() != null) {
            linkMpa(id, entity.getMpa().getId());
        }
        if (entity.getGenres() != null) {
            entity.getGenres().forEach(genre -> linkGenre(id, genre.getId()));
        }
        return findById(id);
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM films WHERE film_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Film update(Film entity) {
        String sql = "UPDATE films SET name = ?, description = ?, duration = ?, release_date = ? WHERE film_id = ?";
        Film film = findById(entity.getId());
        if (entity.getMpa().getId() != film.getMpa().getId()) {
            deleteMpa(entity.getId(), film.getMpa().getId());
            linkMpa(entity.getId(), entity.getMpa().getId());
        }
        if (entity.getGenres() != null
                && film.getGenres() != null
                && (!film.getGenres().containsAll(entity.getGenres())
                || !entity.getGenres().containsAll(film.getGenres()))) {
            film.getGenres().forEach(genre -> deleteGenre(entity.getId(), genre.getId()));
            entity.getGenres().forEach(genre -> linkGenre(entity.getId(), genre.getId()));
        }
        jdbcTemplate.update(sql,
                entity.getName(),
                entity.getDescription(),
                entity.getDuration(),
                entity.getReleaseDate().toString(),
                entity.getId());
        return findById(entity.getId());
    }

    @Override
    public List<Film> findAll() {
        String sql = "SELECT * FROM films;";
        List<Film> films = jdbcTemplate.query(sql, new FilmRowMapper());
        films.forEach(f -> {
            f.setLikes(getLikes(f.getId()));
            f.setMpa(getMpaByFilmId(f.getId()));
            f.setGenres(getGenresByFilmId(f.getId()));
        });
        return films;
    }

    @Override
    public boolean exists(int id) {
        String query = "SELECT EXISTS(SELECT * FROM films WHERE film_id = ?)";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, Boolean.class);
    }

    @Override
    public Film findById(int id) {
        String sql = "SELECT * FROM films WHERE film_id = ?;";
        Film film = jdbcTemplate.queryForObject(sql, new FilmRowMapper(), id);
        film.setLikes(getLikes(id));
        film.setGenres(getGenresByFilmId(id));
        film.setMpa(getMpaByFilmId(id));
        return film;
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        String sql = "INSERT INTO film_likes (film_id,user_id) VALUES (?,?);";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        try {
            String sql = "DELETE FROM film_likes WHERE film_id = ?,user_id = ?;";
            jdbcTemplate.update(sql, filmId, userId);
        } catch (RuntimeException e) {
            throw new NotExistsException("Пользователь", userId);
        }
    }

    @Override
    public Set<Integer> getLikes(int filmId) {
        String sql = "SELECT user_id FROM film_likes WHERE film_id = ?;";
        return new HashSet<>(jdbcTemplate.queryForList(sql, Integer.class, filmId));
    }

    private Set<Genre> getGenresByFilmId(int filmId) {
        String sql = "SELECT g.genre_id, g.name FROM genres AS g " +
                "JOIN film_genres AS fg ON g.genre_id = fg.genre_id WHERE film_id = ? ORDER BY g.genre_id;";
        return new HashSet<>(jdbcTemplate.query(sql, new GanreRowMapper(), filmId));
    }

    private Mpa getMpaByFilmId(int filmId) {
        String sql = "SELECT m.mpa_id, m.name FROM mpas AS m" +
                " JOIN film_mpas AS fm ON m.mpa_id = fm.mpa_id WHERE fm.film_id = ?;";
        return jdbcTemplate.queryForObject(sql, new MpaRowMapper(), filmId);
    }

    private void linkGenre(int filmId, int genreId) {
        String sql = "INSERT INTO film_genres (film_id,genre_id) VALUES (?,?);";
        jdbcTemplate.update(sql, filmId, genreId);
    }

    private void deleteGenre(int filmId, int genreId) {
        String sql = "DELETE FROM film_genres WHERE film_id = ? AND genre_id = ?;";
        jdbcTemplate.update(sql, filmId, genreId);
    }

    private void deleteMpa(int filmId, int mpaId) {
        String sql = "DELETE FROM film_mpas WHERE film_id = ? AND mpa_id = ?;";
        jdbcTemplate.update(sql, filmId, mpaId);
    }

    private void linkMpa(int filmId, int mpaId) {
        String sql = "INSERT INTO film_mpas (film_id,mpa_id) VALUES (?,?);";
        jdbcTemplate.update(sql, filmId, mpaId);
    }

}
