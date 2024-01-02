package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmTest {
    Validator validator;
    Film film = Film.builder()
            .name("name")
            .description("description")
            .releaseDate(LocalDate.of(1997, 3, 24))
            .duration(100)
            .build();

    @BeforeEach
    void setUp() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.usingContext().getValidator();
        }
    }

    @Test
    void notNullName() {
        film.setName("");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("Не заполнено имя.", violations.iterator().next().getMessage());
    }

    @Test
    void descriptionSizeMax() {
        film.setDescription("Описание длиннее 200 символов\n" +
                "Описание длиннее 200 символов\n" +
                "Описание длиннее 200 символов\n" +
                "Описание длиннее 200 символов\n" +
                "Описание длиннее 200 символов\n" +
                "Описание длиннее 200 символов\n" +
                "Описание длиннее 200 символов\n" +
                "Описание длиннее 200 символов");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("Максимальное не больше 200 символов.", violations.iterator().next().getMessage());
    }

    @Test
    void createFailDuration() {
        film.setDuration(-100);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("Должно быть положительное значение.", violations.iterator().next().getMessage());
    }
}