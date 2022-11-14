package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.film.FilmDbService;
import ru.yandex.practicum.filmorate.services.genre.GenreService;
import ru.yandex.practicum.filmorate.services.mpa.MpaService;
import ru.yandex.practicum.filmorate.services.user.UserDbService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Validated
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreControllerTest {
    private final JdbcTemplate jdbcTemplate;
    private final FilmDbService filmService;
    private final MpaService mpaService;
    private final GenreService genreService;
    private final UserDbService userDbService;
    Film film;
    User user;

    @BeforeEach
    void initEach() {
        jdbcTemplate.update("MERGE INTO MPA (id, name)" +
                "VALUES (1, 'G'), (2, 'PG'), (3, 'PG-13'), (4, 'R'), (5, 'NC-17')");
        jdbcTemplate.update("MERGE INTO GENRES (id, name) " +
                "VALUES (1, 'Комедия'), (2, 'Драма'), (3, 'Мультфильм'), (4, 'Триллер'), (5, 'Документальный')," +
                " (6, 'Боевик')");
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM LIKES");
        jdbcTemplate.update("DELETE FROM FILM_GENRE");
        jdbcTemplate.update("DELETE FROM FRIENDSHIP");
        jdbcTemplate.update("DELETE FROM USERS");
        jdbcTemplate.update("DELETE FROM FILMS");
        jdbcTemplate.update("ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE FILMS ALTER COLUMN ID RESTART WITH 1");

    }

    /**
     * Test POST
     */

    @Test
    public void getAllGenre() {
        Collection<Genre> allGenres = genreService.getAll();
        assertEquals(6, allGenres.size(), "Количество Genres не совпадает");
    }

    @Test
    public void getGoodIdGenre() {
        Genre genre = genreService.findById(1L);
        assertEquals("Комедия", genre.getName(), "Имя Genre не совпадает");
        assertEquals(1, genre.getId(), "ID Genre не совпадает");
    }

    @Test
    public void getBadIdGenre() {
        assertThrows(NotFoundException.class, () -> genreService.findById(10L), "Genre найден");
    }

    @Test
    public void postNormalFilmWithOneGenre() {
        List<Genre> genreList = new ArrayList<>();
        genreList.add(genreService.findById(1L));
        film = Film.builder()
                .id((long) 1)
                .name("testFilm")
                .description("testUpdateGenreFilm")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(50)
                .rate(4)
                .mpa(mpaService.findById(1L))
                .genres(genreList)
                .build();
        filmService.create(film);
        assertEquals(1, filmService.getAll().size(), "Количество фильмов не совпадает");
        assertEquals("Комедия", filmService.findById(1L).getGenres().get(0).getName(), "Названия жанров не совпадают ");
    }

    @Test
    public void postNormalFilmWithTwoGenre() {
        List<Genre> genreList = new ArrayList<>();
        genreList.add(genreService.findById(1L));
        genreList.add(genreService.findById(2L));
        film = Film.builder()
                .id((long) 1)
                .name("testFilm")
                .description("testUpdateGenreFilm")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(50)
                .rate(4)
                .mpa(mpaService.findById(1L))
                .genres(genreList)
                .build();
        filmService.create(film);
        assertEquals(1, filmService.getAll().size(), "Количество фильмов не совпадает");
        assertEquals(2, filmService.findById(1L).getGenres().size(), "Количество жанров не совпадают ");
        assertEquals("Драма", filmService.findById(1L).getGenres().get(1).getName(), "Названия жанров не совпадают ");
    }

    @Test
    public void putNormalFilmWithGenre() {
        film = Film.builder()
                .id((long) 1)
                .name("testFilm")
                .description("testFilm")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .rate(4)
                .mpa(mpaService.findById(1L))
                .build();
        filmService.create(film);
        List<Genre> genreList = new ArrayList<>();
        genreList.add(genreService.findById(1L));
        Film film2 = Film.builder()
                .id((long) 1)
                .name("testFilm")
                .description("testUpdateGenreFilm")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(50)
                .rate(4)
                .mpa(mpaService.findById(1L))
                .genres(genreList)
                .build();
        filmService.update(film2);
        assertEquals(1, filmService.getAll().size(), "Количество фильмов не совпадает");
        assertEquals("Комедия", filmService.findById(1L).getGenres().get(0).getName(), "Названия жанров не совпадают ");
    }

    @Test
    public void putNormalFilmWithTwoGenre() {
        List<Genre> genreList = new ArrayList<>();
        genreList.add(genreService.findById(1L));
        genreList.add(genreService.findById(2L));
        film = Film.builder()
                .id((long) 1)
                .name("testFilm")
                .description("testUpdateGenreFilm")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(50)
                .rate(4)
                .mpa(mpaService.findById(1L))
                .genres(genreList)
                .build();
        filmService.create(film);
        genreList.add(genreService.findById(3L));
        Film film2 = Film.builder()
                .id((long) 1)
                .name("testFilm")
                .description("testDeleteGenreFilm")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(50)
                .rate(4)
                .mpa(mpaService.findById(1L))
                .genres(genreList)
                .build();
        filmService.update(film2);
        assertEquals(1, filmService.getAll().size(), "Количество фильмов не совпадает");
        assertEquals(3, filmService.findById(1L).getGenres().size(), "Количество жанров не совпадают ");
        assertEquals("Мультфильм", filmService.findById(1L).getGenres().get(2).getName(), "Названия жанров не совпадают ");
    }

    @Test
    public void deleteGenreFromNormalFilm() {
        film = Film.builder()
                .id((long) 1)
                .name("testFilm")
                .description("testFilm")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .rate(4)
                .mpa(mpaService.findById(1L))
                .build();
        filmService.create(film);
        List<Genre> genreList = new ArrayList<>();
        genreList.add(genreService.findById(1L));
        Film film2 = Film.builder()
                .id((long) 1)
                .name("testFilm")
                .description("testDeleteGenreFilm")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(50)
                .rate(4)
                .mpa(mpaService.findById(1L))
                .genres(genreList)
                .build();
        filmService.update(film2);
        genreList.remove(0);
        Film film3 = Film.builder()
                .id((long) 1)
                .name("testFilm")
                .description("testDeleteGenreFilm")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(50)
                .rate(4)
                .mpa(mpaService.findById(1L))
                .genres(genreList)
                .build();
        filmService.update(film3);
        assertEquals(1, filmService.getAll().size(), "Количество фильмов не совпадает");
        assertEquals(0, filmService.findById(1L).getGenres().size(), "Список жанров не совпадает ");
    }

    @Test
    public void deleteGenreFilmWithThreeGenre() {
        List<Genre> genreList = new ArrayList<>();
        genreList.add(genreService.findById(1L));
        genreList.add(genreService.findById(2L));
        genreList.add(genreService.findById(3L));
        film = Film.builder()
                .id((long) 1)
                .name("testFilm")
                .description("testUpdateGenreFilm")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(50)
                .rate(4)
                .mpa(mpaService.findById(1L))
                .genres(genreList)
                .build();
        filmService.create(film);
        genreList.clear();
        Film film2 = Film.builder()
                .id((long) 1)
                .name("testFilm")
                .description("testDeleteGenreFilm")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(50)
                .rate(4)
                .mpa(mpaService.findById(1L))
                .genres(genreList)
                .build();
        filmService.update(film2);
        assertEquals(1, filmService.getAll().size(), "Количество фильмов не совпадает");
        assertEquals(0, filmService.findById(1L).getGenres().size(), "Количество жанров не совпадают ");
    }
}