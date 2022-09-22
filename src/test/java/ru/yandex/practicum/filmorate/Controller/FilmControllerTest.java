package ru.yandex.practicum.filmorate.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

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
    public void postNoNameFilm() {
        film = Film.builder()
                .description("testWithoutNameDiscr")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .build();
        assertNull(film.getName());
        assertThrows(FilmNullNameException.class, () -> fc.addFilm(film), "Фильм передан с именем");
        assertEquals(0, fc.getAllFilms().size(), "not 0");
    }

    @Test
    public void postAlreadyYetFilm() {
        film = Film.builder()
                .name("testFilm")
                .description("testFilm")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .build();
        fc.addFilm(film);
        Film film2 = Film.builder()
                .name("testFilm")
                .description("testAlreadyExistFilm")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(50)
                .build();
        assertThrows(FilmAlreadyExistException.class, () -> fc.addFilm(film2), "Имена фильмов различаются");
    }

    @Test
    public void postBigSizeDescription() {
        film = Film.builder()
                .name("testFilm")
                .description("TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest" +
                        "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest" +
                        "TestTestTestTestTestTestTestTest203")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .build();
        assertEquals(203, film.getDescription().length(), "Длина описания отличается от 203");
        assertThrows(FilmBadDescriptionException.class, () -> fc.addFilm(film), "Длина Descr допустима");
    }

    @Test
    public void postNormalSizeDescription() {
        film = Film.builder()
                .name("testFilm")
                .description("TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest" +
                        "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest" +
                        "TestTestTestTestTestTestTest199")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .build();
        fc.addFilm(film);
        assertEquals(199, film.getDescription().length(), "Длина описания отличается от 199");
        assertEquals(1, fc.getAllFilms().size(), "Количество фильмов не совпадает");
    }

    @Test
    public void post200CharDescription() {
        film = Film.builder()
                .name("testFilm")
                .description("TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest" +
                        "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest" +
                        "TestTestTestTestTestTestTestt200")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .build();
        fc.addFilm(film);
        assertEquals(200, film.getDescription().length(), "Длина описания отличается от 200");
        assertEquals(1, fc.getAllFilms().size(), "Количество фильмов не совпадает");
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

    @Test
    public void postNegativeDuration() {
        film = Film.builder()
                .name("testFilm")
                .description("testFilm")
                .releaseDate(LocalDate.of(1895, 12, 29))
                .duration(-1)
                .build();
        assertEquals(-1, film.getDuration(), "Продолжительность отличается от -1");
        assertThrows(FilmDurationException.class, () -> fc.addFilm(film), "Длительность фильма допустима");
    }

    @Test
    public void postNullDuration() {
        film = Film.builder()
                .name("testFilm")
                .description("testFilm")
                .releaseDate(LocalDate.of(1895, 12, 29))
                .duration(0)
                .build();
        assertEquals(0, film.getDuration(), "Продолжительность отличается от 0");
        fc.addFilm(film);
        assertEquals(1, fc.getAllFilms().size(), "Количество фильмов не совпадает");
    }

    @Test
    public void postPositiveDuration() {
        film = Film.builder()
                .name("testFilm")
                .description("testFilm")
                .releaseDate(LocalDate.of(1895, 12, 29))
                .duration(1)
                .build();
        assertEquals(1, film.getDuration(), "Продолжительность отличается от 1");
        fc.addFilm(film);
        assertEquals(1, fc.getAllFilms().size(), "Количество фильмов не совпадает");
    }

    /**
     * Test PUT
     */
    @Test
    public void putNoNameFilm() {
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
                .description("testWithoutNameDiscr")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .build();
        assertNull(film2.getName());
        assertThrows(FilmNullNameException.class, () -> fc.updateFilm(film2), "Фильм передан с именем");
        assertEquals(1, fc.getAllFilms().size(), "Количество фильмов отлично от 1");
    }



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
        assertEquals(1,fc.getAllFilms().size(),"Количество фильмов не совпадает");

    }

    @Test
    public void putBigSizeDescription() {
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
                .description("TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest" +
                        "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest" +
                        "TestTestTestTestTestTestTestTest203")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .build();
        assertEquals(203, film2.getDescription().length(), "Длина описания отличается от 203");
        assertThrows(FilmBadDescriptionException.class, () -> fc.updateFilm(film2), "Длина Descr допустима");
    }

    @Test
    public void putNormalSizeDescription() {
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
                .description("TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest" +
                        "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest" +
                        "TestTestTestTestTestTestTest199")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .build();
        fc.updateFilm(film2);
        assertEquals(199, film2.getDescription().length(), "Длина описания отличается от 199");
        assertEquals(1, fc.getAllFilms().size(), "Количество фильмов не совпадает");
    }

    @Test
    public void put200CharDescription() {
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
                .description("TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest" +
                        "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest" +
                        "TestTestTestTestTestTestTestt200")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .build();
        fc.updateFilm(film2);
        assertEquals(200, film2.getDescription().length(), "Длина описания отличается от 200");
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

    @Test
    public void putNegativeDuration() {
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
                .releaseDate(LocalDate.of(1895, 12, 29))
                .duration(-1)
                .build();
        assertEquals(-1, film2.getDuration(), "Продолжительность отличается от -1");
        assertThrows(FilmDurationException.class, () -> fc.updateFilm(film2), "Длительность фильма допустима");
    }

    @Test
    public void putNullDuration() {
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
                .description("testFilm")
                .releaseDate(LocalDate.of(1895, 12, 29))
                .duration(0)
                .build();
        assertEquals(0, film2.getDuration(), "Продолжительность отличается от 0");
        fc.updateFilm(film2);
        assertEquals(1, fc.getAllFilms().size(), "Количество фильмов не совпадает");
    }

    @Test
    public void putPositiveDuration() {
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
                .description("testFilm")
                .releaseDate(LocalDate.of(1895, 12, 29))
                .duration(1)
                .build();
        assertEquals(1, film2.getDuration(), "Продолжительность отличается от 1");
        fc.updateFilm(film2);
        assertEquals(1, fc.getAllFilms().size(), "Количество фильмов не совпадает");
    }
}
