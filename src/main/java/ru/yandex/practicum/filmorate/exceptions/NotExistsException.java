package ru.yandex.practicum.filmorate.exceptions;

public class NotExistsException extends RuntimeException {

    public NotExistsException(String objectName, Long id) {
        super(String.format("%s c таким id(%d) не существует!", objectName, id));
    }
}
