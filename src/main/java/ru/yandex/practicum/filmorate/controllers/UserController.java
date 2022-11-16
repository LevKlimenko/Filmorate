package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.CrudService;
import ru.yandex.practicum.filmorate.services.user.UserFriendDbService;

import javax.validation.Valid;
import java.util.List;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserFriendDbService userFriendDbService;
    private final CrudService<User> userService;

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        checkBlankAndNullName(user);
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        checkBlankAndNullName(user);
        return userService.update(user);
    }

    @GetMapping("/{userId}")
    public User getById(@PathVariable("userId") Long userId) {
        return userService.findById(userId);
    }

    @PutMapping("/{user1}/friends/{user2}")
    public User addFriends(@PathVariable("user1") Long userId1,
                           @PathVariable("user2") Long userId2) {
        userFriendDbService.becomeFriend(userId1, userId2);
        return userService.findById(userId1);
    }

    @DeleteMapping("/{user1}/friends/{user2}")
    public User deleteFriends(@PathVariable("user1") Long userId1,
                              @PathVariable("user2") Long userId2) {
        userFriendDbService.stopBeingFriends(userId1, userId2);
        return userService.findById(userId1);
    }

    @GetMapping("/{id}/friends")
    public List<User> showFriends(@PathVariable("id") Long userId) {
        return userFriendDbService.showAllUserFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> showIntersectionFriends(@PathVariable("id") Long userId1,
                                              @PathVariable("otherId") Long userId2) {
        return userFriendDbService.showIntersectionFriends(userId1, userId2);
    }

    private void checkBlankAndNullName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}