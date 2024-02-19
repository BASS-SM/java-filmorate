package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Error> handleValidException(final ValidateException e) {
        return new ResponseEntity<>(new Error("Ошибка: " + e.getMessage()), e.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity<Error> handleNotFoundException(final NotFoundException e) {
        return new ResponseEntity<>(new Error("Ошибка: " + e.getMessage()), e.getStatus());
    }
}
