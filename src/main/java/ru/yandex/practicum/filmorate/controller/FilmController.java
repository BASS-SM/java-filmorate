package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

import static ru.yandex.practicum.filmorate.constant.FilmConstant.DEFAULT_VALUE_COUNT;


@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        log.info("Получаем один фильм по id : {}", id);
        return service.findById(id);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Получен запрос GET, на получения всех фильмов.");
        List<Film> filmList = service.getAllFilm();
        log.info("Получен список всех фильмов: {}", filmList.size());
        return filmList;
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@Positive @RequestParam(defaultValue = DEFAULT_VALUE_COUNT) String count) {
        log.info("Получен запрос GET, на получение топ {} фильмов.", count);
        List<Film> filmList = service.getPopularFilms(count);
        log.info("Получен топ {} фильмов: {}", count, filmList.size());
        return filmList;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос Post, по фильму: {}", film);
        log.info("Добавлен фильм: {}", film);
        return service.saveFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос Post, на обновления данных по фильму: {}", film);
        Film film1 = service.updateFilm(film);
        log.info("Добавлен или обновлен фильм: {}", film1);
        return film1;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен запрос PUT, на добавления лайков, по id: {}", userId);
        service.addLike(id, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("Получен запрос DELETE, на удаления фильма, по id: {}", id);
        service.delete(id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен запрос DELETE, на удаление лайков, по id: {}", id);
        service.deleteLike(id, userId);
    }
}