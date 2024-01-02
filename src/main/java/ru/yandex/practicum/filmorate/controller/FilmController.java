package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static ru.yandex.practicum.filmorate.constant.FilmConstant.RELEASE_DATE;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private Integer id = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public ResponseEntity<List<Film>> findAll() {
        return ResponseEntity.ok().body(new ArrayList<>(films.values()));
    }

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(RELEASE_DATE)) {
            throw new ValidateException("Дата не может быть ранее " + RELEASE_DATE, BAD_REQUEST);
        }
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Фильм добавлен");
        return ResponseEntity.ok(film);
    }

    @PutMapping
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            if (film.getReleaseDate().isBefore(RELEASE_DATE)) {
                throw new ValidateException("Дата не может быть ранее " + RELEASE_DATE, BAD_REQUEST);
            }
            films.put(film.getId(), film);
            log.info("Фильм обновлен {}.", film.getName());
            return ResponseEntity.ok(film);
        } else {
            log.error("Фильм не обновлен (Не найден)");
            return ResponseEntity.status(404).body(film);
        }
    }
}
