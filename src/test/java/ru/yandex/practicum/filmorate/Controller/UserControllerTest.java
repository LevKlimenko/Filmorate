package ru.yandex.practicum.filmorate.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storages.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootApplication
public class UserControllerTest {

    User user;

    InMemoryUserStorage uc = new InMemoryUserStorage();

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
        uc.create(user);
        assertEquals(1, uc.getUser().size(), "Количество пользователей не совпадает");
    }

    @Test
    public void postTwoStandartUser() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        uc.create(user);
        User user2 = User.builder()
                .email("testUser2@yandex.ru")
                .login("testLogin2")
                .name("testName2")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        uc.create(user2);
        assertEquals(2, uc.getUser().size(), "Количество пользователей не совпадает");
        assertEquals(2, uc.getGeneratorId(), "Последнее ID не совпадает");
    }

    @Test
    public void postBadLoginUser() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("test Login")
                .name("testName")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        assertThrows(BadRequestException.class, () -> uc.create(user), "Добавлен пользователь " + user.getLogin());
    }

    @Test
    public void postNullNameUser() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        uc.create(user);
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
        uc.create(user);
        User user2 = User.builder()
                .id((long) 2)
                .email("testUserUpdate@yandex.ru")
                .login("testLogin")
                .name("testNameUpdate")
                .birthday(LocalDate.now())
                .build();
        assertThrows(BadRequestException.class, () -> uc.create(user2), "Пользователь добавлен");
    }

    @Test
    public void postAlreadyExistEmailUser() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.now())
                .build();
        uc.create(user);
        User user2 = User.builder()
                .id((long) 2)
                .email("testUser@yandex.ru")
                .login("testLoginUpdate")
                .name("testNameUpdate")
                .birthday(LocalDate.now())
                .build();
        assertThrows(BadRequestException.class, () -> uc.create(user2), "Пользователь добавлен");
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
        uc.create(user);
        User user2 = User.builder()
                .id(user.getId())
                .email("testUserUpdate@yandex.ru")
                .login("testLoginUpdate")
                .name("testNameUpdate")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        uc.update(user2);
        assertEquals(1, uc.getUser().size(), "Количество пользователей не совпадает");
        assertEquals(user2, uc.getMap().get((long)1), "Пользователи не совпадают");
    }

    @Test
    public void putUpdatedUserWithoutId() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        uc.create(user);
        User user2 = User.builder()
                .email("testUserUpdate@yandex.ru")
                .login("testLoginUpdate")
                .name("testNameUpdate")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        assertThrows(BadRequestException.class, () -> uc.update(user2), "Пользователь обновлен");
    }

    @Test
    public void putFailIdUser() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        uc.create(user);
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
        assertThrows(NotFoundException.class, () -> uc.update(user2), "Пользователь c ID=" +
                user2.getId() + " обновлен");
        assertThrows(NotFoundException.class, () -> uc.update(user3), "Пользователь c ID=" +
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
        uc.create(user);
        User user2 = User.builder()
                .id(user.getId())
                .email("testUserUpdate@yandex.ru")
                .login("testLogin Update")
                .name("testNameUpdate")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        assertThrows(BadRequestException.class, () -> uc.update(user2), "Пользователь c ID=" +
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
        uc.create(user);
        User user2 = User.builder()
                .id(user.getId())
                .email("testUserUpdate@yandex.ru")
                .login("testLoginUpdate")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        uc.update(user2);
        assertEquals(user2.getLogin(), user2.getName(), "Имя и Логин не совпадают");
    }
}