package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.services.CrudService;
import ru.yandex.practicum.filmorate.services.film.FilmLikeService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmLikeService filmLikeService;
    private final CrudService<Film> filmService;

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
        filmLikeService.addLike(filmId, userId);
        return filmService.findById(filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Boolean deleteLikeByUser(@PathVariable("filmId") Long filmId,
                                    @PathVariable("userId") Long userId) {
        return filmLikeService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilm(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        try {
            if (count < 0) {
                count = 10;
            }
            return filmLikeService.showMostLikedFilms(count);
        } catch (NumberFormatException e) {
            e.getMessage();
        }
        return filmLikeService.showMostLikedFilms(10);
    }
}