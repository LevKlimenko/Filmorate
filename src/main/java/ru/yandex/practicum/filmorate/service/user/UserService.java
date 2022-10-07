package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.userServiceException.UserNullException;
import ru.yandex.practicum.filmorate.exceptions.userServiceException.UsersAlreadyFriendsException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;
    InMemoryUserStorage im = new InMemoryUserStorage();

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void becomeFriend(User user1, User user2) {
        if (user1 != null && user2 != null) {
            if (!user1.getId().equals(user2.getId())) {
                if (!user1.getFriends().contains(user2.getId())) {
                    user1.getFriends().add(user2.getId());
                    user2.getFriends().add(user1.getId());
                } else {
                    throw new UsersAlreadyFriendsException("Уже находятся в друзьях друг у друга");
                }
            } else {
                throw new RuntimeException("Необходимо указать разных пользователей");
            }
        } else {
            throw new UserNullException("Нельзя задать несуществующего пользователя");
        }
    }

    public void stopBeingFriends(User user1, User user2) {
        if (user1 != null && user2 != null) {
            if (!user1.getId().equals(user2.getId())) {
                if (user1.getFriends().contains(user2.getId())) {
                    user1.getFriends().remove(user2.getId());
                    user2.getFriends().remove(user1.getId());
                } else {
                    throw new UsersAlreadyFriendsException("Уже находятся в друзьях друг у друга");
                }
            } else {
                throw new RuntimeException("Необходимо указать разных пользователей");
            }
        } else {
            throw new UserNullException("Нельзя задать несуществующего пользователя");
        }
    }

    public Set<User> showAllFriendsForUser(User user) {
        Set<User> friends = new HashSet<>();
        for (Integer id : user.getFriends()) {
            friends.add(im.getUsers().get(id));
        }
        return friends;
    }

    public Set<User> showIntersectionFriends(User user1, User user2) {
        Set<Integer> interFriendsId = new HashSet<>(user1.getFriends());
        Set<User> interFriends = new HashSet<>();
        interFriendsId.retainAll(user2.getFriends());
        for (Integer id : interFriendsId) {
            interFriends.add(im.getUsers().get(id));
        }
        return interFriends;
    }
}
