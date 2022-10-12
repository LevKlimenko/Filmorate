package ru.yandex.practicum.filmorate.storages.user;

import ru.yandex.practicum.filmorate.models.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {
    Collection<User> getUser();

    User create(User user);

    User update(User user);

    User findById(Long id);

    Map<Long, User> getMap();

    long getGeneratorId();

    boolean isExist(Long id);
}
