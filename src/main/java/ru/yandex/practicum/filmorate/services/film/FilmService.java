package ru.yandex.practicum.filmorate.services.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.storages.film.FilmStorage;
import ru.yandex.practicum.filmorate.storages.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;


    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film add(Film film) {
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film findById(Long filmId) {
        return filmStorage.findById(filmId);
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.findById(filmId);
        if (userStorage.getAllId().contains(userId) ) {
            filmStorage.getCompare().remove(filmStorage.getMap().get(filmId));
            film.getLikesId().add(userId);
            filmStorage.getCompare().add(filmStorage.getMap().get(filmId));
        } else {
            throw new NotFoundException("Пользователя не существует");
        }
    }

    public void deleteLike(Long filmId, Long userId) {
        if (filmStorage.getAllId().contains(userId)) {
            Film film = filmStorage.findById(filmId);
            if (userStorage.getAllId().contains(userId)) {
                filmStorage.getCompare().remove(filmStorage.getMap().get(filmId));
                film.getLikesId().remove(userId);
                filmStorage.getCompare().add(filmStorage.getMap().get(filmId));
            } else {
                throw new NotFoundException("Пользователя не существует");
            }
        } else {
            throw new NotFoundException(String.format("Фильма с id=%d не существует", filmId));
        }
    }

    public List<Film> showMostLikedFilms(Integer count) {
        ArrayList<Film> sortFilm = new ArrayList<>(filmStorage.getCompare());
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

    public Map<Long, Film> getFilms() {
        return filmStorage.getMap();
    }

}
