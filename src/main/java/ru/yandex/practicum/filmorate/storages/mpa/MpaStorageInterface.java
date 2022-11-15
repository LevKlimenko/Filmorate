package ru.yandex.practicum.filmorate.storages.mpa;

import ru.yandex.practicum.filmorate.models.Mpa;

import java.util.List;

public interface MpaStorageInterface {
    Mpa findMpaById(Long id);

    List<Mpa> findAllMpa();

}
