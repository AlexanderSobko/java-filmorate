package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .likes(parseLikes(rs))
                .duration(rs.getInt("duration"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .mpa(Mpa.builder().id(rs.getInt("mpa_id")).name(rs.getString("mpa_name")).build())
                .genres(parseGenres(rs))
                .build();
    }

    private Set<Integer> parseLikes(ResultSet rs) throws SQLException {
        return rs.getString("likes") == null ?
                new HashSet<>()
                : Arrays.stream(rs.getString("likes").split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }

    private Set<Genre> parseGenres(ResultSet rs) throws SQLException {
        String data = rs.getString("genres");
        if (data.length() > 2) {
            return Arrays.stream(data.split(","))
                    .map(genre -> {
                        String[] gen = genre.split("@");
                        int genre_id = Integer.parseInt(gen[0]);
                        return new Genre(genre_id, gen[1]);
                    }).collect(Collectors.toSet());
        }
        return new HashSet<>();
    }
}
