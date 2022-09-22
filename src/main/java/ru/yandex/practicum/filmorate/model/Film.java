package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;
@Data
@Builder
public class Film {
    @Positive(message = "Значение ID должно быть больше 0")
    private int id;
    @NotBlank(message = "Название не может быть пустым или состоять только из пробелов")
    private String name;
    @Size(max = 200, message = "Длина описания больше 200 символов")
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Min(value = 1895-12-28, message = "Дата должна быть больше 1895-12-28")
    private LocalDate releaseDate;
    @PositiveOrZero(message = "Продолжительность должна быть больше или равно 0")
    private int duration;
}
