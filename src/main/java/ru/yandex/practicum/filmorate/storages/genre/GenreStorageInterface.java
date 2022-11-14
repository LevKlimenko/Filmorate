package ru.yandex.practicum.filmorate.storages.genre;

import ru.yandex.practicum.filmorate.models.Genre;

import java.util.List;

public interface GenreStorageInterface {
    Genre findGenreById(Long id);
    List<Genre> findAllGenre();
    List<Genre> findGenresOfFilm(Long id);
}
