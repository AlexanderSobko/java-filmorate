package ru.yandex.practicum.filmorate.exceptions;

public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException(String objectName, Integer id) {
        super(String.format("%s с таким id(%d) уже существет!", objectName, id));
    }
}
