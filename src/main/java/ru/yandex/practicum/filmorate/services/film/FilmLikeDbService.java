package ru.yandex.practicum.filmorate.services.film;

import ru.yandex.practicum.filmorate.models.Film;

import java.util.List;

public interface FilmLikeDbService {

    boolean addLike(Long filmId, Long userId);

    boolean deleteLike(Long filmId, Long userId);

    List<Film> showMostLikedFilms(Integer count);
}
