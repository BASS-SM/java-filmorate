package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService service;

    @GetMapping("/{id}")
    public MPA findById(@PathVariable Long id) {
        log.info("Получаем рейтинг фильма по id (GET): {}", id);
        return service.findById(id);
    }

    @GetMapping
    public List<MPA> getAllMPA() {
        log.info("Получен запрос, на получения всех рейтингов (GET)");
        List<MPA> mpaList = service.getAllMPA();
        log.info("Получен список всех рейтингов (GET): {}", mpaList.size());
        return mpaList;
    }
}