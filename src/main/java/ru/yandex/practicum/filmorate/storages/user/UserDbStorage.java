package ru.yandex.practicum.filmorate.storages.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.User;

import java.util.Collection;
import java.util.Map;


@Component
public class UserDbStorage implements UserStorage{
    @Override
    public Collection<User> getUser() {
        return null;
    }

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public User findById(Long id) {
        return null;
    }

    @Override
    public Map<Long, User> getMap() {
        return null;
    }

    @Override
    public long getGeneratorId() {
        return 0;
    }

    @Override
    public boolean isExist(Long id) {
        return false;
    }
}
