package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException.UserBadLoginException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException.UserIdException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException.UserWithoutIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.*;

/**
 * С помощью аннотации @PathVariable добавьте возможность получать данные
 * о пользователях по их уникальному идентификатору: GET .../users/{id}.
 * PUT /users/{id}/friends/{friendId} — добавление в друзья.
 * DELETE /users/{id}/friends/{friendId} — удаление из друзей.
 * GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
 * GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
 */
@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
       this.userService = userService;
    }


    @GetMapping
    public Collection<User> getAllUser() {
        return userService.getAllUser();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/{userId}")
    public User findUser(@PathVariable("userId") Integer userId) {
        return userService.findUserById(userId);
    }

    @PutMapping("/{user1}/friends/{user2}")
    public User addFriends(@PathVariable("user1") Integer userId1,
                           @PathVariable("user2") Integer userId2) {
        userService.becomeFriend(userId1, userId2);
        return userService.getUsers().get(userId1);
    }

    @DeleteMapping("/{user1}/friends/{user2}")
    public User deleteFriends(@PathVariable("user1") Integer userId1,
                              @PathVariable("user2") Integer userId2) {
        userService.stopBeingFriends(userId1, userId2);
        return userService.getUsers().get(userId1);
    }

    @GetMapping("/{id}/friends")
    public Set<User> showUserFriends(@PathVariable("id") Integer userId) {
        return userService.showAllUserFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> showIntersectionFriends(@PathVariable("id") Integer userId1,
                                              @PathVariable("otherId") Integer userId2) {
        return userService.showIntersectionFriends(userId1, userId2);
    }


}

