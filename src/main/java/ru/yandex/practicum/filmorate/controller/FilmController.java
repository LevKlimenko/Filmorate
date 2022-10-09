package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException.FilmIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**     (С помощью аннотации @PathVariable добавьте возможность получать
        каждый фильм и данные о пользователях по их уникальному идентификатору: GET .../users/{id}.
 PUT /films/{id}/like/{userId} — пользователь ставит лайк фильму.
 DELETE /films/{id}/like/{userId} — пользователь удаляет лайк.
 GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков.
                                    Если значение параметра count не задано, верните первые 10.


 **/
@Validated
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public FilmController(FilmService filmService, FilmStorage filmStorage,
                          UserService userService, UserStorage userStorage) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
        this.userService=userService;
        this.userStorage=userStorage;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
      return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
       return filmService.updateFilm(film);
    }

    @GetMapping("/{filmId}")
    public Film findFilm(@PathVariable("filmId") Integer filmId){
        return filmService.findFilmById(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film addLikeFilmByUser(@PathVariable("filmId") Integer filmId,
                               @PathVariable("userId") Integer userId){
        filmService.addLike(filmId,userId);
        return filmStorage.getFilms().get(filmId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film deleteLikeByUser(@PathVariable("filmId") Integer filmId,
                                 @PathVariable("userId") Integer userId){
        filmService.deleteLike(filmId,userId);
        return filmStorage.getFilms().get(filmId);
    }
    @GetMapping("/popular")
    public List<Film> getMostPopularFilm(
            @RequestParam (defaultValue = "10", required = false) String count){
        int filmCount=Integer.parseInt(count);
        if( filmCount <=0){
            throw new IllegalStateException("Ошибка ввода");
        }
        return  filmService.showMostLikedFilms(filmCount);
    }


}
