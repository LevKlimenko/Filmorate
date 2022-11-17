package ru.yandex.practicum.filmorate.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.CrudService;
import ru.yandex.practicum.filmorate.storages.user.UserFriendDbStorage;
import ru.yandex.practicum.filmorate.storages.user.UserStorage;

import java.util.List;

@Service
public class UserDbService implements CrudService<User>, UserFriendDbService {
    private final UserStorage userStorage;
    private final UserFriendDbStorage userFriendDbStorage;

    @Autowired
    public UserDbService(UserStorage userStorageImpl, UserFriendDbStorage userFriendDbStorage) {
        this.userStorage = userStorageImpl;
        this.userFriendDbStorage = userFriendDbStorage;
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User create(User user) {
        return userStorage.create(user);
    }

    @Override
    public User update(User user) {
        return userStorage.update(user);
    }

    @Override
    public User findById(Long id) {
        return userStorage.findById(id);
    }

    @Override
    public boolean becomeFriend(Long userId1, Long userId2) {
        return userFriendDbStorage.becomeFriend(userId1, userId2);
    }

    @Override
    public boolean stopBeingFriends(Long userId1, Long userId2) {
        return userFriendDbStorage.stopBeingFriends(userId1, userId2);
    }

    @Override
    public List<User> showAllUserFriends(Long userId) {
        return userFriendDbStorage.showAllUserFriends(userId);
    }

    @Override
    public List<User> showIntersectionFriends(Long userId1, Long userId2) {
        return userFriendDbStorage.showIntersectionFriends(userId1, userId2);
    }
}