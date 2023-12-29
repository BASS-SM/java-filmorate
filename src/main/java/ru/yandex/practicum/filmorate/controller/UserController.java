package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer id = 1;
    public EmailValidator emailValidator = new EmailValidator();

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(new ArrayList<>(users.values()));
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        validate(user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Пользователь создан");
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<User> update(@RequestBody User user) {
        if (users.containsKey(user.getId())) {
            validate(user);
            users.put(user.getId(), user);
            log.info("Пользователь обновлен");
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(404).body(user);
        }
    }

    private void validate(User user) {
        String userEmail = user.getEmail();
        String userLogin = user.getLogin();
        String userName = user.getName();
        LocalDate userBirthday = user.getBirthday();

        boolean validEmail = emailValidator.validate(userEmail);
        if (!validEmail) {
            log.error("Неверный маил - " + userEmail);
            throw new ValidateException("Неверный маил", BAD_REQUEST);
        }

        if (userLogin == null || userLogin.isEmpty()) {
            log.error("Логин не может быть пустым");
            throw new ValidateException("Логин не может быть пустым", BAD_REQUEST);
        }
        if (userName == null || userLogin.isEmpty() || userLogin.contains(" ")) {
            log.info("Применен логин в имя");
            user.setName(userLogin);
        }
        if (userBirthday.isAfter(LocalDate.now())) {
            log.error("Дата не может быть больше текущей");
            throw new ValidateException("Дата не может быть больше текущей", BAD_REQUEST);
        }
    }
}
