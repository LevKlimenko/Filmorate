package ru.yandex.practicum.filmorate.models;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Builder
@Data
public class Mpa {

    private Long id;
    @NotBlank
    private String name;
}
