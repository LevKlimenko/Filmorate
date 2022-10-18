package ru.yandex.practicum.filmorate.services.user;

import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.CrudService;

import java.util.Set;

public interface UserFriendService extends CrudService<User> {

    void becomeFriend(Long userId1, Long userId2);

    void stopBeingFriends(Long userId1, Long userId2);

    Set<User> showAllUserFriends(Long userId);

    Set<User> showIntersectionFriends(Long userId1, Long userId2);
}
