package ru.yandex.practicum.filmorate.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@Builder

public class BadRequestException extends RuntimeException {
    private String message;
    private HttpStatus status;
}