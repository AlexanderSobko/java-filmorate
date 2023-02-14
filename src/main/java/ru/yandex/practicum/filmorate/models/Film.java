package ru.yandex.practicum.filmorate.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.validation.FutureAfterDate;
import java.time.LocalDate;


@Data
@Component
public class Film {

    private static long currentId = 0;
    private long id;
    @NotBlank(message = "Films name can't be empty!")
    private String name;
    @Size(max = 200, message = "Description must be less than 200 characters!")
    private String description;
    @FutureAfterDate(date = "1895-12-28", message = "The release date must be after December 28th 1895")
    private LocalDate releaseDate;
    @Positive(message = "Duration can't be negative!")
    private long duration;

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
