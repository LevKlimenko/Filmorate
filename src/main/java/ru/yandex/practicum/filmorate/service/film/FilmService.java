package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.userServiceException.UserNullException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Создайте FilmService, который будет отвечать за операции с фильмами,
 * — добавление и удаление лайка, вывод 10 наиболее популярных фильмов по количеству лайков.
 * Пусть пока каждый пользователь может поставить лайк фильму только один раз.
 **/
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    //InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
    private Set<Film> compareFilm = new TreeSet<>((o1, o2) -> {
        if (o1.getLikesId().size() > o2.getLikesId().size()) {
            return 1;
        }
        if (o1.getLikesId().size() == o2.getLikesId().size()) {
            return 0;
        } else {
            return -1;
        }
    });

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.findFilmById(filmId);
        if (userStorage.getUsers().containsKey(userId)) {
            film.getLikesId().add(userId);
            compareFilm.add(filmStorage.getFilms().get(filmId));
        } else {
            throw new UserNullException("Пользователя не существует");
        }
    }

    public void deleteLike(User user, Film film) {
        film.getLikesId().remove(user.getId());
        compareFilm.remove(film);
    }

    public List<String> showMostLikedFilms(int count) {
        ArrayList<Film> sortFilm = new ArrayList<>(compareFilm);
        List<String> tenLikedFilms = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            tenLikedFilms.add(sortFilm.get(i).getName());
        }
        return tenLikedFilms;
    }


}
