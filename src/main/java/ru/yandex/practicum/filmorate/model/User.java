package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import lombok.*;

import javax.validation.constraints.*;

@Data
@Builder
public class User {
    @Positive(message = "Значение ID должно быть больше 0")
    private Integer id;
    @Email(message = "Введите действительный E-mail")
    private final String email;
    @NotBlank(message = "Логин не может быть пустым или состоять только из пробелов")
    private final String login;
    private String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}
