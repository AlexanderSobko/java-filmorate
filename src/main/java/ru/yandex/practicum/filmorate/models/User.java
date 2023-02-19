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

    static Long currentId = 0L;
    long id;
    @NotBlank(message = "Электронная почта не может быть пустой!")
    @Email(message = "Электронная почта должна содержать символ @!")
    String email;
    @NotEmpty(message = "Логин не может быть пустым!")
    @Pattern(regexp = "\\w*", message = "Логин не может содержать пробелы!")
    String login;
    String name;
    @Past(message = "Дата рождения не может быть в будущем!")
    LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        this.id = currentId;
        currentId++;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public User() {
        this.id = currentId;
        currentId++;
    }


}
