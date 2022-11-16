package ru.yandex.practicum.filmorate.services.mpa;

import ru.yandex.practicum.filmorate.models.Mpa;

import java.util.Collection;
import java.util.List;

public interface MpaService {

    List<Mpa> getAll();

    Mpa findById(Long id);
}
