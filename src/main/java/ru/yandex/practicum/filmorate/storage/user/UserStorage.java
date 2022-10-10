package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {
    Collection<User> getAllUser();

    User createUser(User user);

    User updateUser(User user);

    User findUserById(Long id);

    Map<Long, User> getUsers();

    long getGeneratorId();
}
