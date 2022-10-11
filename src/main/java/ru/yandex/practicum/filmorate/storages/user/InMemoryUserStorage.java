package ru.yandex.practicum.filmorate.storages.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.exceptions.ConflictException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.models.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> usersEmailInBase = new HashSet<>();
    private final Set<String> usersLoginInBase = new HashSet<>();
    private long generatorId;

    @Override
    public Collection<User> getUser() {
        return users.values();
    }

    @Override
    public User create(User user) {
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

    @Override
    public User update(User user) {
        if (user.getId() == null) {
            throw new BadRequestException("Нельзя обновить пользователя, если не указан ID");
        }
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Нет пользователя с ID=" + user.getId());
        }
        checkSpaceInLogin(user);
        checkValidateName(user);
        users.put(user.getId(), user);
        log.info("Пользователь c ID={} обновлен", user.getId());
        return user;
    }

    @Override
    public User findById(Long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException(String.format("Пользователь № %d не найден", userId));
        }
        return users.get(userId);
    }

    private void checkSpaceInLogin(User user) {
        if (user.getLogin().contains(" "))
            throw new BadRequestException("Логин не может содержать пробелы");
    }

    private void checkValidateName(User user) {
        if (user.getLogin() != null || !user.getLogin().isBlank()) {
            if (user.getName() == null || user.getName().isBlank())
                user.setName(user.getLogin());
        } else {
            throw new BadRequestException("Логин не может быть пустым");
        }
    }

    private void checkAlreadyExistUser(User user) {
        if (usersEmailInBase.contains(user.getEmail())) {
            throw new ConflictException("Пользователь с электронной почтой " + user.getEmail() +
                    " уже существует");
        }
        if (usersLoginInBase.contains(user.getLogin())) {
            throw new ConflictException("Пользователь с логином " + user.getLogin() + " уже существует");
        }
    }

    @Override
    public Map<Long, User> getMap() {
        return new HashMap<>(users);
    }

    @Override
    public long getGeneratorId() {
        return generatorId;
    }

    @Override
    public boolean isExist(Long id) {
        return users.containsKey(id);
    }
}
