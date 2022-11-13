package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.user.UserDbService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {

    private final JdbcTemplate jdbcTemplate;
    private final UserDbService userService;
    User user;

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
    public void postStandartUser() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userService.create(user);
        assertEquals(1, userService.getAll().size(), "Количество пользователей не совпадает");
    }

    @Test
    public void postTwoStandartUser() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userService.create(user);
        User user2 = User.builder()
                .email("testUser2@yandex.ru")
                .login("testLogin2")
                .name("testName2")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userService.create(user2);
        assertEquals(2, userService.getAll().size(), "Количество пользователей не совпадает");

    }

    @Test
    public void postBadLoginUser() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("test Login")
                .name("testName")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        assertThrows(DataIntegrityViolationException.class, () -> userService.create(user), "Добавлен пользователь " + user.getLogin());
    }

    @Test
    public void postBadBirthdayUser() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.of(2050, 10, 10))
                .build();
        assertThrows(DataIntegrityViolationException.class, () -> userService.create(user), "Добавлен пользователь " + user.getLogin());
    }

    @Test
    public void postNullNameUser() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userService.create(user);
        assertEquals(user.getLogin(), user.getName(), "Имя не совпадает с логином");
    }

    @Test
    void postAlreadyExistLoginUser() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.now())
                .build();
        userService.create(user);
        User user2 = User.builder()
                .id((long) 2)
                .email("testUserUpdate@yandex.ru")
                .login("testLogin")
                .name("testNameUpdate")
                .birthday(LocalDate.now())
                .build();
        assertThrows(DuplicateKeyException.class, () -> userService.create(user2), "Пользователь добавлен");
    }

    @Test
    public void postAlreadyExistEmailUser() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.now())
                .build();
        userService.create(user);
        User user2 = User.builder()
                .id((long) 2)
                .email("testUser@yandex.ru")
                .login("testLoginUpdate")
                .name("testNameUpdate")
                .birthday(LocalDate.now())
                .build();
        assertThrows(DuplicateKeyException.class, () -> userService.create(user2), "Пользователь добавлен");
    }


    /**
     * TEST PUT
     */

    @Test
    public void putStandartUser() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userService.create(user);
        User user2 = User.builder()
                .id(user.getId())
                .email("testUserUpdate@yandex.ru")
                .login("testLoginUpdate")
                .name("testNameUpdate")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userService.update(user2);
        assertEquals(1, userService.getAll().size(), "Количество пользователей не совпадает");
    }

    @Test
    public void putUpdatedUserWithoutId() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userService.create(user);
        User user2 = User.builder()
                .email("testUserUpdate@yandex.ru")
                .login("testLoginUpdate")
                .name("testNameUpdate")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        assertThrows(BadRequestException.class, () -> userService.update(user2), "Пользователь обновлен");
    }

    @Test
    public void putFailIdUser() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userService.create(user);
        User user2 = User.builder()
                .id((long) -1)
                .email("testUserUpdate@yandex.ru")
                .login("testLoginUpdate")
                .name("testNameUpdate")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        User user3 = User.builder()
                .id((long) 5)
                .email("testUserUpdate@yandex.ru")
                .login("testLoginUpdate")
                .name("testNameUpdate")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        assertThrows(NotFoundException.class, () -> userService.update(user2), "Пользователь c ID=" +
                user2.getId() + " обновлен");
        assertThrows(NotFoundException.class, () -> userService.update(user3), "Пользователь c ID=" +
                user3.getId() + " обновлен");
    }

    @Test
    public void putBadLoginUser() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userService.create(user);
        User user2 = User.builder()
                .id(user.getId())
                .email("testUserUpdate@yandex.ru")
                .login("testLogin Update")
                .name("testNameUpdate")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        assertThrows(DataIntegrityViolationException.class, () -> userService.update(user2), "Пользователь c ID=" +
                user2.getId() + " обновлен");
    }

    @Test
    public void putWithoutNameUser() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userService.create(user);
        User user2 = User.builder()
                .id(user.getId())
                .email("testUserUpdate@yandex.ru")
                .login("testLoginUpdate")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userService.update(user2);
        assertEquals(user2.getLogin(), user2.getName(), "Имя и Логин не совпадают");
    }

    /**
     * TEST FRIENDS
     */


}