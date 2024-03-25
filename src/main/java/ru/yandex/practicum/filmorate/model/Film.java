package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.anotation.DateIsAfter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

import static ru.yandex.practicum.filmorate.constant.FilmConstant.FILM_RELEASE_DATE;

@Data
@Builder
public class Film {
    @Positive(message = "Должно быть положительное значение ID.")
    private Long id;
    @NotBlank(message = "Не заполнено имя.")
    private String name;
    @Size(min = 1, max = 200, message = "Максимальное не больше 200 символов.")
    private String description;
    @DateIsAfter(value = FILM_RELEASE_DATE, message = "28.12.1895")
    private LocalDate releaseDate;
    @Positive(message = "Должно быть положительное значение.")
    private Integer duration;
    private Set<Long> listLike;

    private Set<Genre> genres;
    private MPA mpa;
}
