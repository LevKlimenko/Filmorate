package ru.yandex.practicum.filmorate.services.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.storages.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storages.film.FilmStorage;
import ru.yandex.practicum.filmorate.storages.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storages.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class FilmService implements FilmLikeService {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmStorage filmStorage;
    private final UserDbStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, UserDbStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
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
    public void addLike(Long filmId, Long userId) {
        if (filmStorage.isExist(filmId)) {
            Film film = filmStorage.findById(filmId);
            if (userStorage.getMap().containsKey(userId)) {
                filmStorage.getCompare().remove(filmStorage.findById(filmId));
                film.getLikesId().add(userId);
                log.debug("Пользователь с id {} добавил лайк к фильму с id {}.", userId, filmId);
                filmStorage.getCompare().add(filmStorage.findById(filmId));
            } else {
                throw new NotFoundException("Пользователя не существует");
            }
        }
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        if (filmStorage.isExist(filmId)) {
            Film film = filmStorage.findById(filmId);
            if (userStorage.getMap().containsKey(userId)) {
                filmStorage.getCompare().remove(filmStorage.findById(filmId));
                film.getLikesId().remove(userId);
                log.debug("Пользователь с id {} удалил лайк с фильма с id {}.", userId, filmId);
                filmStorage.getCompare().add(filmStorage.findById(filmId));
            } else {
                throw new NotFoundException("Пользователя не существует");
            }
        } else {
            throw new NotFoundException(String.format("Фильма с id=%d не существует", filmId));
        }
    }

    @Override
    public List<Film> showMostLikedFilms(Integer count) {
        List<Film> sortFilm = new ArrayList<>(filmStorage.getCompare());
        List<Film> likedFilms = new ArrayList<>();
        if (count > sortFilm.size()) {
            count = sortFilm.size();
        }
        if (sortFilm.size() > 0) {
            for (int i = 0; i < count; i++) {
                likedFilms.add(sortFilm.get(i));
            }
        }
        return likedFilms;
    }

    @Override
    public Map<Long, Film> getMap() {
        return filmStorage.getMap();
    }
}
