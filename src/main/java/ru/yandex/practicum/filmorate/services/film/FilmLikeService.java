package ru.yandex.practicum.filmorate.services.film;

import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.services.CrudService;

import java.util.List;

public interface FilmLikeService extends CrudService<Film> {

    void addLike(Long filmId, Long userId);

    boolean deleteLike(Long filmId, Long userId);

    List<Film> showMostLikedFilms(Integer count);
}