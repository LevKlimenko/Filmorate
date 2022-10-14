package ru.yandex.practicum.filmorate.services.user;

import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.FilmorateService;

import java.util.Set;

public interface UserFriend extends FilmorateService<User> {

    void becomeFriend(Long userId1, Long userId2);

    void stopBeingFriends(Long userId1, Long userId2);

    Set<User> showAllUserFriends(Long userId);

    Set<User> showIntersectionFriends(Long userId1, Long userId2);
}
