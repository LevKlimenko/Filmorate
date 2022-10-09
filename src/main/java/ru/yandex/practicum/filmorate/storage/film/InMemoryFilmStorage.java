package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException.FilmIdException;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException.FilmNotFoundExceptin;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Integer, Film> films = new HashMap<>();
    private int id;
    private FilmService filmService;
    private Set<Film> compareFilm = new TreeSet<>((o1, o2) -> {
        if (o1.getLikesId().size() < o2.getLikesId().size()) {
            return 1;
        }
        if (o1.getLikesId().size() == o2.getLikesId().size()) {
            return o1.getId()-o2.getId();
        } else {
            return -1;
        }
    });
@Override
    public Set<Film> getCompareFilm() {
        return compareFilm;
    }

    @Override
    // @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    // @PostMapping
    public Film addFilm(Film film) {
        ++id;
        film.setId(id);
        films.put(film.getId(), film);
        compareFilm.add(film);
        log.info("Фильм {} сохранен с ID={}", film.getName(), film.getId());
        return film;
    }

    @Override
    //  @PutMapping
    public Film updateFilm( Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundExceptin("Фильма с ID=" + film.getId() + " в базе нет");
        }
        compareFilm.remove(film);
        films.put(film.getId(), film);
        compareFilm.add(film);
        log.info("Фильм {} обновлен", film.getName());
        return film;
    }

    @Override
    public Film findFilmById(Integer filmId) {
        if (!films.containsKey(filmId)) {
            throw new FilmNotFoundExceptin(String.format("Фильм № %d не найден", filmId));
        }
        return films.get(filmId);
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return films;
    }


}
