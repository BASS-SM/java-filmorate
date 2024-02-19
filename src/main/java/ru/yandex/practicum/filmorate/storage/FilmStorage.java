package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film findByID(Long id);

    List<Film> findAll();

    Film save(Film film);

    Film update(Film film);

    boolean addLike(Long id, Long userId);

    boolean deleteLike(Long id, Long userId);

    List<Film> getPopularFilms(Integer count);


}
