package ru.yandex.practicum.filmorate.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException.FilmReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootApplication
public class FilmControllerTest {
    Film film;

    //FilmController fc = new FilmController();
    InMemoryFilmStorage fs = new InMemoryFilmStorage();


    /**
     * Test POST
     */

    @Test
    public void postStandartFilm() {
        film = Film.builder()
                .name("testFilm")
                .description("testWithNameDiscr")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .build();
        fs.addFilm(film);
        assertEquals(1, fs.getAllFilms().size(), "Количество фильмов не совпадает");
    }

    @Test
    public void postTwoStandartFilm() {
        film = Film.builder()
                .name("testFilm")
                .description("testFilm")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .build();
        fs.addFilm(film);
        Film film2 = Film.builder()
                .name("testFilm2")
                .description("testFilm2")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(50)
                .build();
        fs.addFilm(film2);
        assertEquals(2, fs.getAllFilms().size(), "Количество фильмов не совпадает");
    }

    /**
     * Test PUT
     */

    @Test
    public void putNormalFilm() {
        film = Film.builder()
                .id((long) 1)
                .name("testFilm")
                .description("testFilm")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .build();
        fs.addFilm(film);
        Film film2 = Film.builder()
                .id((long) 1)
                .name("testFilm")
                .description("testAlreadyExistFilm")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(50)
                .build();
        fs.updateFilm(film2);
        assertEquals(1, fs.getAllFilms().size(), "Количество фильмов не совпадает");
    }

}
