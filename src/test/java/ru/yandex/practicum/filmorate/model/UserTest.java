package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {
    Validator validator;
    User user = User.builder()
            .name("Name name")
            .email("test@test.test")
            .login("name2")
            .birthday(LocalDate.of(1999, 8, 22))
            .build();

    @BeforeEach
    void setUp() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.usingContext().getValidator();
        }
    }

    @Test
    void failMail() {
        user.setEmail(" ");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        String mesage = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(""));
        assertEquals("не верно введен мэил.", mesage);
    }

    @Test
    void loginFail() {
        user.setLogin(" ");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        String mesage = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(""));
        boolean isTrue = false;
        if (mesage.equals("Не должно быть пробелов.Пустое поле.") ||
                mesage.equals("Пустое поле.Не должно быть пробелов.")) {
            isTrue = true;
        }
        assertTrue(isTrue);
    }

    @Test
    void dateBirth() {
        user.setBirthday(LocalDate.of(2099, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals("Дата превышает.", violations.iterator().next().getMessage());
    }
}