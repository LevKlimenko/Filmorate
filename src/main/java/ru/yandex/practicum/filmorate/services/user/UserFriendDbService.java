package ru.yandex.practicum.filmorate.services.user;

import ru.yandex.practicum.filmorate.models.User;

import java.util.List;

public interface UserFriendDbService {

    boolean becomeFriend(Long userId1, Long userId2);

    boolean stopBeingFriends(Long userId1, Long userId2);

    List<User> showAllUserFriends(Long userId);

    List<User> showIntersectionFriends(Long userId1, Long userId2);
}
