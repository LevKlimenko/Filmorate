package ru.yandex.practicum.filmorate.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.storages.film.FilmStorage;
import ru.yandex.practicum.filmorate.storages.film.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootApplication
public class FilmControllerTest {
    Film film;

    //FilmController fc = new FilmController();
    FilmStorage filmStorage = new InMemoryFilmStorage();


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
        filmStorage.create(film);
        assertEquals(1, filmStorage.getAll().size(), "Количество фильмов не совпадает");
    }

    @Test
    public void postTwoStandartFilm() {
        film = Film.builder()
                .name("testFilm")
                .description("testFilm")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .build();
        filmStorage.create(film);
        Film film2 = Film.builder()
                .name("testFilm2")
                .description("testFilm2")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(50)
                .build();
        filmStorage.create(film2);
        assertEquals(2, filmStorage.getAll().size(), "Количество фильмов не совпадает");
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
        filmStorage.create(film);
        Film film2 = Film.builder()
                .id((long) 1)
                .name("testFilm")
                .description("testAlreadyExistFilm")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(50)
                .build();
        filmStorage.update(film2);
        assertEquals(1, filmStorage.getAll().size(), "Количество фильмов не совпадает");
    }

}
