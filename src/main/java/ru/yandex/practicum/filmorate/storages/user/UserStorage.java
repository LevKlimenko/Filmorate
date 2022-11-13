package ru.yandex.practicum.filmorate.storages.user;

import ru.yandex.practicum.filmorate.models.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> getAll();

    User create(User user);

    User update(User user);

    User findById(Long id);

    boolean isExist(Long id);
}
