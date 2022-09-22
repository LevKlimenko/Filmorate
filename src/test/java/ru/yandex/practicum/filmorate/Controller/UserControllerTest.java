package ru.yandex.practicum.filmorate.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException.UserBadLoginException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException.UserIdException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException.UserWithoutIdException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootApplication
public class UserControllerTest {

    User user;

    UserController uc = new UserController();


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
        uc.createUser(user);
        assertEquals(1, uc.getAllUser().size(), "Количество пользователей не совпадает");
    }

    @Test
    public void postTwoStandartUser() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        uc.createUser(user);
        User user2 = User.builder()
                .email("testUser2@yandex.ru")
                .login("testLogin2")
                .name("testName2")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        uc.createUser(user2);
        assertEquals(2, uc.getAllUser().size(), "Количество пользователей не совпадает");
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
        assertThrows(UserBadLoginException.class, () -> uc.createUser(user), "Добавлен пользователь " + user.getLogin());
    }

    @Test
    public void postNullNameUser() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        uc.createUser(user);
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
        uc.createUser(user);
        User user2 = User.builder()
                .id(2)
                .email("testUserUpdate@yandex.ru")
                .login("testLogin")
                .name("testNameUpdate")
                .birthday(LocalDate.now())
                .build();
        assertThrows(UserAlreadyExistException.class, () -> uc.createUser(user2), "Пользователь добавлен");
    }

    @Test
    public void postAlreadyExistEmailUser() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.now())
                .build();
        uc.createUser(user);
        User user2 = User.builder()
                .id(2)
                .email("testUser@yandex.ru")
                .login("testLoginUpdate")
                .name("testNameUpdate")
                .birthday(LocalDate.now())
                .build();
        assertThrows(UserAlreadyExistException.class, () -> uc.createUser(user2), "Пользователь добавлен");
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
        uc.createUser(user);
        User user2 = User.builder()
                .id(user.getId())
                .email("testUserUpdate@yandex.ru")
                .login("testLoginUpdate")
                .name("testNameUpdate")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        uc.updateUser(user2);
        assertEquals(1, uc.getAllUser().size(), "Количество пользователей не совпадает");
        assertEquals(user2, uc.getUsers().get(1), "Пользователи не совпадают");
    }

    @Test
    public void putUpdatedUserWithoutId() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        uc.createUser(user);
        User user2 = User.builder()
                .email("testUserUpdate@yandex.ru")
                .login("testLoginUpdate")
                .name("testNameUpdate")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        assertThrows(UserWithoutIdException.class, () -> uc.updateUser(user2), "Пользователь обновлен");
    }

    @Test
    public void putFailIdUser() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        uc.createUser(user);
        User user2 = User.builder()
                .id(-1)
                .email("testUserUpdate@yandex.ru")
                .login("testLoginUpdate")
                .name("testNameUpdate")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        User user3 = User.builder()
                .id(5)
                .email("testUserUpdate@yandex.ru")
                .login("testLoginUpdate")
                .name("testNameUpdate")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        assertThrows(UserIdException.class, () -> uc.updateUser(user2), "Пользователь c ID=" +
                user2.getId() + " обновлен");
        assertThrows(UserIdException.class, () -> uc.updateUser(user3), "Пользователь c ID=" +
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
        uc.createUser(user);
        User user2 = User.builder()
                .id(user.getId())
                .email("testUserUpdate@yandex.ru")
                .login("testLogin Update")
                .name("testNameUpdate")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        assertThrows(UserBadLoginException.class, () -> uc.updateUser(user2), "Пользователь c ID=" +
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
        uc.createUser(user);
        User user2 = User.builder()
                .id(user.getId())
                .email("testUserUpdate@yandex.ru")
                .login("testLoginUpdate")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        uc.updateUser(user2);
        assertEquals(user2.getLogin(), user2.getName(), "Имя и Логин не совпадают");
    }

}
