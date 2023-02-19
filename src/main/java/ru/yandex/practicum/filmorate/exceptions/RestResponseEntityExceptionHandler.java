package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    private final HttpHeaders headers;

    {
        headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
    }

    @ExceptionHandler(value = AlreadyExistsException.class)
    protected ResponseEntity<Object> handleAlreadyExistsExc(AlreadyExistsException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(),
                headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = NotExistsException.class)
    protected ResponseEntity<Object> handleNotExistExc(NotExistsException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(),
                headers, HttpStatus.NOT_FOUND, request);
    }

}