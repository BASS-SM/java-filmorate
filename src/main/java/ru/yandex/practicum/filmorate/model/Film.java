package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {
    @Positive(message = "Должно быть положительное значение ID.")
    private Long id;
    @NotBlank(message = "Не заполнено имя.")
    private String name;
    @Size(min = 1, max = 200, message = "Максимальное не больше 200 символов.")
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "Должно быть положительное значение.")
    private Integer duration;
    private final Set<Long> listLike = new HashSet<>();
}
