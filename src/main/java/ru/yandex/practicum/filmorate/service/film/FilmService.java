package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException.FilmIdException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException.UserIdException;
import ru.yandex.practicum.filmorate.exceptions.userServiceException.UserNullException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

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

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film findFilmById(Long filmId) {
        return filmStorage.findFilmById(filmId);
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.findFilmById(filmId);
        if (userStorage.getUsers().containsKey(userId)) {
            filmStorage.getCompareFilm().remove(filmStorage.getFilms().get(filmId));
            film.getLikesId().add(userId);
            filmStorage.getCompareFilm().add(filmStorage.getFilms().get(filmId));
        } else {
            throw new UserNullException("Пользователя не существует");
        }
    }

    public void deleteLike(Long filmId, Long userId) {
        if (filmStorage.getFilms().containsKey(filmId)) {
            Film film = filmStorage.findFilmById(filmId);
            if (userStorage.getUsers().containsKey(userId)) {
                filmStorage.getCompareFilm().remove(filmStorage.getFilms().get(filmId));
                film.getLikesId().remove(userId);
                filmStorage.getCompareFilm().add(filmStorage.getFilms().get(filmId));
            } else {
                throw new UserIdException("Пользователя не существует");
            }
        } else {
            throw new FilmIdException(String.format("Фильма с id=%d не существует", filmId));
        }
    }

    public List<Film> showMostLikedFilms(Integer count) {
        ArrayList<Film> sortFilm = new ArrayList<>(filmStorage.getCompareFilm());
        List<Film> likedFilms = new ArrayList<>();
        if (count < 0) {
            throw new IllegalStateException("Значение показываемых фильмов count должно быть больше 0");
        }
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
        return filmStorage.getFilms();
    }

}
