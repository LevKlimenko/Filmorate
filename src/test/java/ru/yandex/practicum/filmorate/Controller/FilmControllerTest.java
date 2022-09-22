package ru.yandex.practicum.filmorate.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException.FilmReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootApplication
public class FilmControllerTest {
    Film film;

    FilmController fc = new FilmController();


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
        fc.addFilm(film);
        assertEquals(1, fc.getAllFilms().size(), "Количество фильмов не совпадает");
    }

    @Test
    public void postTwoStandartFilm() {
        film = Film.builder()
                .name("testFilm")
                .description("testFilm")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .build();
        fc.addFilm(film);
        Film film2 = Film.builder()
                .name("testFilm2")
                .description("testFilm2")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(50)
                .build();
        fc.addFilm(film2);
        assertEquals(2, fc.getAllFilms().size(), "Количество фильмов не совпадает");
    }

    @Test
    public void postFailReleaseDate() {
        film = Film.builder()
                .name("testFilm")
                .description("testFilm")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(100)
                .build();
        assertEquals(LocalDate.of(1895, 12, 27), film.getReleaseDate(), "Время отличается от 1895-12-27");
        assertThrows(FilmReleaseDateException.class, () -> fc.addFilm(film), "Время релиза допустимо");
    }

    @Test
    public void postDayInDayReleaseDate() {
        film = Film.builder()
                .name("testFilm")
                .description("testFilm")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(100)
                .build();
        assertEquals(LocalDate.of(1895, 12, 28), film.getReleaseDate(), "Время отличается от 1895-12-28");
        fc.addFilm(film);
        assertEquals(1, fc.getAllFilms().size(), "Количество фильмов не совпадает");
    }

    @Test
    public void postDayAfterReleaseDate() {
        film = Film.builder()
                .name("testFilm")
                .description("testFilm")
                .releaseDate(LocalDate.of(1895, 12, 29))
                .duration(100)
                .build();
        assertEquals(LocalDate.of(1895, 12, 29), film.getReleaseDate(), "Время отличается от 1895-12-29");
        fc.addFilm(film);
        assertEquals(1, fc.getAllFilms().size(), "Количество фильмов не совпадает");
    }

    /**
     * Test PUT
     */

    @Test
    public void putNormalFilm() {
        film = Film.builder()
                .id(1)
                .name("testFilm")
                .description("testFilm")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .build();
        fc.addFilm(film);
        Film film2 = Film.builder()
                .id(1)
                .name("testFilm")
                .description("testAlreadyExistFilm")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(50)
                .build();
        fc.updateFilm(film2);
        assertEquals(1, fc.getAllFilms().size(), "Количество фильмов не совпадает");
    }

    @Test
    public void putFailReleaseDate() {
        film = Film.builder()
                .id(1)
                .name("testFilm")
                .description("testFilm")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .build();
        fc.addFilm(film);
        Film film2 = Film.builder()
                .id(1)
                .name("testFilm2")
                .description("testFilm")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(100)
                .build();
        assertEquals(LocalDate.of(1895, 12, 27), film2.getReleaseDate(), "Время отличается от 1895-12-27");
        assertThrows(FilmReleaseDateException.class, () -> fc.updateFilm(film2), "Время релиза допустимо");
    }

    @Test
    public void putDayInDayReleaseDate() {
        film = Film.builder()
                .id(1)
                .name("testFilm")
                .description("testFilm")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .build();
        fc.addFilm(film);
        Film film2 = Film.builder()
                .id(1)
                .name("testFilm2")
                .description("testFilm")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(100)
                .build();
        assertEquals(LocalDate.of(1895, 12, 28), film2.getReleaseDate(), "Время отличается от 1895-12-28");
        fc.updateFilm(film2);
        assertEquals(1, fc.getAllFilms().size(), "Количество фильмов не совпадает");
    }

    @Test
    public void putDayAfterReleaseDate() {
        film = Film.builder()
                .id(1)
                .name("testFilm")
                .description("testFilm")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .build();
        fc.addFilm(film);
        Film film2 = Film.builder()
                .id(1)
                .name("testFilm2")
                .description("testFilm2")
                .releaseDate(LocalDate.of(1895, 12, 29))
                .duration(100)
                .build();
        assertEquals(LocalDate.of(1895, 12, 29), film2.getReleaseDate(), "Время отличается от 1895-12-29");
        fc.updateFilm(film2);
        assertEquals(1, fc.getAllFilms().size(), "Количество фильмов не совпадает");
    }

}
