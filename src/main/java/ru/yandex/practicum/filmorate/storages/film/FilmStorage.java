package ru.yandex.practicum.filmorate.storages.film;

import ru.yandex.practicum.filmorate.models.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Collection<Film> getAll();

    Film create(Film film);

    Film update(Film film);

    Film findById(Long filmId);

    boolean isExist(Long id);

    List<Film> getFilms(List<Long> filmsId);
}