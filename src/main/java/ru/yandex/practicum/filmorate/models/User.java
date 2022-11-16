package ru.yandex.practicum.filmorate.models;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class User {
    @NotNull
    @Email(message = "Введите действительный E-mail")
    private final String email;
    @NotBlank(message = "Логин не может быть пустым или состоять только из пробелов")
    private final String login;
    private Long id;
    private String name;
    @NotNull
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}