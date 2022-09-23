package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException.UserBadLoginException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException.UserIdException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException.UserWithoutIdException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Integer, User> users = new HashMap<>();
    private final Set<String> usersEmailInBase = new HashSet<>();
    private final Set<String> usersLoginInBase = new HashSet<>();
    private int generatorId;

    @GetMapping
    public Collection<User> getAllUser() {
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        checkSpaceInLogin(user);
        checkAlreadyExistUser(user);
        checkValidateName(user);
        ++generatorId;
        user.setId(generatorId);
        users.put(user.getId(), user);
        usersEmailInBase.add(user.getEmail());
        usersLoginInBase.add(user.getLogin());
        log.info("Пользователь '{}' с электронной почтой '{}' сохранен. ID={}", user.getLogin(), user.getEmail(),
                user.getId());
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (user.getId() == null) {
            throw new UserWithoutIdException("Нельзя обновить пользователя, если не указан ID");
        }
        if (!users.containsKey(user.getId())) {
            throw new UserIdException("Нет пользователя с ID=" + user.getId());
        }
        checkSpaceInLogin(user);
        checkValidateName(user);
        users.put(user.getId(), user);
        log.info("Пользователь c ID={} обновлен", user.getId());
        return user;
    }

    private void checkSpaceInLogin(User user) {
        if (user.getLogin().contains(" "))
            throw new UserBadLoginException("Логин не может содержать пробелы");
    }

    private void checkValidateName(User user) {
        if (user.getName() == null || user.getLogin().isBlank())
            user.setName(user.getLogin());
    }

    private void checkAlreadyExistUser(User user) {
        if (usersEmailInBase.contains(user.getEmail())) {
            throw new UserAlreadyExistException("Пользователь с электронной почтой " + user.getEmail() +
                    " уже существует");
        }
        if (usersLoginInBase.contains(user.getLogin())) {
            throw new UserAlreadyExistException("Пользователь с логином " + user.getLogin() + " уже существует");
        }
    }

    public HashMap<Integer, User> getUsers() {
        return new HashMap<>(users);
    }

    public int getGeneratorId() {
        return generatorId;
    }
}
