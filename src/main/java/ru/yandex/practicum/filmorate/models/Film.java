package ru.yandex.practicum.filmorate.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.validation.FutureAfterDate;

import java.time.LocalDate;


@Data
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {

    static long currentId = 0;
    long id;
    @NotBlank(message = "Название не может быть пустым!")
    String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов!")
    String description;
    @FutureAfterDate(date = "1895-12-28", message = "Дата релиза — не раньше 28 декабря 1895 года!")
    LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительной!")
    long duration;

    public Film(String name, String description, LocalDate releaseDate, long duration) {
        this.id = currentId;
        currentId++;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Film() {
        this.id = currentId;
        currentId++;
    }
}
