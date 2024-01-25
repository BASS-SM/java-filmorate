package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

import static ru.yandex.practicum.filmorate.constant.FilmConstant.DEFAULT_VALUE_COUNT;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film findById(@PathVariable Long id) {
        log.info("получен запрос получения фильма по ID: {}", id);
        Film film1 = filmService.getFilmByID(id);
        log.info("получен фильм: {}", film1);
        return film1;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Film> findAll() {
        log.info("получен запрос получения списка всех фильмов.");
        List<Film> allFilms = filmService.getAllFilms();
        log.info("получен список всех фильмов, кол-во: {}", allFilms.size());
        return allFilms;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("получен запрос создания фильма: {}", film.getName());
        filmService.createFilm(film);
        log.info("Фильм добавлен");
        return film;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("получен запрос обновления фильма: {}", film.getName());
        filmService.updateFilm(film);
        log.info("Фильм обновлен");
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = DEFAULT_VALUE_COUNT) String count) {
        return filmService.getPopularFilms(count);
    }
}