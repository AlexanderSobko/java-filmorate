package ru.yandex.practicum.filmorate.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.validation.FutureAfterDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@Component
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {

    public static long currentId = 1L;
    Long id;
    @NotBlank(message = "Название не может быть пустым!")
    String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов!")
    String description;
    @FutureAfterDate(date = "1895-12-28", message = "Дата релиза — не раньше 28 декабря 1895 года!")
    LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительной!")
    long duration;

    Set<Long> likes = new HashSet<>();

    public void addLike(Long userId) {
        likes.add(userId);
    }

    public List<Long> getLikes() {
        return new ArrayList<>(likes);
    }

    public Film(String name, String description, LocalDate releaseDate, long duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

}
