package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    protected Integer id = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public ResponseEntity<List<Film>> findAll() {
        return ResponseEntity.ok().body(new ArrayList<>(films.values()));
    }

    @PostMapping
    public ResponseEntity<Film> create(@RequestBody Film film) {
        validate(film);
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Фильм добавлен");
        return ResponseEntity.ok(film);
    }

    @PutMapping
    public ResponseEntity<Film> update(@RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            validate(film);
            films.put(film.getId(), film);
            log.info("Фильм обновлен");
            return ResponseEntity.ok(film);
        } else {
            log.error("Фильм не обновлен (Не найден)");
            return ResponseEntity.status(404).body(film);
        }
    }

    private void validate(Film film) {
        String filmName = film.getName();
        String description = film.getDescription();
        LocalDate releaseDate = film.getReleaseDate();
        Integer duration = film.getDuration();

        if (filmName == null || filmName.isEmpty()) {
            log.error("Название не может быть пустым");
            throw new ValidateException("Название не может быть пустым", BAD_REQUEST);
        }
        if (description.length() > 200) {
            log.error("Описание длиннее 200 символов");
            throw new ValidateException("Описание не должно быть длиннее 200 символов", BAD_REQUEST);
        }
        if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата не может быть ранее 28-12-1895");
            throw new ValidateException("Дата не может быть ранее 28-12-1895", BAD_REQUEST);
        }
        if (duration < 0) {
            log.error("Продолжительность отрицательная");
            throw new ValidateException("Продолжительность должна быть положительной", BAD_REQUEST);
        }
    }
}
