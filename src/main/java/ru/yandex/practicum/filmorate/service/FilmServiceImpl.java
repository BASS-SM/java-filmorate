package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.yandex.practicum.filmorate.constant.FilmConstant.RELEASE_DATE;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public Film createFilm(Film film) {
        if (film.getReleaseDate().isBefore(RELEASE_DATE)) {
            throw new ValidateException("Дата не может быть ранее " + RELEASE_DATE, BAD_REQUEST);
        }
        return filmStorage.save(film);
    }

    @Override
    public Film getFilmByID(Long id) {
        Film film = filmStorage.findByID(id);
        if (film == null) {
            throw new NotFoundException("Не найден ID: " + id, NOT_FOUND);
        }
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.findAll();
    }

    @Override
    public Film updateFilm(Film film) {
        if (film == null) {
            throw new NotFoundException("Не найден фильм по ID: ", NOT_FOUND);
        }
        return filmStorage.update(film);
    }

    @Override
    public void addLike(Long id, Long userId) {
        checkIds(id, userId);
        filmStorage.addLike(id, userId);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        checkIds(id, userId);
        filmStorage.deleteLike(id, userId);
    }

    @Override
    public List<Film> getPopularFilms(String count) {
        return filmStorage.getPopularFilms(Integer.valueOf(count));
    }

    public void checkIds(Long filmId, Long userId) {
        Film film = filmStorage.findByID(filmId);
        User user = userStorage.findByID(userId);

        if (film == null && user == null) {
            throw new NotFoundException("Не найден фильм по ID: " + filmId
                    + ". Не найден пользователь по ID: " + userId, NOT_FOUND);
        }

        if (film == null) {
            throw new NotFoundException("Не найден фильм по ID: " + filmId, NOT_FOUND);
        }

        if (user == null) {
            throw new NotFoundException("Не найден пользователь по ID: " + userId, NOT_FOUND);
        }
    }
}
