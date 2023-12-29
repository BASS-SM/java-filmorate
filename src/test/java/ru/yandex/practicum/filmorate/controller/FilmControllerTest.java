package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

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
        filmController = new FilmController();
    }

    @Test
    void createStandart() {
        filmController.create(film);
        assertEquals(List.of(film).toArray().length, 1);
    }

    @Test
    void createFailName() {
        film.setName("");
        Throwable exception = assertThrows(ValidateException.class, () -> filmController.create(film));
        assertEquals(exception.getMessage(), "Название не может быть пустым");
    }

    @Test
    void createFailDescription() {
        film.setDescription("Описание длиннее 200 символов\n" +
                "Описание длиннее 200 символов\n" +
                "Описание длиннее 200 символов\n" +
                "Описание длиннее 200 символов\n" +
                "Описание длиннее 200 символов\n" +
                "Описание длиннее 200 символов\n" +
                "Описание длиннее 200 символов\n" +
                "Описание длиннее 200 символов");
        Throwable exception = assertThrows(ValidateException.class, () -> filmController.create(film));
        assertEquals(exception.getMessage(), "Описание не должно быть длиннее 200 символов");
    }

    @Test
    void createFailDate() {
        film.setReleaseDate(LocalDate.of(1795, 12, 28));
        Throwable exception = assertThrows(ValidateException.class, () -> filmController.create(film));
        assertEquals(exception.getMessage(), "Дата не может быть ранее 28-12-1895");
    }

    @Test
    void createFailDuration() {
        film.setDuration(-100);
        Throwable exception = assertThrows(ValidateException.class, () -> filmController.create(film));
        assertEquals(exception.getMessage(), "Продолжительность должна быть положительной");
    }

    @Test
    void update() {
    }
}