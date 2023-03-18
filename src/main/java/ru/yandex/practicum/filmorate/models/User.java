package ru.yandex.practicum.filmorate.models;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    Integer id;
    @NotBlank(message = "Электронная почта не может быть пустой!")
    @Email(message = "Электронная почта должна содержать символ @!")
    String email;
    @NotEmpty(message = "Логин не может быть пустым!")
    @Pattern(regexp = "\\w*", message = "Логин не может содержать пробелы!")
    String login;
    String name;
    @Past(message = "Дата рождения не может быть в будущем!")
    LocalDate birthday;
    Set<Integer> friends = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void addFriend(Integer id) {
        friends.add(id);
    }

    public List<Integer> getFriends() {
        return new ArrayList<>(friends);
    }

    public void deleteFriend(Integer id) {
        friends.remove(id);
    }

}
