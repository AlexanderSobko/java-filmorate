package ru.yandex.practicum.filmorate.models;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.validation.FutureAfterDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    Integer id;
    @NotBlank(message = "Название не может быть пустым!")
    String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов!")
    String description;
    @FutureAfterDate(date = "1895-12-28", message = "Дата релиза — не раньше 28 декабря 1895 года!")
    LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительной!")
    long duration;
    Set<Integer> likes;
    Set<Genre> genres;
    Mpa mpa;

    public void addLike(Integer userId) {
        likes.add(userId);
    }

    public List<Integer> getLikes() {
        return new ArrayList<>(likes);
    }

}
