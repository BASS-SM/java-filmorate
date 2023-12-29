package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    UserController userController;
    User user = User.builder()
            .email("tr@tr.tr")
            .login("testlogin")
            .name("testname")
            .birthday(LocalDate.of(1997, 3, 24))
            .build();

    @BeforeEach
    void beforeEach() {
        userController = new UserController();
    }

    @Test
    void createStandart() {
        userController.create(user);
        assertEquals(List.of(user).toArray().length, 1);
    }

    @Test
    void createFailMail() {
        user.setEmail("qwe@er");
        Throwable exception = assertThrows(ValidateException.class, () -> userController.create(user));
        assertEquals(exception.getMessage(), "Неверный маил");
    }

    @Test
    void createFailLogin() {
        user.setLogin("");
        Throwable exception = assertThrows(ValidateException.class, () -> userController.create(user));
        assertEquals(exception.getMessage(), "Логин не может быть пустым");
    }

    @Test
    void createFailDate() {
        user.setBirthday(LocalDate.of(3000, 12, 1));
        Throwable exception = assertThrows(ValidateException.class, () -> userController.create(user));
        assertEquals(exception.getMessage(), "Дата не может быть больше текущей");
    }
}