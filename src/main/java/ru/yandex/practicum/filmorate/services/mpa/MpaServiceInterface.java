package ru.yandex.practicum.filmorate.services.mpa;

import ru.yandex.practicum.filmorate.models.Mpa;

import java.util.Collection;

public interface MpaServiceInterface {

    Collection<Mpa> getAll();

    Mpa findById(Long id);
}
