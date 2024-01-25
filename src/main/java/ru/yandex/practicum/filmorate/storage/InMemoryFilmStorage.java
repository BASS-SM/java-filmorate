package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class InMemoryFilmStorage implements FilmStorage {

    private Long id = 1L;
    private final Map<Long, Film> films = new HashMap<>();


    @Override
    public Film findByID(Long id) {
        return films.getOrDefault(id, null);
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film save(Film film) {
        film.setId(id++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            throw new NotFoundException("Фильм не найден" + film.getId(), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public boolean addLike(Long id, Long userId) {
        return films.get(id).getListLike().add(userId);
    }

    @Override
    public boolean deleteLike(Long id, Long userId) {
        return films.get(id).getListLike().remove(userId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return findAll()
                .stream()
                .sorted((film1, film2) -> film2.getListLike().size() - film1.getListLike().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
