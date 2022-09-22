package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final HashMap<Integer, User> users = new HashMap<>();
    private int generatorId;

    @GetMapping
    public Collection<User> getAllUser() {
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if (user.getEmail() == null) {
            throw new UserWithoutEmailException("Отсутствует e-mail у пользователя");
        }
        if (!user.getEmail().contains("@")) {
            throw new UserBadEmailException("e-mail должен содержать знак '@'");
        }
        if (user.getLogin() == null) {
            throw new UserWithoutLoginException("Логин не может быть пустым");
        }
        if (user.getLogin().contains(" ")) {
            throw new UserBadLoginException("Логин не может содержать пробелы");
        }
        if (users.containsKey(user.getId())) {
            throw new UserAlreadyExistException("Пользователь с ID=" + user.getId() + " уже существует");
        }
        if (users.values().toString().contains(user.getEmail())) {
            throw new UserAlreadyExistException("Пользователь с электронной почтой " + user.getEmail() +
                    " уже существует");
        }
        if (users.values().toString().contains(user.getLogin())) {
            throw new UserAlreadyExistException("Пользователь с логином " + user.getLogin() + " уже существует");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new UserBadBirthdayException("Дата рождения не может быть в будущем");
        }
        generatorId = ++generatorId;
        user.setId(generatorId);
        users.put(user.getId(), user);
        log.info("Пользователь '{}' с электронной почтой '{}' сохранен. ID={}", user.getLogin(), user.getEmail(),
                user.getId());
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (user.getId() == null) {
            throw new UserWithoutIdException("Нельзя обновить пользователя, если не указан ID");
        }
        if (user.getId() < 1) {
            throw new UserIdException("Id должен быть больше 0");
        }
        if (!users.containsKey(user.getId())) {
            throw new UserIdException("Нет пользователя с ID=" + user.getId());
        }
        if (user.getEmail() == null) {
            throw new UserWithoutEmailException("Отсутствует e-mail у пользователя " + user.getName());
        }
        if (!user.getEmail().contains("@")) {
            throw new UserBadEmailException("e-mail должен содержать знак '@'");
        }
        if (user.getLogin() == null) {
            throw new UserWithoutLoginException("Логин не может быть пустым");
        }
        if (user.getLogin().contains(" ")) {
            throw new UserBadLoginException("Логин не может содержать пробелы");
        }
        if (user.getName() == null)
            user.setName(user.getLogin());
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new UserBadBirthdayException("Дата рождения не может быть в будущем");
        }
        users.put(user.getId(), user);
        log.info("Пользователь c ID={} обновлен", user.getId());
        return user;
    }

    public HashMap<Integer, User> getUsers() {
        return new HashMap<>(users);
    }

    public int getGeneratorId() {
        return generatorId;
    }
}
