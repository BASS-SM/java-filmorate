package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Data
@Builder
public class Genre {
    private Long id;
    private String name;
}


