package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface FilmStorage {
    Set<Film> getCompareFilm();

    Collection<Film> getAllFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film findFilmById(Long filmId);

    Map<Long, Film> getFilms();

}
