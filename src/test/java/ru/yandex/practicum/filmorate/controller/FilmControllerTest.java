package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.FilmServiceImpl;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class FilmControllerTest {
    FilmController filmController;
    Film film = Film.builder()
            .name("film name")
            .description("film description")
            .releaseDate(LocalDate.of(1997, 3, 24))
            .duration(100)
            .build();

    @BeforeEach
    void beforeEach() {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        FilmService filmService = new FilmServiceImpl(filmStorage, userStorage);
        filmController = new  FilmController(filmService);
    }

    @Test
    void createStandart() {
        filmController.createFilm(film);
        assertEquals(List.of(film).toArray().length, 1);
    }

    @Test
    void createFailDate() {
        film.setReleaseDate(LocalDate.of(1795, 12, 28));
        Throwable exception = assertThrows(ValidateException.class, () -> filmController.createFilm(film));
        assertEquals(exception.getMessage(), "Дата не может быть ранее 1895-12-28");
    }
}