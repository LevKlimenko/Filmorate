package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.user.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Set;


@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getAllUser() {
        return userService.getAll();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping("/{userId}")
    public User findUser(@PathVariable("userId") Long userId) {
        return userService.findById(userId);
    }

    @PutMapping("/{user1}/friends/{user2}")
    public User addFriends(@PathVariable("user1") Long userId1,
                           @PathVariable("user2") Long userId2) {
        userService.becomeFriend(userId1, userId2);
        return userService.getUsers().get(userId1);
    }

    @DeleteMapping("/{user1}/friends/{user2}")
    public User deleteFriends(@PathVariable("user1") Long userId1,
                              @PathVariable("user2") Long userId2) {
        userService.stopBeingFriends(userId1, userId2);
        return userService.getUsers().get(userId1);
    }

    @GetMapping("/{id}/friends")
    public Set<User> showUserFriends(@PathVariable("id") Long userId) {
        return userService.showAllUserFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> showIntersectionFriends(@PathVariable("id") Long userId1,
                                             @PathVariable("otherId") Long userId2) {
        return userService.showIntersectionFriends(userId1, userId2);
    }
}

