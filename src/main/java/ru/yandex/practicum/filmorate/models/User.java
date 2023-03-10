package ru.yandex.practicum.filmorate.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
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
    Set<Long> friends = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void addFriend(Long id) {
        friends.add(id);
    }

    public List<Long> getFriends() {
        return new ArrayList<>(friends);
    }

    public void deleteFriend(Long id) {
        friends.remove(id);
    }

}
