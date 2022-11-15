package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.film.FilmLikeService;
import ru.yandex.practicum.filmorate.services.genre.GenreService;
import ru.yandex.practicum.filmorate.services.mpa.MpaService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Validated
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmControllerTest {
    private final JdbcTemplate jdbcTemplate;
    private final FilmLikeService filmLikeService;
    private final CrudService<Film> filmService;
    private final MpaService mpaService;
    private final GenreService genreService;
    private final CrudService<User> userDbService;
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
    public void postStandardFilm() {
        film = Film.builder()
                .name("testFilm")
                .description("testWithNameDiscr")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .rate(4)
                .mpa(mpaService.findById(1L))
                .build();
        filmService.create(film);
        assertEquals(1, filmService.getAll().size(), "Количество фильмов не совпадает");
        assertThrows(NotFoundException.class, () -> filmService.findById(2L), "Фильм найден");
        assertThrows(NotFoundException.class, () -> filmService.findById(-2L), "Фильм найден");
    }

    @Test
    public void postTwoStandardFilm() {
        film = Film.builder()
                .name("testFilm")
                .description("testFilm")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .rate(4)
                .mpa(mpaService.findById(1L))
                .build();
        filmService.create(film);
        Film film2 = Film.builder()
                .name("testFilm2")
                .description("testFilm2")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(50)
                .rate(4)
                .mpa(mpaService.findById(1L))
                .build();
        filmService.create(film2);
        assertEquals(2, filmService.getAll().size(), "Количество фильмов не совпадает");
        assertThrows(NotFoundException.class, () -> filmService.findById(3L), "Фильм найден");
        assertThrows(NotFoundException.class, () -> filmService.findById(-3L), "Фильм найден");
    }

    @Test
    public void postFailNameFilm() {
        film = Film.builder()
                .name("")
                .description("testWithNameDiscr")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .rate(4)
                .mpa(mpaService.findById(1L))
                .build();
        assertThrows(DataIntegrityViolationException.class, () -> filmService.create(film), "Фильм добавлен");
    }

    @Test
    public void postNullNameFilm() {
        film = Film.builder()
                .description("testWithNameDiscr")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .rate(4)
                .mpa(mpaService.findById(1L))
                .build();
        assertThrows(DataIntegrityViolationException.class, () -> filmService.create(film), "Фильм добавлен");
    }

    @Test
    public void postFailDescriptionFilm() {
        film = Film.builder()
                .name("testFilm")
                .description("ThisTextHave22Symbols.ThisTextHave22Symbols.ThisTextHave22Symbols.ThisTextHave22Symbols." +
                        "ThisTextHave22Symbols.ThisTextHave22Symbols.ThisTextHave22Symbols." +
                        "ThisTextHave22Symbols.ThisTextHave22Symbols....")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .rate(4)
                .mpa(mpaService.findById(1L))
                .build();
        assertEquals(201, film.getDescription().length(), "Длина Description не совпадает");
        assertThrows(DataIntegrityViolationException.class, () -> filmService.create(film), "Фильм добавлен");
    }

    @Test
    public void postReleaseDateFailFilm() {
        film = Film.builder()
                .name("testFilm")
                .description("Date release is 1895-12-27")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(100)
                .rate(4)
                .mpa(mpaService.findById(1L))
                .build();
        assertThrows(DataIntegrityViolationException.class, () -> filmService.create(film), "Фильм добавлен");
    }

    @Test
    public void postBadDurationFilm() {
        film = Film.builder()
                .name("testFilm")
                .description("Duration <0")
                .releaseDate(LocalDate.of(1995, 12, 27))
                .duration(-1)
                .rate(4)
                .mpa(mpaService.findById(1L))
                .build();
        assertThrows(DataIntegrityViolationException.class, () -> filmService.create(film), "Фильм добавлен");
    }

    @Test
    public void postWithoutMpaFilm() {
        film = Film.builder()
                .name("testFilm")
                .description("Not Found Mpa")
                .releaseDate(LocalDate.of(1995, 12, 27))
                .duration(100)
                .rate(4)
                .mpa(null)
                .build();
        assertThrows(DataIntegrityViolationException.class, () -> filmService.create(film), "Фильм добавлен");
    }

    @Test
    public void getPopularFilm() {
        film = Film.builder()
                .name("testFilm")
                .description("testWithNameDiscr")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .rate(4)
                .mpa(mpaService.findById(1L))
                .genres(new ArrayList<>())
                .likesId(new ArrayList<>())
                .build();
        filmService.create(film);
        assertEquals(1, filmLikeService.showMostLikedFilms(10).size(), "Количество фильмов не совпадает");
        assertEquals(film, filmLikeService.showMostLikedFilms(10).get(0), "Фильмы различаются");
    }

    @Test
    public void postFilmWithGenres() {
        film = Film.builder()
                .name("testFilm")
                .description("testWithNameDiscr")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .rate(4)
                .mpa(mpaService.findById(3L))
                .genres(Arrays.asList(genreService.findById(1L)))
                .likesId(new ArrayList<>())
                .build();
        filmService.create(film);
        assertEquals(1, filmService.getAll().size(), "Количество фильмов не совпадает");
        assertEquals(film, filmService.findById(1L), "Фильмы различаются");
        assertThrows(NotFoundException.class, () -> filmService.findById(2L), "Фильм найден");
    }

    /**
     * Test LIKES
     */

    @Test
    public void addLikeForFilmWithGenres() {
        film = Film.builder()
                .name("testFilm")
                .description("testWithNameDiscr")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .rate(4)
                .mpa(mpaService.findById(3L))
                .genres(Arrays.asList(genreService.findById(1L)))
                .build();
        filmService.create(film);
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userDbService.create(user);
        filmLikeService.addLike(film.getId(), user.getId());
        assertEquals(1, filmService.getAll().size(), "Количество фильмов не совпадает");
        assertEquals(1, filmService.findById(1L).getLikesId().size(), "Количество лайков отличается");
        assertEquals(user.getId(), filmService.findById(1L).getLikesId().get(0), "Количество лайков отличается");
    }

    @Test
    public void getPopularFilmWithLikeAndWithout() {
        film = Film.builder()
                .name("testFilm1")
                .description("testWithNameDiscr1")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .rate(4)
                .mpa(mpaService.findById(3L))
                .genres(Arrays.asList(genreService.findById(1L)))
                .likesId(new ArrayList<>())
                .build();
        filmService.create(film);
        assertEquals(1, filmLikeService.showMostLikedFilms(10).size(), "Количество фильмов не совпадает");
        assertEquals(filmService.findById(1L), filmLikeService.showMostLikedFilms(10).get(0), "Фильмы различаются");
        Film film2 = Film.builder()
                .name("testFilm2")
                .description("testWithNameDiscr2")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .rate(4)
                .mpa(mpaService.findById(3L))
                .genres(Arrays.asList(genreService.findById(1L)))
                .likesId(new ArrayList<>())
                .build();
        filmService.create(film2);
        assertEquals(2, filmLikeService.showMostLikedFilms(10).size(), "Количество фильмов не совпадает");
        assertEquals(filmService.findById(1L), filmLikeService.showMostLikedFilms(10).get(0), "Фильмы 1 различаются");
        assertEquals(filmService.findById(2L), filmLikeService.showMostLikedFilms(10).get(1), "Фильмы 2 различаются");
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userDbService.create(user);
        filmLikeService.addLike(film2.getId(), user.getId());
        assertEquals(2, filmLikeService.showMostLikedFilms(10).size(), "Количество фильмов не совпадает");
        assertEquals(filmService.findById(2L), filmLikeService.showMostLikedFilms(10).get(0), "Фильмы 2 различаются");
        assertEquals(filmService.findById(1L), filmLikeService.showMostLikedFilms(10).get(1), "Фильмы 1 различаются");
    }

    @Test
    public void deleteFilmLikeAndGetPopular() {
        film = Film.builder()
                .name("testFilm1")
                .description("testWithNameDiscr1")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .rate(4)
                .mpa(mpaService.findById(3L))
                .genres(Arrays.asList(genreService.findById(1L)))
                .likesId(new ArrayList<>())
                .build();
        filmService.create(film);
        Film film2 = Film.builder()
                .name("testFilm2")
                .description("testWithNameDiscr2")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .rate(4)
                .mpa(mpaService.findById(3L))
                .genres(Arrays.asList(genreService.findById(1L)))
                .likesId(new ArrayList<>())
                .build();
        filmService.create(film2);
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userDbService.create(user);
        filmLikeService.addLike(film2.getId(), user.getId());
        assertEquals(2, filmLikeService.showMostLikedFilms(10).size(), "Количество фильмов не совпадает");
        assertEquals(filmService.findById(2L), filmLikeService.showMostLikedFilms(10).get(0), "Фильмы 2 различаются");
        assertEquals(filmService.findById(1L), filmLikeService.showMostLikedFilms(10).get(1), "Фильмы 1 различаются");
        filmLikeService.deleteLike(film2.getId(), user.getId());
        assertEquals(2, filmLikeService.showMostLikedFilms(10).size(), "Количество фильмов не совпадает");
        assertEquals(filmService.findById(1L), filmLikeService.showMostLikedFilms(10).get(0), "Фильмы 1 различаются");
        assertEquals(filmService.findById(2L), filmLikeService.showMostLikedFilms(10).get(1), "Фильмы 2 различаются");
    }

    @Test
    public void deleteFilmLikeWithoutLikeOrFailUserOrFilm() {
        film = Film.builder()
                .name("testFilm1")
                .description("testWithNameDiscr1")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .rate(4)
                .mpa(mpaService.findById(3L))
                .genres(Arrays.asList(genreService.findById(1L)))
                .likesId(new ArrayList<>())
                .build();
        filmService.create(film);
        Film film2 = Film.builder()
                .name("testFilm2")
                .description("testWithNameDiscr2")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .rate(4)
                .mpa(mpaService.findById(3L))
                .genres(Arrays.asList(genreService.findById(1L)))
                .likesId(new ArrayList<>())
                .build();
        filmService.create(film2);
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userDbService.create(user);
        filmLikeService.addLike(film2.getId(), user.getId());
        assertThrows(NotFoundException.class, () -> filmLikeService.deleteLike(2L, 2L), "Лайк удален");
        assertThrows(NotFoundException.class, () -> filmLikeService.deleteLike(3L, 2L), "Фильм найден");
        assertThrows(NotFoundException.class, () -> filmLikeService.deleteLike(2L, 3L), "Пользователь найден");
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
                .rate(4)
                .mpa(mpaService.findById(1L))
                .build();
        filmService.create(film);
        Film film2 = Film.builder()
                .id((long) 1)
                .name("testFilm")
                .description("testUpdateDescFilm")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(50)
                .rate(4)
                .mpa(mpaService.findById(1L))
                .build();
        filmService.update(film2);
        assertEquals(1, filmService.getAll().size(), "Количество фильмов не совпадает");
        assertEquals("testUpdateDescFilm", filmService.findById(1L).getDescription(), "Фильмы не совпадают");
    }

    @Test
    public void putBadIdFilm() {
        film = Film.builder()
                .name("testFilm")
                .description("testFilm")
                .releaseDate(LocalDate.of(2022, 5, 5))
                .duration(100)
                .rate(4)
                .mpa(mpaService.findById(1L))
                .build();
        filmService.create(film);
        Film film2 = Film.builder()
                .id((long) -1)
                .name("testFilm")
                .description("testBadID -1")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(50)
                .rate(4)
                .mpa(mpaService.findById(1L))
                .build();
        Film film3 = Film.builder()
                .id((long) 2)
                .name("testFilm")
                .description("testBadId 2")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(50)
                .rate(4)
                .mpa(mpaService.findById(1L))
                .build();
        assertThrows(NotFoundException.class, () -> filmService.update(film2), "Фильм обновлен");
        assertThrows(NotFoundException.class, () -> filmService.update(film3), "Фильм обновлен");
    }
}