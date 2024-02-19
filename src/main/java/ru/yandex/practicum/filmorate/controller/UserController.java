package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {
        log.info("получен запрос получения пользователя по ID: {}", id);
        User user1 = userService.getUserByID(id);
        log.info("получен пользователь: {}", user1);
        return user1;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAll() {
        log.info("получен запрос получения списка всех пользователей.");
        List<User> allFilms = userService.getAllUsers();
        return allFilms;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        User user1 = userService.createUser(user);
        log.info("Пользователь создан {}", user1.getName());
        return user1;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        User user1 = userService.updateUser(user);
        log.info("Пользователь обновлен {}", user1.getName());
        return user1;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
        log.info("Пользователю обновлен друг {}", id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriend(id, friendId);
        log.info("Пользователю удален друг {}", id);
    }

    @GetMapping("{id}/friends")
    public List<User> getAllFriends(@PathVariable Long id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
