package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final HttpHeaders headers;

    static {
        headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
    }

    @ExceptionHandler(value = AlreadyExistsException.class)
    protected ResponseEntity<Object> handleAlreadyExistsExc(AlreadyExistsException ex, WebRequest request) {
        return handleExceptionInternal(ex, Map.of("message", ex.getMessage()),
                headers, INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = NotExistsException.class)
    protected ResponseEntity<Object> handleNotExistExc(NotExistsException ex, WebRequest request) {
        return handleExceptionInternal(ex, Map.of("message", ex.getMessage()),
                headers, HttpStatus.NOT_FOUND, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        List<String> errorList = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return handleExceptionInternal(exception, errorList, this.headers, BAD_REQUEST, request);
    }

}