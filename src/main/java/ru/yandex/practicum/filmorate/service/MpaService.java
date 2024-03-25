package ru.yandex.practicum.filmorate.service;


import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
import java.util.Optional;

public interface MpaService {
    MPA findById(Long id);

    List<MPA> getAllMPA();
}