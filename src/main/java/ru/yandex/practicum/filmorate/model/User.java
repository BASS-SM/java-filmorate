package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.constant.UserConstant.EMAIL_REGEX;
import static ru.yandex.practicum.filmorate.constant.UserConstant.LOGIN_REGEX;

@Data
@Builder
public class User {
    private Integer id;
    @Email(regexp = EMAIL_REGEX, message = "не верно введен мэил.")
    private String email;
    @NotBlank(message = "Пустое поле.")
    @Pattern(regexp = LOGIN_REGEX, message = "Не должно быть пробелов.")
    private String login;
    private String name;
    @Past(message = "Дата превышает.")
    private LocalDate birthday;
}