package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException.FilmIdException;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Long, Film> films = new HashMap<>();
    private long id;
    private Set<Film> compareFilm = new TreeSet<>((o1, o2) -> {
        if (o1.getLikesId().size() < o2.getLikesId().size()) {
            return 1;
        }
        if (o1.getLikesId().size() == o2.getLikesId().size()) {
            return (int) (o1.getId() - o2.getId());
        } else {
            return -1;
        }
    });

    @Override
    public Set<Film> getCompareFilm() {
        return compareFilm;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film addFilm(Film film) {
        ++id;
        film.setId(id);
        films.put(film.getId(), film);
        compareFilm.add(film);
        log.info("Фильм {} сохранен с ID={}", film.getName(), film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (film.getId() > 0) {
            if (!films.containsKey(film.getId())) {
                throw new FilmNotFoundException(String.format("Фильма с ID=%d в базе нет", film.getId()));
            }
            compareFilm.remove(film);
            films.put(film.getId(), film);
            compareFilm.add(film);
            log.info("Фильм {} обновлен", film.getName());
            return film;
        } else {
            throw new FilmIdException("Для обновления фильма значение ID должно быть больше 0");
        }
    }

    @Override
    public Film findFilmById(Long filmId) {
        if (!films.containsKey(filmId)) {
            throw new FilmNotFoundException(String.format("Фильм № %d не найден", filmId));
        }
        return films.get(filmId);
    }

    @Override
    public Map<Long, Film> getFilms() {
        return films;
    }


}
