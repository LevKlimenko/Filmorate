package ru.yandex.practicum.filmorate.services.user;

import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.CrudService;

import java.util.List;

public interface UserFriendService extends CrudService<User> {

    void becomeFriend(Long userId1, Long userId2);

    void stopBeingFriends(Long userId1, Long userId2);

    List<User> showAllUserFriends(Long userId);

    List<User> showIntersectionFriends(Long userId1, Long userId2);
}