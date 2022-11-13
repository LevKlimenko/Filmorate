package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.services.film.FilmDbService;
import ru.yandex.practicum.filmorate.services.film.FilmLikeService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Validated
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmLikeService filmService;

    @Autowired
    public FilmController(FilmDbService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmService.getAll();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("/{filmId}")
    public Film findFilm(@PathVariable("filmId") Long filmId) {
        return filmService.findById(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film addLikeFilmByUser(@PathVariable("filmId") Long filmId,
                                  @PathVariable("userId") Long userId) {
        filmService.addLike(filmId, userId);
        return filmService.findById(filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Boolean deleteLikeByUser(@PathVariable("filmId") Long filmId,
                                    @PathVariable("userId") Long userId) {

        return filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilm(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        try {
            if (count < 0) {
                count = 10;
            }
            return filmService.showMostLikedFilms(count);
        } catch (NumberFormatException e) {
            e.getMessage();
        }
        return filmService.showMostLikedFilms(10);
    }
}
