package ru.yandex.practicum.filmorate.models;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.models.constants.FriendStatus;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder
public class User {
    private Long id;
    @NotNull
    @Email(message = "Введите действительный E-mail")
    private final String email;
    @NotBlank(message = "Логин не может быть пустым или состоять только из пробелов")
    private final String login;
    private String name;
    @NotNull
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
    private final Map<Long, FriendStatus> friendsId = new HashMap<>();
}
