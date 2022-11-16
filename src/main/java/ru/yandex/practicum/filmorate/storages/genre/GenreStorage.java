package ru.yandex.practicum.filmorate.storages.genre;

import ru.yandex.practicum.filmorate.models.Genre;

import java.util.List;

public interface GenreStorage {
    Genre findById(Long id);

    List<Genre> getAll();
}
