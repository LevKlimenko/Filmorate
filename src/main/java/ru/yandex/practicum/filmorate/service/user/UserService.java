package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.userServiceException.UserNullException;
import ru.yandex.practicum.filmorate.exceptions.userServiceException.UsersAlreadyFriendsException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAllUser(){
        return userStorage.getAllUser();
    }

    public User createUser(User user){
        return userStorage.createUser(user);
    }

    public User updateUser(User user){
        return userStorage.updateUser(user);
    }

    public User findUserById(Integer userId){
        return userStorage.findUserById(userId);
    }
    public void becomeFriend(Integer userId1, Integer userId2) {
        if (userStorage.getUsers().get(userId1) != null && userStorage.getUsers().get(userId2) != null) {
            User user1 = userStorage.getUsers().get(userId1);
            User user2 = userStorage.getUsers().get(userId2);
            if (!userId1.equals(userId2)) {
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

    public void stopBeingFriends(Integer userId1, Integer userId2) {
        if (userStorage.getUsers().get(userId1) != null && userStorage.getUsers().get(userId2) != null) {
            User user1 = userStorage.getUsers().get(userId1);
            User user2 = userStorage.getUsers().get(userId2);
            if (!userId1.equals(userId2)) {
                if (user1.getFriends().contains(user2.getId())) {
                    user1.getFriends().remove(user2.getId());
                    user2.getFriends().remove(user1.getId());
                } else {
                    throw new UsersAlreadyFriendsException("Пользователей нет в друзьях друг у друга");
                }
            } else {
                throw new RuntimeException("Необходимо указать разных пользователей");
            }
        } else {
            throw new UserNullException("Нельзя задать несуществующего пользователя");
        }
    }

    public Set<User> showAllUserFriends(Integer userId) {
        Set<User> friends = new HashSet<>();
        for (Integer id : userStorage.getUsers().get(userId).getFriends()) {
            friends.add(userStorage.getUsers().get(id));
        }
        return friends;
    }

    public Set<User> showIntersectionFriends(Integer userId1, Integer userId2) {
        User user1 = userStorage.getUsers().get(userId1);
        User user2 = userStorage.getUsers().get(userId2);
        Set<Integer> interFriendsId = new HashSet<>(user1.getFriends());
        Set<User> interFriends = new HashSet<>();
        interFriendsId.retainAll(user2.getFriends());
        for (Integer id : interFriendsId) {
            interFriends.add(userStorage.getUsers().get(id));
        }
        return interFriends;
    }

    public Map<Integer, User> getUsers(){
        return userStorage.getUsers();
    }
}
