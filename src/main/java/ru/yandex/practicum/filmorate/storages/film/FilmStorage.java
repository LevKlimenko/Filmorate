package ru.yandex.practicum.filmorate.storages.film;

import ru.yandex.practicum.filmorate.models.Film;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FilmStorage {
    Set<Film> getCompare();

    Collection<Film> getAll();

    Film create(Film film);

    Film update(Film film);

    Film findById(Long filmId);

    Map<Long, Film> getMap();

    boolean isExist(Long id);

    List<Film> getFilms(List<Long> filmsId);
}
