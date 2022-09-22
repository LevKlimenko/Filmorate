package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final HashMap<Integer, Film> films = new HashMap<>();
    private final HashMap<Integer, String> nameFilmWithID = new HashMap<>();
    private int id;

    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        if (film.getName()==null) {
            throw new FilmNullNameException("Отсутствует название фильма");
        }
        if (films.values().toString().contains(film.getName())) {
            throw new FilmAlreadyExistException("Фильм " + film.getName() + " уже есть в базе");
        }
        if (film.getDescription().length() > 200) {
            throw new FilmBadDescriptionException("Максимальная длина описания - 200 символов. Длина описания " +
                    film.getDescription().length() + " символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new FilmReleaseDateException("Дата релиза должна быть не раньше 28 декабря 1895 года. " +
                    "Текущее значение" + film.getReleaseDate());
        }
        if (film.getDuration()<0) {
            throw new FilmDurationException("Продолжительность фильма должна быть положительной. " +
                    "Текущее значение: " + film.getDuration() + " минут");
        }
        id = ++id;
        film.setId(id);
        films.put(film.getId(), film);
        log.info("Фильм {} сохранен с ID={}", film.getName(), film.getId());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (film.getName()==null) {
            throw new FilmNullNameException("Отсутствует название фильма");
        }
        if (!films.containsKey(film.getId())){
            throw new FilmIdException("Фильма с ID=" + film.getId() + " в базе нет");
        }
        if (film.getDescription().length() > 200) {
            throw new FilmBadDescriptionException("Максимальная длина описания - 200 символов. Длина описания " +
                    film.getDescription().length() + " символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new FilmReleaseDateException("Дата релиза должна быть не раньше 28 декабря 1895 года. " +
                    "Текущее значение " + film.getReleaseDate());
        }
        if (film.getDuration()<0) {
            throw new FilmDurationException("Продолжительность фильма должна быть положительной. " +
                    "Текущее значение: " + film.getDuration() + " минут");
        }
        films.put(film.getId(), film);
        log.info("Фильм {} обновлен", film.getName());
        return film;
    }


}
