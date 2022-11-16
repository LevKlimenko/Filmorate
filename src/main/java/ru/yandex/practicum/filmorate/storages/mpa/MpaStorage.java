package ru.yandex.practicum.filmorate.storages.mpa;

import ru.yandex.practicum.filmorate.models.Mpa;

import java.util.List;

public interface MpaStorage {
    Mpa findById(Long id);

    List<Mpa> getAll();

}
