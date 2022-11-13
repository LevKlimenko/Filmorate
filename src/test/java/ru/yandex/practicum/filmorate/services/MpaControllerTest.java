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
import ru.yandex.practicum.filmorate.models.Mpa;
import ru.yandex.practicum.filmorate.services.mpa.MpaService;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Validated
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaControllerTest {
    private final JdbcTemplate jdbcTemplate;

    private final MpaService mpaService;

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
    public void getAllMpa() {
        Collection<Mpa> allMpa = mpaService.getAll();
        assertEquals(5, allMpa.size(), "Количество MPA не совпадает");
    }

    @Test
    public void getGoodIdMpa() {
        Mpa mpa = mpaService.findById(1L);
        assertEquals("G", mpa.getName(), "Имя MPA не совпадает");
        assertEquals(1, mpa.getId(), "ID MPA не совпадает");
    }

    @Test
    public void getBadIdMpa(){
        assertThrows(NotFoundException.class,() ->mpaService.findById(10L), "MPA найден");
    }
}
