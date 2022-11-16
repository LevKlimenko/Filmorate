package ru.yandex.practicum.filmorate.models;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.DateReleaseConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class Film {
    private Long id;
    @NotBlank(message = "Название не может быть пустым или состоять только из пробелов")
    private String name;
    @Size(max = 200, message = "Длина описания больше 200 символов")
    private String description;
    @NotNull
    @DateReleaseConstraint
    private LocalDate releaseDate;
    @PositiveOrZero(message = "Продолжительность должна быть больше или равно 0")
    private int duration;
    @NotNull
    private int rate;
    @NotNull
    private Mpa mpa;
    private List<Genre> genres;
}