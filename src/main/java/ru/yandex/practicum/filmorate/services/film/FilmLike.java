package ru.yandex.practicum.filmorate.services.film;

import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.services.FilmorateService;

import java.util.List;

public interface FilmLike extends FilmorateService<Film> {

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Film> showMostLikedFilms(Integer count);
}
