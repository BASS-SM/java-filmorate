package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MpaDAO;


import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {
  //  private final GenreDAO genreDAO;
    private final MpaDAO mpaDAO;

    @Override
    public MPA findById(Long id) {
        return mpaDAO.findById(id).orElseThrow(()->{
            throw new NotFoundException("Не найден фильм по ID: ", NOT_FOUND);
        });

    }

    @Override
    public List<MPA> getAllMPA() {
        return mpaDAO.findAll();
    }
}