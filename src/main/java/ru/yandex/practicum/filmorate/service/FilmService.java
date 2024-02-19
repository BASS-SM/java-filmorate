package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film getFilmByID(Long id);

    List<Film> getAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void addLike(Long id, Long userId);

    void deleteLike(Long id, Long userId);

    List<Film> getPopularFilms(String count);


}
