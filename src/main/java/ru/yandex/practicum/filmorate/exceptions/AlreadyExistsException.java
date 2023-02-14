package ru.yandex.practicum.filmorate.exceptions;

public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException(String objectName, Long id) {
        super("The %s with id(%d) already exists!".formatted(objectName, id));
    }
}
