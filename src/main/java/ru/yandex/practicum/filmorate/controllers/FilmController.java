package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.services.CrudService;
import ru.yandex.practicum.filmorate.services.film.FilmLikeDbService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmLikeDbService filmLikeDbService;
    private final CrudService<Film> filmService;

    @GetMapping
    public List<Film> getAll() {
        return filmService.getAll();
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("/{filmId}")
    public Film getById(@PathVariable("filmId") Long filmId) {
        return filmService.findById(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film addLikeByUser(@PathVariable("filmId") Long filmId,
                              @PathVariable("userId") Long userId) {
        filmLikeDbService.addLike(filmId, userId);
        return filmService.findById(filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Boolean deleteLikeByUser(@PathVariable("filmId") Long filmId,
                                    @PathVariable("userId") Long userId) {
        return filmLikeDbService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopular(@RequestParam(value = "count", defaultValue = "10", required = false)
                                     @Positive Integer count) {
        return filmLikeDbService.showMostLikedFilms(count);
    }
}