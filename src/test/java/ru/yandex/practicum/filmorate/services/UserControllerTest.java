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
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.exceptions.ConflictException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.user.UserFriendService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {

    private final JdbcTemplate jdbcTemplate;
    private final UserFriendService userFriendService;
    private final CrudService<User> userService;
    private final UserController userController;
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
        userController.createUser(user);
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
        userController.updateUser(user2);
        assertEquals(user2.getLogin(), user2.getName(), "Имя и Логин не совпадают");
    }

    /**
     * TEST FRIENDS
     */

    @Test
    public void checkFriendsWithoutAdd() {
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
                .name("user2")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userService.create(user2);
        assertEquals(0, userFriendService.showAllUserFriends(1L).size(), "Количество друзей пользователя 1 неверное");
        assertEquals(0, userFriendService.showAllUserFriends(2L).size(), "Количество друзей пользователя 2 неверное");
    }

    @Test
    public void addNormalUser1AsFriendUser2() {
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
                .name("user2")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userService.create(user2);
        userFriendService.becomeFriend(user.getId(), user2.getId());
        assertEquals(1, userFriendService.showAllUserFriends(1L).size(), "Количество друзей у пользователя 1 отличается");
        assertEquals(userService.findById(user2.getId()), userFriendService.showAllUserFriends(1L).get(0), "Друг пользователя 1 отличается");
        assertEquals(0, userFriendService.showAllUserFriends(2L).size(), "Количество друзей у пользователя 2 отличается");
    }

    @Test
    public void addUser1FriendHimself() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userService.create(user);
        assertThrows(ConflictException.class, () -> userFriendService.becomeFriend(user.getId(), user.getId()), "Пользователь стал сам себе другом");
    }

    @Test
    public void addAgainUser1AsFriendUser2() {
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
                .name("user2")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userService.create(user2);
        userFriendService.becomeFriend(user.getId(), user2.getId());
        assertThrows(ConflictException.class, () -> userFriendService.becomeFriend(user.getId(), user2.getId()), "Пользователь опять добавил друга в друзья");
    }

    @Test
    public void deleteNormalUser1AsFriendNotFoundUser2() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userService.create(user);
        assertThrows(NotFoundException.class, () -> userFriendService.stopBeingFriends(user.getId(), -5L), "Дружба прекращена");
    }

    @Test
    public void addNormalUser1AsFriendNotFoundUser2() {
        user = User.builder()
                .email("testUser@yandex.ru")
                .login("testLogin")
                .name("testName")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userService.create(user);
        assertThrows(NotFoundException.class, () -> userFriendService.becomeFriend(user.getId(), -5L), "Пользователи стали друзьями");
    }

    @Test
    public void addNormalUser1AsFriendUser2matual() {
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
                .name("user2")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userService.create(user2);
        userFriendService.becomeFriend(user.getId(), user2.getId());
        userFriendService.becomeFriend(user2.getId(), user.getId());
        assertEquals(1, userFriendService.showAllUserFriends(1L).size(), "Количество друзей у пользователя 1 отличается");
        assertEquals(userService.findById(user2.getId()), userFriendService.showAllUserFriends(1L).get(0), "Друг пользователя 1 отличается");
        assertEquals(1, userFriendService.showAllUserFriends(2L).size(), "Количество друзей у пользователя 2 отличается");
        assertEquals(userService.findById(user.getId()), userFriendService.showAllUserFriends(2L).get(0), "Друг пользователя 2 отличается");
    }

    @Test
    public void addNormalUser1AsFriendUser2AndUser3() {
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
                .name("user2")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userService.create(user2);
        User user3 = User.builder()
                .email("testUser3@yandex.ru")
                .login("testLogin3")
                .name("user3")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userService.create(user3);
        userFriendService.becomeFriend(user.getId(), user2.getId());
        userFriendService.becomeFriend(user.getId(), user3.getId());
        assertEquals(2, userFriendService.showAllUserFriends(1L).size());
        assertEquals(userService.findById(user2.getId()), userFriendService.showAllUserFriends(1L).get(0), "Дружба 1-2 различается");
        assertEquals(userService.findById(user3.getId()), userFriendService.showAllUserFriends(1L).get(1), "Дружба 1-3 различается");
        assertEquals(0, userFriendService.showAllUserFriends(3L).size(), "Количество друзей у пользователя 3 не совпалает");
    }

    @Test
    public void addUsersAsFriendGetIntersectionAndDeleteFriend() {
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
                .name("user2")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userService.create(user2);
        User user3 = User.builder()
                .email("testUser3@yandex.ru")
                .login("testLogin3")
                .name("user3")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
        userService.create(user3);
        userFriendService.becomeFriend(user.getId(), user2.getId());
        userFriendService.becomeFriend(user.getId(), user3.getId());
        userFriendService.becomeFriend(user2.getId(), user3.getId());
        assertEquals(2, userFriendService.showAllUserFriends(1L).size(), "Количество друзей у пользователя 1 не совпадает");
        assertEquals(1, userFriendService.showAllUserFriends(2L).size(), "Количество друзей у пользователя 2 не совпадает");
        assertEquals(userService.findById(user2.getId()), userFriendService.showAllUserFriends(1L).get(0), "Дружба 1-2 не совпадает");
        assertEquals(userService.findById(user3.getId()), userFriendService.showAllUserFriends(1L).get(1), "Дружба 1-3 не совпадает");
        assertEquals(userService.findById(user3.getId()), userFriendService.showAllUserFriends(2L).get(0), "Дружба 2-3 не совпадает");
        assertEquals(1, userFriendService.showIntersectionFriends(user.getId(), user2.getId()).size(),
                "Количество общих друзей различается");
        assertEquals(userService.findById(user3.getId()),
                userFriendService.showIntersectionFriends(user.getId(), user2.getId()).get(0), "Общие друзья у пользователей 1 и 2 не совпадают");
        assertEquals(0, userFriendService.showAllUserFriends(3L).size(), "Количество друзей у пользователя 3 не совпадает");
        userFriendService.stopBeingFriends(user2.getId(), user3.getId());
        assertEquals(0, userFriendService.showAllUserFriends(2L).size(), "Количество друзей у пользователя 2 не совпадает");
        assertEquals(0, userFriendService.showIntersectionFriends(user.getId(), user2.getId()).size(),
                "Количество общих друзей различается");
    }
}