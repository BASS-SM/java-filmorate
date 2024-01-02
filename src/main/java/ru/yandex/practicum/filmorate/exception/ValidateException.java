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
public class ValidateException extends RuntimeException {
    private String message;
    private HttpStatus status;
}
