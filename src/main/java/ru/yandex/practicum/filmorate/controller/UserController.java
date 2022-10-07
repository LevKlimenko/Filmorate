package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException.UserBadLoginException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException.UserIdException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException.UserWithoutIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.*;

/**
С помощью аннотации @PathVariable добавьте возможность получать данные
        о пользователях по их уникальному идентификатору: GET .../users/{id}.
 PUT /users/{id}/friends/{friendId} — добавление в друзья.
 DELETE /users/{id}/friends/{friendId} — удаление из друзей.
 GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
 GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
*/

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }


    @GetMapping
    public Collection<User> getAllUser() {
        return userStorage.getAllUser();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
       return userStorage.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
      return userStorage.updateUser(user);
    }

    @GetMapping("/{userId}")
    @ExceptionHandler
    public User findUser(@PathVariable("userId") Integer userId){
        return userStorage.findUserById(userId);
    }

    @PutMapping("/{user1}/friends/{user2}")
    public User addFriends(@PathVariable("user1") Integer user1,
                           @PathVariable("user2") Integer user2){
        userService.becomeFriend(user1,user2);
        return userStorage.getUsers().get(user1);
    }

}

