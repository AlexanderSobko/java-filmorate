package ru.yandex.practicum.filmorate.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    static public Long currentId = 0L;
    Long id;
    @NotBlank(message = "Электронная почта не может быть пустой!")
    @Email(message = "Электронная почта должна содержать символ @!")
    String email;
    @NotEmpty(message = "Логин не может быть пустым!")
    @Pattern(regexp = "\\w*", message = "Логин не может содержать пробелы!")
    String login;
    String name;
    @Past(message = "Дата рождения не может быть в будущем!")
    LocalDate birthday;

    public Long getId() {
        return id;
    }

}
