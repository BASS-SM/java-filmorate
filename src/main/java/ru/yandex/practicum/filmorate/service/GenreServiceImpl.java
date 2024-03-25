package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDAO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreDAO genreDAO;


    @Override
    public Genre findById(Long id) {
        return genreDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Жанр не найден", HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Genre> findAll() {
        return genreDAO.findAll();
    }
}