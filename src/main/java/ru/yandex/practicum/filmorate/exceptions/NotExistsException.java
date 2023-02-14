package ru.yandex.practicum.filmorate.exceptions;

public class NotExistsException extends RuntimeException {

    public NotExistsException(String objectName, Long id) {
        super("The %s with id(%d) doesn't exist!".formatted(objectName, id));
    }
}
