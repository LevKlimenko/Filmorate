package ru.yandex.practicum.filmorate.services.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UsersAlreadyFriendsException;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.storages.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class UserService implements UserFriendService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    @Override
    public Collection<User> getAll() {
        return userStorage.getUser();
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
        if (userStorage.isExist(userId1) && userStorage.isExist(userId2)) {
            User user1 = userStorage.getMap().get(userId1);
            User user2 = userStorage.getMap().get(userId2);
            if (!userId1.equals(userId2)) {
                if (!user1.getFriendsId().contains(user2.getId())) {
                    user1.getFriendsId().add(user2.getId());
                    user2.getFriendsId().add(user1.getId());
                } else {
                    throw new UsersAlreadyFriendsException("Уже находятся в друзьях друг у друга");
                }
            } else {
                throw new RuntimeException("Необходимо указать разных пользователей");
            }
        } else {
            throw new NotFoundException("Нельзя задать несуществующего пользователя");
        }
        return true;
    }

    @Override
    public boolean stopBeingFriends(Long userId1, Long userId2) {
        if (userStorage.isExist(userId1) && userStorage.isExist(userId2)) {
            User user1 = userStorage.getMap().get(userId1);
            User user2 = userStorage.getMap().get(userId2);
            if (!userId1.equals(userId2)) {
                if (user1.getFriendsId().contains(user2.getId())) {
                    user1.getFriendsId().remove(user2.getId());
                    user2.getFriendsId().remove(user1.getId());
                } else {
                    throw new BadRequestException("Пользователей нет в друзьях друг у друга");
                }
            } else {
                throw new BadRequestException("Необходимо указать разных пользователей");
            }
        } else {
            throw new NotFoundException("Нельзя задать несуществующего пользователя");
        }
        return true;
    }

    @Override
    public List<User> showAllUserFriends(Long userId) {
        List<User> friends = new ArrayList<>();
        for (Long id : userStorage.getMap().get(userId).getFriendsId()) {
            friends.add(userStorage.getMap().get(id));
        }
        return friends;
    }

    @Override
    public List<User> showIntersectionFriends(Long userId1, Long userId2) {
        User user1 = userStorage.getMap().get(userId1);
        User user2 = userStorage.getMap().get(userId2);
        List<Long> interFriendsId = new ArrayList<>(user1.getFriendsId());
        List<User> interFriends = new ArrayList<>();
        interFriendsId.retainAll(user2.getFriendsId());
        for (Long id : interFriendsId) {
            interFriends.add(userStorage.getMap().get(id));
        }
        return interFriends;
    }

    public Map<Long, User> getMap() {
        return userStorage.getMap();
    }


}
