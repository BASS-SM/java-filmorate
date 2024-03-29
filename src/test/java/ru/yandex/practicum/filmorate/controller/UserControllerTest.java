package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        UserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserServiceImpl(userStorage);
        userController = new UserController(userService);
    }

    @Test
    void createStandart() {
        userController.create(user);
        assertEquals(List.of(user).toArray().length, 1);
    }

    @Test
    void createFailLogin() {
        user.setName("");
        userController.create(user);
        assertEquals(user.getLogin(), user.getLogin());
    }
}