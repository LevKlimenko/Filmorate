package ru.yandex.practicum.filmorate.services.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.storages.film.FilmStorage;
import ru.yandex.practicum.filmorate.storages.user.UserDbStorage;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class FilmDbService implements FilmLikeService {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmStorage filmStorage;
    private final UserDbStorage userStorage;

    @Autowired
    public FilmDbService(@Qualifier("filmDbStorage") FilmStorage filmStorage, UserDbStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public void addLike(Long filmId, Long userId) {

    }

    @Override
    public void deleteLike(Long filmId, Long userId) {

    }

    @Override
    public List<Film> showMostLikedFilms(Integer count) {
        return null;
    }

    @Override
    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public Film create(Film film) {
        return filmStorage.create(film);
    }

    @Override
    public Film update(Film film) {
        return filmStorage.update(film);
    }

    @Override
    public Film findById(Long id) {
        return filmStorage.findById(id);
    }

    @Override
    public Map<Long, Film> getMap() {
        return filmStorage.getMap();
    }
}
