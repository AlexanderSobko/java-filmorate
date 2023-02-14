package ru.yandex.practicum.filmorate.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Data
@Component
public class User {

    private static Long currentId = 0L;
    private long id;
    @NotBlank(message = "Email can't be blank!")
    @Email(message = "Mast be a valid email address!")
    private String email;
    @NotEmpty(message = "Login mustn't be empty!")
    @Pattern(regexp = "\\w*", message = "Login mustn't have any whitespaces!")
    private String login;
    private String name;
    @Past(message = "You've not born yet)")
    private LocalDate birthday;

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
