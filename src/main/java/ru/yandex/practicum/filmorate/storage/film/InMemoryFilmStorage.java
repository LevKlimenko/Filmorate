package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException.FilmIdException;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException.FilmNotFoundExceptin;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage{

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static final LocalDate MIN_DATE_FOR_RELEASE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();
    private int id;

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
        log.info("Фильм {} сохранен с ID={}", film.getName(), film.getId());
        return film;
    }

    @Override
  //  @PutMapping
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmIdException("Фильма с ID=" + film.getId() + " в базе нет");
        }
        films.put(film.getId(), film);
        log.info("Фильм {} обновлен", film.getName());
        return film;
    }

    @Override
    public Film findFilmById(Integer filmId){
       if (!films.containsKey(filmId)){
           throw new FilmNotFoundExceptin(String.format("Фильм № %d не найден", filmId));
          }
        return films.get(filmId);
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return films;
    }


}
