package ru.yandex.practicum.filmorate.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.CrudService;
import ru.yandex.practicum.filmorate.storages.user.UserDbStorage;

import java.util.Collection;

@Service
public class UserDbService implements CrudService<User> {
    private final UserDbStorage userDbStorage;

    @Autowired
    public UserDbService(UserDbStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

    @Override
    public Collection<User> getAll() {
        return userDbStorage.getAll();
    }

    @Override
    public User create(User user) {
        return userDbStorage.create(user);
    }

    @Override
    public User update(User user) {
        return userDbStorage.update(user);
    }

    @Override
    public User findById(Long id) {
        return userDbStorage.findById(id);
    }
}