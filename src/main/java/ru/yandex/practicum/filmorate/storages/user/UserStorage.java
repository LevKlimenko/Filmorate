package ru.yandex.practicum.filmorate.storages.user;

import ru.yandex.practicum.filmorate.models.User;

import java.util.List;

public interface UserStorage {
    List<User> getAll();

    User create(User user);

    User update(User user);

    User findById(Long id);

    boolean isExist(Long id);
}